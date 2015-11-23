package corete.data.ppileup;

import java.util.ArrayList;

/**
 * Created by robertkofler on 8/27/15.
 */
public class DirectionalPpileupSiteLightwight {
	private final String chromosome;
	private final int position;
	private final String comment;
	private final ArrayList<ArrayList<String>> forwardEntries;
	private final ArrayList<ArrayList<String>> reverseEntries;

	public DirectionalPpileupSiteLightwight(String chromosome, int position, String comment, ArrayList<ArrayList<String>> forwardEntries,  ArrayList<ArrayList<String>> reverseEntries)
	{
		this.chromosome=chromosome;
		this.position=position;
		this.comment=comment;
		this.forwardEntries=new ArrayList<ArrayList<String>>(forwardEntries);
		this.reverseEntries=new ArrayList<ArrayList<String>>(reverseEntries);
		if(forwardEntries.size() != reverseEntries.size())throw new IllegalArgumentException("Invalid argument");
	}

	public String getChromosome()
	{
		return this.chromosome;
	}
	public int getPosition(){return this.position;}
	public String getComment(){return this.comment;}


	public ArrayList<String> getForwardEntries(int popindex)
	{
		return new ArrayList<String>(this.forwardEntries.get(popindex));
	}

	public ArrayList<String> getReverseEntries(int popindex)
	{
		return new ArrayList<String>(this.reverseEntries.get(popindex));
	}

	/**
	 * The number of populations in the sample
	 * @return
	 */
	public int size()
	{
		return this.forwardEntries.size();
	}

	/**
	 * Get the forward coverage for the population with the given index
	 * @param popindex
	 * @return
	 */
	public int getForwardCoverage(int popindex)
	{
		return this.forwardEntries.get(popindex).size();
	}


	/**
	 * Get the minimum forward coverage across all samples
	 * @return
	 */
	public int getMinimumForwardCoverage()
	{
		int toret=-1;
		for(ArrayList<String> als: this.forwardEntries)
		{
			if(toret==-1) toret=als.size();
			if(als.size()<toret) toret=als.size();
		}
		return toret;
	}

	/**
	 * Get the minimum reverse coverage across all samples
	 * @return
	 */
	public int getMinimumReverseCoverage()
	{
		int toret=-1;
		for(ArrayList<String> als: this.reverseEntries)
		{
			if(toret==-1) toret=als.size();
			if(als.size()<toret) toret=als.size();
		}
		return toret;
	}




	/**
	 * Get the forward coverage for the population with the given index
	 * @param popindex
	 * @return
	 */
	public int getReverseCoverage(int popindex)
	{
		return this.forwardEntries.get(popindex).size();
	}



}
