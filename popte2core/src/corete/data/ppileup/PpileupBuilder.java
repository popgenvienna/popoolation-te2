package corete.data.ppileup;

import corete.data.SamPair;
import corete.data.SamPairType;
import corete.data.TEFamilyShortcutTranslator;
import corete.io.ISamPairReader;
import corete.io.SamPairReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 8/21/15.
 */
public class PpileupBuilder {

	//immutable

	private  static final int maxChrPos= Integer.MAX_VALUE;

	private final int minMapQual;
	private final int averagePairDistance;
	private final ISamPairReader reader;
	private final TEFamilyShortcutTranslator translator;
	private final int srmd;

	// mutable - used for working
	// specific for each chromosomes
	private  HashMap<Integer,ArrayList<String>> construction;
	private int constructionDoneUntil;
	private String activeChr=null;
	private boolean requireChromosomeSwitch;
	private boolean extendClipped=false;
	private Logger logger;
	private int readcount;
	private int reportAt=20000;
	private int deleteCount;
	private int lastDeleted=0;
	private SamPair lastPair;

	// general whole thing
	private boolean eof;




	public PpileupBuilder(int minMapQual, int averagePairDistance, int srmd, ISamPairReader reader, TEFamilyShortcutTranslator translator, Logger logger)
	{
		// immutable
		this.minMapQual=minMapQual;
		this.averagePairDistance=averagePairDistance;
		this.srmd=srmd;
		this.reader=reader;
		this.translator=translator;
		this.constructionDoneUntil=-1;
		this.readcount=0;

		this.eof=false;
		this.logger=logger;
		this.logger.fine("Will used average distance "+averagePairDistance);
	}




	//region read Buffer - SamPair
	private SamPair bufferPair=null;
	private void bufferPair(SamPair pair)
	{
		assert(pair!=null);
		this.bufferPair=pair;
	}

	private SamPair nextPair()
	{
		if(this.bufferPair!=null)
		{
			SamPair toret=this.bufferPair;
			this.bufferPair=null;
			return toret;
		}
		else
		{
			if(this.reader.hasNext())
			{
				return this.reader.next();
			}
			else
			{
				return null;
			}

		}
	}
	//endregion






	/**
	 * Construction is done until which position?
	 * All positions smaller than this can already be retrieved
	 * @return
	 */
	public int doneUntil()
	{
		if(this.eof || this.requireChromosomeSwitch) return maxChrPos;
		return constructionDoneUntil;
	}


	private int setConstructionDone(SamPair sp)
	{
		int distance=this.averagePairDistance+this.srmd+10;
		int suggestedDistance=sp.getFirstRead().getStart()-distance;
		if(suggestedDistance<1) return this.constructionDoneUntil;


		if(suggestedDistance<this.constructionDoneUntil)
		{
			return this.constructionDoneUntil;

				//throw new IllegalStateException("Error, construction done until " + this.constructionDoneUntil + " ; Can not make it smaller " + suggestedDistance+
				//		" with read starting at "+sp.getFirstRead().getStart()+" read with id "+sp.getFirstRead().getReadname()
				//+ " previous read starting at "+lastPair.getFirstRead().getStart()+" with read id "+lastPair.getFirstRead().getReadname());

		}

		this.lastPair=sp;
		return suggestedDistance;



	}



	/**
	 * Add a new read to the construction
	 * four possible effect
	 * no effect
	 * shift position of doneUntil()
	 * make switch to new Chromosome necessary
	 * end of file may be reached
	 * return value true if a pair was added;
	 */
	public boolean addRead()
	{
		    SamPair sp=this.nextPair();


			// eof has been reached
			if(sp==null){this.eof=true;
				this.constructionDoneUntil=maxChrPos;
				return false;}

			// is a chromosome switch necessary
			if(!sp.getFirstRead().getRefchr().equals(activeChr))
			{
				this.requireChromosomeSwitch=true;    // set the flag that a chromosome switch is necessary
				this.bufferPair(sp);
				this.constructionDoneUntil=maxChrPos;
				return false;
			}


		constructionDoneUntil=setConstructionDone(sp);
		this.readcount++;
		if(this.readcount%this.reportAt==0) reportStatus();

		// ADD THE READ
		if(sp.getSamPairType()== SamPairType.Pair)
		   {
			   if(sp.isProperPair(this.minMapQual))
			   {
				   if( sp.getInnerDistance()<=2*this.averagePairDistance) {
					   // ok proper pair, and not exceeding max distance than => construct
					   addFromTo(sp.getFirstRead().getEnd() + 1, sp.getSecondRead().getStart() - 1, PpileupSymbols.ABS);
					   return true;
				   }else
				   {
					   addFromTo(sp.getFirstRead().getEnd()+1, sp.getFirstRead().getEnd()+averagePairDistance, PpileupSymbols.ABS);
					   addFromTo(sp.getSecondRead().getStart() - averagePairDistance,sp.getSecondRead().getStart() - 1, PpileupSymbols.ABS);

					   return true;
				   }

			   }
		   }
		else if(sp.getSamPairType()==SamPairType.BrokenPair)
		   {
			   if(sp.getFirstRead().getMapq()>=this.minMapQual) {
					addBrokenPair(sp);
				   	return true;
			   }
		   }
		else if(sp.getSamPairType()==SamPairType.TEInsert)
		{
			if(sp.getFirstRead().getMapq()>=this.minMapQual)
			{
				addTE(sp);
				return true;
			}
		}
		return false;
	}

	private void reportStatus()
	{
		int max=0;
		int leaks=0;
		for(int i:this.construction.keySet())
		{
			if(i>max)max=i;
			if(i<lastDeleted) leaks++;

		}

		this.logger.fine("");
		this.logger.fine("Added "+this.readcount+" reads; Now at chr. "+this.activeChr+" done until "+this.doneUntil());
		this.logger.fine("Sites deleted "+deleteCount+"; last deleted "+this.lastDeleted+" ; leaks "+leaks);
		this.logger.fine("Size of HashMap "+this.construction.size()+" with max. Position "+max);


	}




	private void  addBrokenPair(SamPair brokenPair)
	{
		String symbol;
		int start;
		int end;
		if(brokenPair.getFirstRead().isForwardStrand())
		{
			symbol=PpileupSymbols.SvFWD;
			start=brokenPair.getFirstRead().getEnd()+1;
			end=brokenPair.getFirstRead().getEnd()+this.averagePairDistance;
		}
		else
		{
			symbol=PpileupSymbols.SvRev;
			start=brokenPair.getFirstRead().getStart()-this.averagePairDistance;
			end=brokenPair.getFirstRead().getStart()-1;
		}
		addFromTo(start,end,symbol);
	}

	private void  addTE(SamPair tepair)
	{
		String symbol;
		int start;
		int end;
		String fam=tepair.getFamily();
		if(tepair.getFirstRead().isForwardStrand())
		{

			symbol=this.translator.getShortcutFwd(fam);

			start=tepair.getFirstRead().getEnd()+1;
			end=tepair.getFirstRead().getEnd()+this.averagePairDistance;
		}
		else
		{
			symbol=this.translator.getShortcutRev(fam);
			start=tepair.getFirstRead().getStart()-this.averagePairDistance;
			end=tepair.getFirstRead().getStart()-1;
		}

		if(symbol.length()>1)symbol=PpileupSymbols.TEstart+symbol+PpileupSymbols.TEend;
		addFromTo(start,end,symbol);
	}



	private void addFromTo(int start, int end, String symbol)
	{
		// test if at least one base will be added    start== end is allowed
		if(start<1) start=1;
		if(end-start<0) return;
		assert(symbol!=null);
		if(start<constructionDoneUntil) throw new IllegalStateException("Can not start writing at "+start +" ;construction done until "+constructionDoneUntil);



		// initate the whole stretch
		for(int i=start;i<=end; i++)
		{
			this.construction.putIfAbsent(i, new ArrayList<String>());
		}

		// add symbols including start and end positions
		this.construction.get(start).add(PpileupSymbols.PPSTART);
		for(int i=start; i<=end; i++)
		{
			this.construction.get(i).add(symbol);
		}
		this.construction.get(end).add(PpileupSymbols.PPEND);


	}



	/**
	 * Get finished site
	 * must be lower than doneUntil()
	 * @param site
	 * @return
	 */
	public String getSite(int site)
	{
		if(site>this.constructionDoneUntil)throw new IllegalStateException("Construction done until "+constructionDoneUntil +" ;Can not access "+site);

		// first deal with empty sites
		if(!construction.containsKey(site)) return null;

		this.deleteCount++;
		this.lastDeleted=site;
		// than remove the finished construction site;
		ArrayList<String> tobuild=construction.remove(site);
		StringBuilder sb= new StringBuilder();
		for(String s: tobuild)
		{
			sb.append(s);
		}
		return sb.toString();
	}


	/**
	 * Is a chromosome switch required
	 * @return
	 */
	public boolean requireChromosomeSwitch(){  return this.requireChromosomeSwitch;}

	/**
	 * Switch to next refererence chromosome
	 * return id of next chromosome
	 * @return
	 */
	public String switchChromosome()
	{
		SamPair sp=this.nextPair();
		String chr=sp.getFirstRead().getRefchr();

		while(chr.equals(this.activeChr))
		{
			sp=this.nextPair();
			if(sp==null) throw new IllegalArgumentException("End of file reached while searching for next reference chromosome");
			chr=sp.getFirstRead().getRefchr();
		}

		this.construction=new HashMap<Integer,ArrayList<String>>();
		this.constructionDoneUntil=-1;
		this.requireChromosomeSwitch=false;
		this.activeChr=chr;
		this.bufferPair(sp);
		return chr;


	}

	public String getActiveChr()
	{return this.activeChr;}


	/**
	 * this ppileup builder is done
	 * no more reads, no more unprinted sites
	 * @return
	 */
	public boolean eof()
	{
		        return this.eof;
	}


}




