package corete.data.ppileup;

import javax.print.DocFlavor;
import java.util.ArrayList;

/**
 * Created by robertkofler on 8/27/15.
 */
public class PpileupSiteLightwight {
	private final String chromosome;
	private final int position;
	private final String comment;
	private final ArrayList<ArrayList<String>> popentries;

	public PpileupSiteLightwight(String chromosome, int position, String comment, ArrayList<ArrayList<String>> popentries)
	{
		this.chromosome=chromosome;
		this.position=position;
		this.comment=comment;
		this.popentries=new ArrayList<ArrayList<String>>(popentries);
	}

	public String getChromosome()
	{
		return this.chromosome;
	}
	public int getPosition(){return this.position;}
	public String getComment(){return this.comment;}


	public ArrayList<String> getEntries(int popindex)
	{
		return new ArrayList<String>(this.popentries.get(popindex));
	}

	/**
	 * The number of populations in the sample
	 * @return
	 */
	public int size()
	{
		return this.popentries.size();
	}

	/**
	 * Get the coverage for the popualtion with the given index
	 * @param popindex
	 * @return
	 */
	public int getCoverage(int popindex)
	{
		return this.popentries.get(popindex).size();
	}



}
