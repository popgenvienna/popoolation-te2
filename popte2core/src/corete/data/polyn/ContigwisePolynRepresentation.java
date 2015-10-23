package corete.data.polyn;



import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by robertkofler on 10/23/15.
 */
public class ContigwisePolynRepresentation {
	private final HashSet<Integer> pnr;
	private final String contig;

	public ContigwisePolynRepresentation(ArrayList<PolyNRecord> polyns, String contig)
	{
		this.contig=contig;
		HashSet<Integer> torep=new HashSet<Integer>();
		if(polyns.size()>0)
		{
	    	for(PolyNRecord p:polyns)
			{
				if(!p.getChromosome().equals(contig)) throw new IllegalArgumentException("Can not build polyn representation for multiple contigs "+p.getChromosome()+" "+contig);
				for(int i=p.getStart(); i<=p.getEnd(); i++)
				{
					torep.add(i);
				}

			}

		}

		pnr=torep;

	}

	public String getContig(){return this.contig;}

	/**
	 * Computes the distance between two genomic positions, ignoring poly-N tracts
	 * pos1 may be smaller, larger or equal than pos2
	 * distance without poly-N is abs(pos1-pos2)
	 * @param pos1
	 * @param pos2
	 * @return
	 */
	public int getDistance(int pos1,int pos2)
	{
		int start=pos1;
		int end=pos2;
		if(start>end)
		{
			start=pos2;
			end=pos1;
		}

		// basic question of start==end -> distance=0
		// start=4; end=5; distance =1
		int distance=0;
		for(int i=start; i<end;i++)
		{
			if(!this.pnr.contains(i)) distance++;
		}
		return distance;

	}



}
