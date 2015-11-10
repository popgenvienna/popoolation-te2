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
	 *  if end<start the distance is negative
	 * @return
	 */
	public int getDistance(int start,int end)
	{

		int distance=0;

		if(end>=start) {

			for (int i = start; i < end; i++) {
				if (!this.pnr.contains(i)) distance++;
			}
		}
		else
		{
			for(int i=end; i<start; i++)
			{
				if(!this.pnr.contains(i)) distance--;
			}
		}
		return distance;
	}






	public boolean distanceIsOutsideBoundary(int start,int end,int minDistance, int maxDistance)
	{

		int absmaxdistance= Math.max(Math.abs(maxDistance),Math.abs(minDistance));
		int distance=0;

		if(end>=start) {

			for (int i = start; i < end; i++) {
				if (!this.pnr.contains(i)) distance++;
			}
			if(distance>absmaxdistance) return false;
		}
		else
		{
			for(int i=end; i<start; i++)
			{
				if(!this.pnr.contains(i)) distance--;
			}
			if(Math.abs(distance)>absmaxdistance) return false;
		}

		if(distance < minDistance || distance >maxDistance) return true;
		else return false;
	}



}
