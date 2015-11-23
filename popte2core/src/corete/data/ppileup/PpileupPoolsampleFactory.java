package corete.data.ppileup;

import corete.data.SignatureDirection;

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
		int sumabsencefwd=0;
		int sumabsencerev=0;
		int sumsrfwd=0;
		int sumsrrev=0;
		int sumcoverage=0;
		HashMap<String,Integer> tecountfwd=new HashMap<String,Integer>();
		HashMap<String,Integer> tecountrev=new HashMap<String,Integer>();
		for(int i=0; i<site.size();i++)
		{
			PpileupSampleSummary ps=this.site.getPpileupSampleSummary(i);
			sumabsencefwd+=ps.getForward().getAbsence();
			sumabsencerev+=ps.getReverse().getAbsence();
			sumsrfwd+=ps.getForward().getStructuralRearrangement();
			sumsrrev+=ps.getReverse().getStructuralRearrangement();

			for(Map.Entry<String,Integer> e: ps.getForward().getTecount().entrySet())
			{
				String sc=e.getKey();
			 	tecountfwd.putIfAbsent(sc,0);
				tecountfwd.put(sc,tecountfwd.get(sc)+e.getValue());
			}
			for(Map.Entry<String,Integer> e: ps.getReverse().getTecount().entrySet())
			{
				String sc=e.getKey();
				tecountrev.putIfAbsent(sc,0);
				tecountrev.put(sc,tecountrev.get(sc)+e.getValue());
			}


		}
		PpileupDirectionalSampleSummary fw=new PpileupDirectionalSampleSummary(SignatureDirection.Forward,sumabsencefwd,sumsrfwd,tecountfwd);
		PpileupDirectionalSampleSummary re=new PpileupDirectionalSampleSummary(SignatureDirection.Reverse,sumabsencerev,sumsrrev,tecountrev);
		ArrayList<PpileupSampleSummary> singlesample=new ArrayList<PpileupSampleSummary>();
		singlesample.add(new PpileupSampleSummary(fw,re));
		return new PpileupSite(site.getChromosome(),site.getPosition(),site.getComment(),singlesample);
	}

}
