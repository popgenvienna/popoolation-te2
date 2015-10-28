package corete.data.polyn;

import corete.data.polyn.PolyNRecord;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by robertkofler on 10/23/15.
 */
public class PolyNRecordCollection {

	private final ArrayList<PolyNRecord> polyns;
	public PolyNRecordCollection(ArrayList<PolyNRecord> polyns)
	{
		this.polyns=new ArrayList<PolyNRecord>(polyns);
	}

	public ArrayList<PolyNRecord> getPolyNRecords()
	{
		return new ArrayList<PolyNRecord>(this.polyns);
	}

	public int countNbases()
	{
		int count=0;
		for(PolyNRecord r:polyns)
		{
			count+=r.length();
		}
		return count;
	}

	public int countRecords(){return polyns.size();}

	public int countContigs(){
		HashSet<String> contigs=new HashSet<String>();
		for(PolyNRecord r:polyns)
		{
			contigs.add(r.getChromosome());
		}
		return contigs.size();
	}

	public ContigwisePolynRepresentation getContigwisePolynRepresentation(String contig)
	{
		ArrayList<PolyNRecord> poly=new ArrayList<PolyNRecord>();
		for(PolyNRecord pr:this.getPolyNRecords())
		{
			if(pr.getChromosome().equals(contig)) poly.add(pr);
		}
		return new ContigwisePolynRepresentation(poly,contig);
	}



}
