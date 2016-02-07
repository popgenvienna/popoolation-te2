package corete.io.misc;

import corete.data.SamPair;
import corete.data.SamPairType;
import corete.data.hier.TEHierarchy;
import corete.data.stat.*;
import corete.io.SamPairReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 8/18/15.
 */
public class PpileupHelpStatReader {
	private ArrayList<String> inputFiles;
	private Logger logger;
	private int mapQual;
	private TEabundanceContainer abundance=null;
	private InsertSizeDistributionContainer distribution=null;
	private RefChrSortingContainer rcsc=null;
	private InformativeReadCountContainer ircc=null;
	private int srmd;
	private final TEHierarchy hier;
	private LastPositionContainer lpc=null;


	public PpileupHelpStatReader(ArrayList<String> inputFiles, TEHierarchy hier, int mapQual, int srmd, Logger logger)
	{
		this.inputFiles=new ArrayList<String>(inputFiles);
		this.logger=logger;
		this.mapQual=mapQual;
		this.srmd=srmd;
		this.hier=hier;
	}


	/**
	 * Read the statisicts, both insert size and TE abundance
	 */
	private void readStats()
	{
		ArrayList<TEabundance> abundances = new ArrayList<TEabundance>();
		ArrayList<InsertSizeDistribution> distributions = new ArrayList<InsertSizeDistribution>();
		ArrayList<ArrayList<String>> refchrsortings=new ArrayList<ArrayList<String>>();
		ArrayList<HashMap<String,Integer>> lastPositions=new ArrayList<HashMap<String,Integer>>();
		ArrayList<Integer> informativeReads=new ArrayList<Integer>();

		for(String file: this.inputFiles)
		{
			AISDContainer stat=getStatistics(file);

			abundances.add(stat.abundance);
			distributions.add(stat.distribution);
			refchrsortings.add(stat.rcs);
			lastPositions.add(stat.lastPos);
			informativeReads.add(stat.informativeReadCount);
		}
		this.ircc=new InformativeReadCountContainer(informativeReads);
		this.abundance=new TEabundanceContainer(abundances);
		this.distribution=new InsertSizeDistributionContainer(distributions);
		this.rcsc=new RefChrSortingContainer(refchrsortings);
		this.lpc=new LastPositionContainer(lastPositions);
		this.logger.info("Finished estimating insert size distribution, TE abundance and sorting of reference chromosomes");
	}

	/**
	 * Get the TE abundance for all populations
	 * @return
	 */
	public TEabundanceContainer getTEabundance()
	{
		if(abundance==null) readStats();
		return abundance;
	}


	/**
	 * Get Informative read counts for all populations
	 * @return
	 */
	public InformativeReadCountContainer getInformativeReadCountContainer()
	{
		if(ircc==null) readStats();
		return ircc;
	}


	/**
	 * Get the insert size distribution for all populations
	 * @return
	 */
	public InsertSizeDistributionContainer getInsertSizeDistribution()
	{
		if(distribution==null) readStats();
		return distribution;
	}


	/**
	 * Get the sorting of reference chromosomes
	 * @return
	 */
	public RefChrSortingContainer getRefChrSorting()
	{
		if(rcsc==null) readStats();
		return rcsc;
	}


	/**
	 * Get the last position container
	 * @return
	 */
	public LastPositionContainer getLastPositionContainer()
	{
		if(this.lpc==null) readStats();
		return lpc;
	}





	/**
	 * Get the Insert Size distribution, TE abundance and sorting of reference chromosomes for a single given file
	 * @param file
	 * @return
	 */
	private AISDContainer getStatistics(String file)
	{
		this.logger.info("Estimating insert size distribution, TE abundance, sorting of reference chromosomes and size of chromosomes for file "+file);
		SamPairReader spr=new SamPairReader(file,hier,srmd,this.logger);

		HashMap<String,Integer> famcount=new HashMap<String,Integer>();
		HashMap<Integer,Integer> distcount=new HashMap<Integer,Integer>();
		HashSet<String> pastrcs=new HashSet<String>();
		ArrayList<String> rcs=new ArrayList<String>();

		HashMap<String,Integer> lastEnd=new HashMap<String,Integer>();

		int informativeReadCount=0;



		while(spr.hasNext())
		{
			SamPair sp =spr.next();
			String chr=null;
			int pos=-1;

			informativeReadCount++; // sam pair reader will only return TEInserts, Pairs and BrokenPairs


			if(sp.getSamPairType() == SamPairType.TEInsert)
			{
				// consider mapqual as well
				if(sp.getFirstRead().getMapq()>=this.mapQual) {
					String fam = sp.getFamily();
					famcount.putIfAbsent(fam, 0);
					famcount.put(fam, famcount.get(fam) + 1);

					// refchrsorting
					chr=sp.getFirstRead().getRefchr();
					pos=sp.getFirstRead().getEnd();

				}
			}
			else if(sp.getSamPairType()== SamPairType.Pair)
			{
				if(sp.isProperPair(this.mapQual))
				{
				   int distance= sp.getInnerDistance();
					distcount.putIfAbsent(distance, 0);
					distcount.put(distance, distcount.get(distance) + 1);

					// refchrsorting
					chr=sp.getFirstRead().getRefchr();
					pos=sp.getSecondRead().getEnd();
				}
			}

			// process reference chromosomes sorting
			if(chr!=null)
			{
				    if(!pastrcs.contains(chr))
					{
						rcs.add(chr);
						pastrcs.add(chr);
						lastEnd.put(chr,-1);
					}
					if(pos > lastEnd.get(chr)) lastEnd.put(chr,pos);
			}
		}

		InsertSizeDistribution isd=new InsertSizeDistribution(distcount);
		TEabundance tea=new TEabundance(famcount);
		return new AISDContainer(tea,isd,rcs,lastEnd,informativeReadCount);
	}


	/**
	 * helper class...allows to return three parameters in a function
	 */
	private class AISDContainer
	{
		public AISDContainer(TEabundance abundance, InsertSizeDistribution distribution, ArrayList<String> rcs,HashMap<String,Integer> lastPos,int informativeReadCount)
		{
			this.abundance=abundance;
			this.distribution=distribution;
			this.rcs=rcs;
			this.lastPos=lastPos;
			this.informativeReadCount=informativeReadCount;
		}

		public int informativeReadCount;
		public TEabundance abundance;
		public InsertSizeDistribution distribution;
		public ArrayList<String> rcs;
		public HashMap<String,Integer> lastPos;
	}






}
