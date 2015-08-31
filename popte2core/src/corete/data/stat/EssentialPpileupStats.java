package corete.data.stat;

import java.util.ArrayList;

/**
 * Created by robertkofler on 8/27/15.
 */
public class EssentialPpileupStats {
	private final ArrayList<Integer> defaultInnerDistances;
	private final int srmd;
	private final String version;
	private final int minMapQual;
	public EssentialPpileupStats(ArrayList<Integer> defaultInnerDistances,int minMapQual, int srmd, String version)
	{
		this.defaultInnerDistances=new ArrayList<Integer>(defaultInnerDistances);
		this.minMapQual=minMapQual;
		this.srmd=srmd;
		this.version=version;
	}

	public ArrayList<Integer>  getDefaultInnerDistances()
	{
		return new ArrayList<Integer>(this.defaultInnerDistances);
	}

	public int getDefaultInnerDistance(int index)
	{
		return this.defaultInnerDistances.get(index);
	}

	public int countSamples()
	{
		return this.defaultInnerDistances.size();
	}



	public int getStructuralRearrangementMinimumDistance()
	{
		return this.srmd;
	}

	public String getVersionNumber()
	{
		return this.version;
	}

	public int getMinMapQual()
	{
		return this.minMapQual;
	}







}
