package corete.data.ppileup;

import java.util.ArrayList;

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

}
