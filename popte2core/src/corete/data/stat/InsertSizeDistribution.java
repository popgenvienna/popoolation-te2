package corete.data.stat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by robertkofler on 8/18/15.
 */
public class InsertSizeDistribution {
	// key=insert size; value=count
	private final HashMap<Integer,Integer> distcount;
	private final ArrayList<Integer> sortedKeys;
	private final int sumInsertsizes;
	private final int sumFragmentCount;

	public InsertSizeDistribution(HashMap<Integer,Integer> distcount)
	{
		this.distcount=new HashMap<Integer,Integer>(distcount);
		this.sortedKeys=new ArrayList<Integer>(distcount.keySet());

		int  si=0;  //sum insert
		int sf=0;   //sum fragment
		for(Map.Entry<Integer,Integer> m:distcount.entrySet())
		{
		   	sf+=m.getValue();
			int tmp=m.getKey()*m.getValue();
			si+=tmp;
		}
		this.sumFragmentCount=sf;
		this.sumInsertsizes=si;

	}

	public int getMean()
	{
		return (int)(((float)sumInsertsizes)/((float)sumFragmentCount));


	}
	public int getMedian()
	{
		Collections.sort(sortedKeys);
		int half=sumFragmentCount/2;
		int runsum=0;
		for(int inssize: sortedKeys)
		{
			  runsum+=distcount.get(inssize);
			if(runsum>half) return inssize;

		}

		throw new IllegalArgumentException("No median found");
	}

	public int getUpperQuantile(float fraction)
	{
		Collections.reverse(sortedKeys);
		int targetCount=(int)(fraction*sumFragmentCount);

		int runsum=0;
		for(int inssize:sortedKeys)
		{
			runsum+=distcount.get(inssize);
			if(runsum>targetCount) return inssize;
		}
		throw new IllegalArgumentException("No percentile found");
	}






}
