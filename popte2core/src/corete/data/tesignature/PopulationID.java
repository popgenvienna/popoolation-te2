package corete.data.tesignature;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by robertkofler on 10/22/15.
 */
public class PopulationID implements Comparable<PopulationID> {
	private final ArrayList<Integer> ids;

	public PopulationID(ArrayList<Integer> populations)
	{
		if(populations.size()==0) throw new IllegalArgumentException("size of population ID's size must at least be one");
		boolean issorted=isSorted(populations);
		if(!issorted)throw new IllegalArgumentException("Invalid population IDs; must be sorted");
		ids=new ArrayList<Integer>(populations);

	}


	private boolean isSorted(ArrayList<Integer> pops)
	{
		if(pops.size()==1) return true;
		for(int i = 1; i < pops.size(); i++) {
			if(pops.get(i-1) > pops.get(i)) return false;
		}
		return true;
	}


	/***
	 * Test if the PopulationID contains the population with the given number
	 * @param popSample
	 * @return
	 */
	public boolean containsPopulationSample(int popSample)
	{
		return this.ids.contains(popSample);
	}




	public ArrayList<Integer> getIds()
	{
		return new ArrayList<Integer>(this.ids);
	}


	@Override
	public int compareTo(PopulationID pids)
	{

		ArrayList<Integer> b=pids.getIds();
		if(ids.size()<b.size()) return -1;
		if(ids.size()>b.size()) return 1;

		for(int i=0; i<this.ids.size(); i++)
		{
			int ide=this.ids.get(i);
			int be=b.get(i);
			if(ide<be) return -1;
			if(ide>be) return 1;
		}

		return 0;
	}

	@Override
	public int hashCode()
	{
		// hash: chromosome, strand, position, family
		return this.ids.hashCode();

	}

	@Override
	public boolean equals(Object o)
	{


		// hash: chromosome, strand, position, family
		if(!(o instanceof PopulationID)){return false;}
		PopulationID is=(PopulationID)o;
		if(this.ids.equals(is.getIds())) return true;
		else return false;

	}









}
