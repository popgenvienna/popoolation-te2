package corete.data.ppileup;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by robertkofler on 11/23/15.
 */
public class SubsampleDirectionalPpileupSiteLightwight {

	private final int targetcoverage;
	public SubsampleDirectionalPpileupSiteLightwight(int targetcoverage)
	{
		this.targetcoverage=targetcoverage;
	}


	public DirectionalPpileupSiteLightwight subsample(DirectionalPpileupSiteLightwight tosubsample)
	{

		ArrayList<ArrayList<String>> ssfwd=new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> ssrev=new ArrayList<ArrayList<String>>();

		// FORWARD
		if(tosubsample.getMinimumForwardCoverage() >= targetcoverage)
		{
			for(int i=0; i<tosubsample.size();i++)
			{
				ArrayList<String> tosfwd=tosubsample.getForwardEntries(i);
				if(tosfwd.size()<targetcoverage) throw new IllegalArgumentException("Can not subsample from smaller sample");
				ssfwd.add(sample(tosfwd));
			}
		}
		else for(int i=0; i<tosubsample.size();i++) ssfwd.add(new ArrayList<String>());

		// REVERSE
		if(tosubsample.getMinimumReverseCoverage() >= targetcoverage)
		{
			for(int i=0; i<tosubsample.size();i++)
			{
				ArrayList<String> tosrev=tosubsample.getReverseEntries(i);
				if(tosrev.size()<targetcoverage) throw new IllegalArgumentException("Can not subsample from smaller sample");
				ssrev.add(sample(tosrev));
			}
		}
		else for(int i=0; i<tosubsample.size();i++) ssrev.add(new ArrayList<String>());
		if(ssfwd.size()==0 && ssrev.size()==0) throw new IllegalArgumentException("Invalid coverage");

		return new DirectionalPpileupSiteLightwight(tosubsample.getChromosome(),tosubsample.getPosition(),tosubsample.getComment(),ssfwd,ssrev);
	}

	private ArrayList<String> sample(ArrayList<String> tos)
	{

		LinkedList<String> ts=new LinkedList<String>(tos);
		ArrayList<String> sample=new ArrayList<String>();
		while(sample.size()<this.targetcoverage)
		{
			int index=(int)(Math.random() * (double)ts.size());
			String sampled=ts.remove(index);
			sample.add(sampled);
		}
		return sample;

	}


}
