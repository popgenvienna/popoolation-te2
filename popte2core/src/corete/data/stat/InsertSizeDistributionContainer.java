package corete.data.stat;

import java.util.ArrayList;

/**
 * Created by robertkofler on 8/18/15.
 */
public class InsertSizeDistributionContainer {
	private final ArrayList<InsertSizeDistribution> distributions;

	public InsertSizeDistributionContainer(ArrayList<InsertSizeDistribution> distributions)
	{
		this.distributions=new ArrayList<InsertSizeDistribution>(distributions);
	}


	public ISDSummary getISDSummary(float fractionUpperQuantile)
	{
		ArrayList<Integer> mean=new ArrayList<Integer>();
		ArrayList<Integer> median=new ArrayList<Integer>();
		ArrayList<Integer> upperQuantile=new ArrayList<Integer>();
		for(InsertSizeDistribution d:distributions)
		{
			mean.add(d.getMean());
			median.add(d.getMedian());
			upperQuantile.add(d.getUpperQuantile(fractionUpperQuantile));
		}

		return new ISDSummary(mean,median,upperQuantile,fractionUpperQuantile);

	}




}
