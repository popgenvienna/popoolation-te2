package corete.data.ppileup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by robertkofler on 9/3/15.
 */
public class PpileupPoolsampleFactory {
	 private final PpileupSite site;
	public PpileupPoolsampleFactory(PpileupSite site)
	{
		this.site=site;
	}



	public PpileupSite getPooledsample()
	{
		int sumabsence=0;
		int sumsrfwd=0;
		int sumsrrev=0;
		int sumcoverage=0;
		HashMap<String,Integer> tecount=new HashMap<String,Integer>();
		for(int i=0; i<site.size();i++)
		{
			PpileupSampleSummary ps=this.site.getPpileupSampleSummary(i);
			sumabsence+=ps.getCountAbsence();
			sumsrfwd+=ps.getCountSrFwd();
			sumsrrev+=ps.getCountSrRev();
			sumcoverage+=ps.getCoverage();
			for(Map.Entry<String,Integer> e: ps.getAllTEcounts().entrySet())
			{
				String sc=e.getKey();
			 	tecount.putIfAbsent(sc,0);
				tecount.put(sc,tecount.get(sc)+e.getValue());
			}

		}
		ArrayList<PpileupSampleSummary> singlesample=new ArrayList<PpileupSampleSummary>();
		singlesample.add(new PpileupSampleSummary(sumabsence,sumsrfwd,sumsrrev,tecount));
		return new PpileupSite(site.getChromosome(),site.getPosition(),site.getComment(),singlesample);
	}

}
