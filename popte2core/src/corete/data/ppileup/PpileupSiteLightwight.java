package corete.data.ppileup;

import java.util.ArrayList;

/**
 * Created by robertkofler on 8/27/15.
 */
public class PpileupSiteLightwight {
	private final String chromosome;
	private final int position;
	private final ArrayList<ArrayList<String>> popentries;

	public PpileupSiteLightwight(String chromosome, int position, ArrayList<ArrayList<String>> popentries)
	{
		this.chromosome=chromosome;
		this.position=position;
		this.popentries=new ArrayList<ArrayList<String>>(popentries);
	}

}
