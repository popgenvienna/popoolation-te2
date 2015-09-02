package corete.io.ppileup;

import corete.data.TEFamilyShortcutTranslator;
import corete.data.ppileup.PpileupSampleSummary;
import corete.data.ppileup.PpileupSite;
import corete.data.ppileup.PpileupSiteLightwight;
import corete.data.ppileup.PpileupSymbols;
import corete.data.stat.EssentialPpileupStats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 9/2/15.
 */
public class PpileupReader {
	private final IPpileupLightwightReader lwr;
	private Logger logger;
	public PpileupReader(String inputFile, Logger logger)
	{
		this.lwr=new PpileupLightwightReader(inputFile,logger);
	}


	public EssentialPpileupStats getEssentialPpileupStats()
	{
		return lwr.getEssentialPpileupStats();
	}

	public TEFamilyShortcutTranslator getTEFamilyShortcutTranslator()
	{
		return lwr.getTEFamilyShortcutTranslator();
	}

	public PpileupSite next()
	{
		PpileupSiteLightwight lw=lwr.next();
		ArrayList<PpileupSampleSummary> sum=new ArrayList<PpileupSampleSummary>();

		for(int i=0; i<lw.size(); i++)
		{
			ArrayList<String> sa=lw.getEntries(i);
			int coverage=sa.size();
			int properpair=0;
			int srfwd=0;
			int srrev=0;
			HashMap<String,Integer> tecount=new HashMap<String,Integer>();
			for(String s:sa)
			{
				switch(s)
				{
					case PpileupSymbols.ABS:
						properpair++;
						break;
					case PpileupSymbols.SvFWD:
						srfwd++;
						break;
					case PpileupSymbols.SvRev:
						srrev++;
						break;
					default:
						tecount.putIfAbsent(s,0);
						tecount.put(s,1+tecount.get(s));
				}  // swithc
			}               //for
			PpileupSampleSummary ss=new PpileupSampleSummary(properpair,srfwd,srrev,coverage,tecount);
			sum.add(ss);

		}
		return new PpileupSite(lw.getChromosome(),lw.getPosition(),lw.getComment(),sum);
	}





}
