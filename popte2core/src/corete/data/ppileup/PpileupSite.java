package corete.data.ppileup;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by robertkofler on 9/2/15.
 */
public class PpileupSite {
	private final String chromosome;
	private final int position;
	private final String comment;
	private final ArrayList<PpileupSampleSummary> samples;

	public PpileupSite(String chromosome, int position, String comment, ArrayList<PpileupSampleSummary> samples )
	{

		this.chromosome=chromosome;
		this.position=position;
		this.comment=comment;
		this.samples=new ArrayList<PpileupSampleSummary>(samples);
	}

	public String getChromosome(){return this.chromosome;}

	public int getPosition(){return this.position;}

	public String getComment(){return this.comment;}

	public int size(){return this.samples.size();}

	public PpileupSampleSummary getPpileupSampleSummary(int index){
		return this.samples.get(index);
	}

	/**
	 * Get the max TE support for a site; support per sample individually
	 * @return
	 */
	public int getMaxTESupport_SampleSpecific()
	{
		int maxsupport=0;
		for(PpileupSampleSummary ps:this.samples)
		{
			if(ps.maxTESupport()>maxsupport)maxsupport=ps.maxTESupport();
		}
		return maxsupport;
	}

	/**
	 * Get the full complement of TE insertions at a given position
	 */
	public HashSet<String> getTEComplement()
	{
		HashSet<String> tes=new HashSet<String>();
		for(PpileupSampleSummary ps:this.samples)
		{
			tes.addAll(ps.getTEComplement());
		}
		return tes;
	}



}
