package corete.data.ppileup;

import corete.data.SamPair;
import corete.data.SamPairType;
import corete.data.TEFamilyShortcutTranslator;
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
	private final int maxDistancePair;
	private final SamPairReader reader;
	private final TEFamilyShortcutTranslator translator;

	// mutable - used for working
	// specific for each chromosomes
	private  HashMap<Integer,ArrayList<String>> construction;
	private int constructionDoneUntil;
	private String activeChr=null;
	private boolean requireChromosomeSwitch;


	// general whole thing
	private boolean eof;

	public PpileupBuilder(int minMapQual, int averagePairDistance, int maxDistancePair, SamPairReader reader, TEFamilyShortcutTranslator translator)
	{
		// immutable
		this.minMapQual=minMapQual;
		this.averagePairDistance=averagePairDistance;
		this.maxDistancePair=maxDistancePair;
		this.reader=reader;
		this.translator=translator;

		this.eof=false;
	}




	//region read Buffer - SamPair
	private SamPair bufferPair=null;
	private void bufferPair(SamPair pair)
	{
		assert(this.bufferPair!=null);
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
		this.constructionDoneUntil=sp.getFirstRead().getStart()-this.averagePairDistance+1;

		// ADD THE READ
		if(sp.getSamPairType()== SamPairType.Pair)
		   {
			   if(sp.isProperPair(this.minMapQual) && sp.getInnerDistance()<=this.maxDistancePair)
			   {
				   // ok proper pair, and not exceeding max distance than => construct
				   addFromTo(sp.getFirstRead().getEnd()+1,sp.getSecondRead().getStart()-1,PpileupSymbols.ABS);
				   return true;

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

	private void  addBrokenPair(SamPair brokenPair)
	{
		String symbol;
		int start;
		int end;
		if(brokenPair.getFirstRead().isForwardStrand())
		{
			symbol=PpileupSymbols.SvFWD;
			start=brokenPair.getFirstRead().getEnd()+1;
			end=brokenPair.getFirstRead().getEnd()+this.averagePairDistance-1;
		}
		else
		{
			symbol=PpileupSymbols.SvRev;
			start=brokenPair.getFirstRead().getStart()-this.averagePairDistance+1;
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
			end=tepair.getFirstRead().getEnd()+this.averagePairDistance-1;
		}
		else
		{
			symbol=this.translator.getShortcutRev(fam);
			start=tepair.getFirstRead().getStart()-this.averagePairDistance+1;
			end=tepair.getFirstRead().getStart()-1;
		}

		if(symbol.length()>1)symbol=PpileupSymbols.TEstart+symbol+PpileupSymbols.TEend;
		addFromTo(start,end,symbol);
	}



	private void addFromTo(int start, int end, String symbol)
	{
		assert(symbol!=null);
		// test if at least one base will be added    start== end is allowed
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




