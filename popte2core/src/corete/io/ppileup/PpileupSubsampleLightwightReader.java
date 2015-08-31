package corete.io.ppileup;

import corete.data.TEFamilyShortcutTranslator;
import corete.data.ppileup.PpileupSiteLightwight;
import corete.data.stat.EssentialPpileupStats;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 8/31/15.
 */
public class PpileupSubsampleLightwightReader implements IPpileupLightwightReader {

	private final int targetCoverage;
	private final PpileupLightwightReader lwr;
	private final Logger logger;
	private int countInsufficientCoverage=0;
	private int countRead=0;

	public PpileupSubsampleLightwightReader(PpileupLightwightReader lwr, int targetCoverage, Logger logger)
	{
		     this.lwr=lwr;
		this.targetCoverage=targetCoverage;
		this.logger=logger;
	}


	public PpileupSiteLightwight next()
	{
		PpileupSiteLightwight plr=null;

		while((plr=lwr.next())!=null)
		{
			countRead++;
			boolean sufficientCoverage=true;
			for(int i=0; i<plr.size(); i++) if(plr.getCoverage(i)<this.targetCoverage) sufficientCoverage=false;
			if(sufficientCoverage) {
				return subsampleToCoverage(plr, this.targetCoverage);
			}
			else countInsufficientCoverage++;
		}

		this.logger.info("Finished subsampling ppileup; Read "+this.countRead+ " entries");
		this.logger.info("Discarded "+this.countInsufficientCoverage + " entries due to insufficient coverage in any of the samples");
		return null;
	}

	public void logSummary()
	{


	}



	private PpileupSiteLightwight subsampleToCoverage(PpileupSiteLightwight plr,int tocoverage)
	{

		ArrayList<ArrayList<String>> newEntries=new ArrayList<ArrayList<String>>();

		for(int i =0; i<plr.size(); i++)
		{
			 LinkedList<String> ts=new LinkedList<String>(plr.getEntries(i));
		    ArrayList<String> sample=new ArrayList<String>();
			while(sample.size()<tocoverage)
			{
				String sampled=ts.remove((int) Math.random() * ts.size());
				sample.add(sampled);
			}
			newEntries.add(sample);
		}
		PpileupSiteLightwight toret=new PpileupSiteLightwight(plr.getChromosome(),plr.getPosition(),plr.getComment(),newEntries);
		return toret;

	}


	@Override
	public TEFamilyShortcutTranslator getTEFamilyShortcutTranslator()
	{
		return this.lwr.getTEFamilyShortcutTranslator();
	}


	@Override
	public EssentialPpileupStats getEssentialPpileupStats()
	{
		return this.lwr.getEssentialPpileupStats();
	}



}
