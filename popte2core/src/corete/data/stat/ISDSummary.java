package corete.data.stat;

import java.util.ArrayList;

/**
 * Created by robertkofler on 8/19/15.
 * Insert size distribution summary
 * Summary of mean,median, upper quantile (value specified by user) for all populations/samples
 *
 */
public class ISDSummary {
	private final ArrayList<Integer> median;
	private final ArrayList<Integer> mean;
	private final ArrayList<Integer> upperQuantil;
	private final float fractionUpperQuantil;
	private final int popcount;


	public ISDSummary(ArrayList<Integer> mean, ArrayList<Integer> median, ArrayList<Integer> upperQuantil, float fractionUpperQuantil)
	{
		this.mean=new ArrayList<Integer>(mean);
		this.median=new ArrayList<Integer>(median);
		this.upperQuantil=new ArrayList<Integer>(upperQuantil);
		this.popcount = mean.size();
		if(median.size()!=popcount) throw new IllegalArgumentException("all insert size distribution estimates must have same number of populations");
		if(upperQuantil.size()!=popcount) throw new IllegalArgumentException("all insert size distribution estimates must have same number of populations");
		this.fractionUpperQuantil=fractionUpperQuantil;
	}


	/**
	 * get the upper quantil for a given population
	 * @param popindex
	 * @return
	 */
	public int getUpperQuantil(int popindex)
	{
		return this.upperQuantil.get(popindex);
	}

	public int getMedian(int popindex)
	{
		return this.median.get(popindex);
	}

	public ArrayList<Integer> getMedians()
	{
		return new ArrayList<Integer>(this.median);
	}


	public int getMean(int popindex)
	{
		return this.mean.get(popindex);
	}




}
