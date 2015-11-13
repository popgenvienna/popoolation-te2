package corete.data.ppileup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by robertkofler on 9/2/15.
 */
public class PpileupChunk {
	private final ArrayList<PpileupSite> sites;
	private final int startPosition;
	private final int endPosition;
	private final String chromosome;

	public PpileupChunk(ArrayList<PpileupSite> sites)
	{
		this.sites=new ArrayList<PpileupSite>(sites);
		if(sites.size()<1) throw new IllegalArgumentException("No chromosomal chunks of zero size are allowed");

		int start=sites.get(0).getPosition();
		int end=sites.get(sites.size()-1).getPosition();

		String chromo=null;
		for(PpileupSite ps:this.sites)
		{
			if(chromo==null) chromo=ps.getChromosome();
			if(ps.getPosition()<start) throw new IllegalArgumentException("Chunk is unsorted...sort your ppileup file by chromosome and position!");
			if(ps.getPosition()>end) throw new IllegalArgumentException("Chunk is unsorted...sort your ppileup file by chromosome and position!");
			if(!ps.getChromosome().equals(chromo)) throw new IllegalArgumentException("Chunk must not contain entries from different chromosomes");
		}
		this.startPosition=start;
		this.endPosition=end;
		this.chromosome=chromo;

	}


	public int getStartPosition(){return this.startPosition;}
	public int getEndPosition(){return this.endPosition;}
	public String getChromosome(){return this.chromosome;}


	/**
	 * get the number of samples/populations
	 * @return
	 */
	public int size(){return this.sites.get(0).size();}

	/**
	 * Get the number of ppileup entries in the chunk
	 * @return
	 */
	public int siteCount(){return this.sites.size();}

	/**
	 * get only PSS (PpileupSummaryStatistic) for the population with the given index
	 * the track will created for every site in the chunk; if absent in a population a
	 * default PSS with all values zero will be used
	 * @param popindex
	 * @return
	 */
	public HashMap<Integer,PpileupSampleSummary> getPSSTrack(int popindex)
	{
		HashMap<Integer,PpileupSampleSummary> toret=new HashMap<Integer,PpileupSampleSummary>();

		for(PpileupSite s:this.sites){
			toret.put(s.getPosition(),s.getPpileupSampleSummary(popindex));
		}

		for(int i=this.getStartPosition(); i<=this.getEndPosition(); i++)
		{
			  if(!toret.containsKey(i)) toret.put(i,PpileupSampleSummary.getEmpty());
		}
		return toret;
	}


	public HashMap<Integer,PpileupSampleSummary> getPooledTrack()
	{
		HashMap<Integer,PpileupSampleSummary> toret=new HashMap<Integer,PpileupSampleSummary>();

		for(PpileupSite s:this.sites){
			toret.put(s.getPosition(),new PpileupPoolsampleFactory(s).getPooledsample().getPpileupSampleSummary(0));
		}

		for(int i=this.getStartPosition(); i<=this.getEndPosition(); i++)
		{
			if(!toret.containsKey(i)) toret.put(i,PpileupSampleSummary.getEmpty());
		}
		return toret;
	}

	/**
	 * Get the shortcuts of all TEs in the chunk;
	 * @return a unique list of all TE shortcuts in the chromosomal chunk
	 */
	public ArrayList<String> getShortcutsOfTEsInChunk()
	{
		HashSet<String> tes=new HashSet<String>();
		for(PpileupSite ps:this.sites)
		{
			tes.addAll(ps.getTEComplement());
		}
		return new ArrayList<String>(tes);
	}



}
