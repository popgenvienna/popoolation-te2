package corete.data.tesignature;

import corete.data.ppileup.PpileupSampleSummary;
import corete.data.ppileup.PpileupSite;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by robertkofler on 10/21/15.
 */
public class SignatureFrequencyBuilder {
	private final InsertionSignature signature;
	private final String teshortcut;
	private ArrayList<PpileupSite> sites;
	public SignatureFrequencyBuilder(InsertionSignature signature, String teshortcut)
	{
		this.signature=signature;
		this.teshortcut=teshortcut;
	}



	public void addSite(PpileupSite site)
	{
		this.sites.add(site);
	}


	public InsertionSignature getUpdatedSignature()
	{
		ArrayList<FrequencySampleSummary> freqs=new ArrayList<FrequencySampleSummary>();
		ArrayList<Integer> ids=signature.getPopid().getIds();
		for(int id:ids)
		{

		}
	}


	private FrequencySampleSummary getFrequencySampleSummaryForID(int id)
	{
		ArrayList<PpileupSampleSummary> ppss=new ArrayList<PpileupSampleSummary>();
		for(PpileupSite s:this.sites){
			ppss.add(s.getPpileupSampleSummary(id));
		}

		double coverage=0.0;
		double givenTE=0.0;
		double otherTE=0.0;
		double structural=0.0;
		for(PpileupSampleSummary pss:ppss)
		{
			coverage+=(double)pss.getCoverage();
			for(Map.Entry<String,Integer> me:pss.getAllTEcounts().entrySet())
			{
				if(me.getKey().equals(this.teshortcut))
				{
					givenTE+=(double)me.getValue();
				}
				else{
					otherTE+=(double)me.getValue();
				}
			}
			structural+=(double)pss.getCountSrFwd();
			structural+=(double)pss.getCountSrRev();
		}
		double size=(double)ppss.size();

		coverage/=size;
		givenTE/=size;
		otherTE/=size;
		structural/=size;
		return new FrequencySampleSummary(coverage,givenTE,otherTE,structural);
	}











}
