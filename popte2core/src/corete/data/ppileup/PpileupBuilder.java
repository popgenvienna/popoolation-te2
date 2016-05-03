package corete.data.ppileup;

import corete.data.SamPair;
import corete.data.SamPairType;
import corete.data.SamRecord;
import corete.data.TEFamilyShortcutTranslator;
import corete.io.ISamPairReader;
import corete.io.SamPairReader;

import java.util.ArrayList;
import java.util.HashMap;

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

	// mutable - used for working
	// specific for each chromosomes
	private  HashMap<Integer,ArrayList<String>> construction;
	private int constructionDoneUntil;
	private String activeChr=null;
	private boolean requireChromosomeSwitch;
	private boolean extendClipped;


	// general whole thing
	private boolean eof;




	public PpileupBuilder(int minMapQual, int averagePairDistance, ISamPairReader reader, TEFamilyShortcutTranslator translator, boolean extendClipped)
	{
		this(minMapQual,averagePairDistance,reader,translator);
		this.extendClipped=extendClipped;
	}


	public PpileupBuilder(int minMapQual, int averagePairDistance, ISamPairReader reader, TEFamilyShortcutTranslator translator)
	{
		// immutable
		this.minMapQual=minMapQual;
		this.averagePairDistance=averagePairDistance;
		this.reader=reader;
		this.translator=translator;
		this.constructionDoneUntil=-1;

		this.eof=false;
		this.extendClipped=false;
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
			if(sp==null){this.eof=true; return false;}

			// is a chromosome switch necessary
			if(!sp.getFirstRead().getRefchr().equals(activeChr))
			{
				this.requireChromosomeSwitch=true;    // set the flag that a chromosome switch is necessary
				this.bufferPair(sp);
				return false;
			}


		// construction done until what?
		this.constructionDoneUntil= getStart(sp.getFirstRead())-this.averagePairDistance;

		// ADD THE READ
		if(sp.getSamPairType()== SamPairType.Pair)
		   {
			   if(sp.isProperPair(this.minMapQual))
			   {
				   if( sp.getInnerDistance()<=2*this.averagePairDistance) {
					   // ok proper pair, and not exceeding max distance than => construct
					   addFromTo(getEnd(sp.getFirstRead()) + 1, getStart(sp.getSecondRead()) - 1, PpileupSymbols.ABS);
					   return true;
				   }else
				   {
					   addFromTo(getEnd(sp.getFirstRead())+1, getEnd(sp.getFirstRead())+averagePairDistance, PpileupSymbols.ABS);
					   addFromTo(getStart(sp.getSecondRead()) - averagePairDistance, getStart(sp.getSecondRead()) - 1, PpileupSymbols.ABS);

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


	private int getStart(SamRecord sr)
	{
		if(extendClipped)
		{
		   return sr.getStartWithS();
		}
		else
		{
			return sr.getStart();
		}

	}

	private int getEnd(SamRecord sr)
	{
		if(extendClipped)
		{
			return sr.getEndWithS();
		}
		else
		{
			return sr.getEnd();
		}
	}





	private void  addBrokenPair(SamPair brokenPair)
	{
		String symbol;
		int start;
		int end;
		if(brokenPair.getFirstRead().isForwardStrand())
		{
			symbol=PpileupSymbols.SvFWD;
			start=getEnd(brokenPair.getFirstRead())+1;
			end=getEnd(brokenPair.getFirstRead())+this.averagePairDistance;
		}
		else
		{
			symbol=PpileupSymbols.SvRev;
			start=getStart(brokenPair.getFirstRead())-this.averagePairDistance;
			end=getStart(brokenPair.getFirstRead())-1;
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

			start=getEnd(tepair.getFirstRead())+1;
			end=getEnd(tepair.getFirstRead())+this.averagePairDistance;
		}
		else
		{
			symbol=this.translator.getShortcutRev(fam);
			start=getStart(tepair.getFirstRead())-this.averagePairDistance;
			end=getStart(tepair.getFirstRead())-1;
		}

		if(symbol.length()>1)symbol=PpileupSymbols.TEstart+symbol+PpileupSymbols.TEend;
		addFromTo(start,end,symbol);
	}



	private void addFromTo(int start, int end, String symbol)
	{
		assert(symbol!=null);
		// test if at least one base will be added    start== end is allowed
		if(start<1) start=1;
		if(end-start<0) return;


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
		// first deal with empty sites
		if(!construction.containsKey(site)) return null;

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




