package corete.io.ppileup;

import corete.data.SignatureDirection;
import corete.data.TEFamilyShortcutTranslator;
import corete.data.ppileup.*;
import corete.data.stat.EssentialPpileupStats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 9/2/15.
 */
public class PpileupReader implements IPpileupReader {
	private final IPpileupLightwightReader lwr;
	private Logger logger;
	public PpileupReader(String inputFile, Logger logger)
	{
		this.lwr=new PpileupLightwightReader(inputFile,logger);
	}


	@Override
	public EssentialPpileupStats getEssentialPpileupStats()
	{
		return lwr.getEssentialPpileupStats();
	}

	@Override
	public TEFamilyShortcutTranslator getTEFamilyShortcutTranslator()
	{
		return lwr.getTEFamilyShortcutTranslator();
	}

	@Override
	public PpileupSite next()
	{
		PpileupSiteLightwight lw=lwr.next();
		if(lw==null) return null;
		ArrayList<PpileupSampleSummary> sum=new ArrayList<PpileupSampleSummary>();

		for(int i=0; i<lw.size(); i++)
		{
			// special case; emtpy entry; lightwight reader returns empyt ArrayList
			// treated properly; zero counts for all
			ArrayList<String> sa=lw.getEntries(i);

			int absfwd=0;
			int absrev=0;
			int srfwd=0;
			int srrev=0;
			HashMap<String,Integer> tecountfwd=new HashMap<String,Integer>();
			HashMap<String,Integer> tecountrev=new HashMap<String,Integer>();
			for(String s:sa)
			{
				switch(s)
				{
					case PpileupSymbols.ABS:
						absfwd++;
						absrev++;
						break;
					case PpileupSymbols.ABSFWD:
						absfwd++;
						break;
					case PpileupSymbols.ABSREV:
						absrev++;
						break;
					case PpileupSymbols.SvFWD:
						srfwd++;
						break;
					case PpileupSymbols.SvRev:
						srrev++;
						break;
					default:
						if(s.toLowerCase().equals(s))
						{
							tecountrev.putIfAbsent(s,0);
							tecountrev.put(s,1+tecountrev.get(s));
						}
						else if(s.toUpperCase().equals(s))
						{
							tecountfwd.putIfAbsent(s,0);
							tecountfwd.put(s,1+tecountfwd.get(s));
						}

				}  // swithc
			}               //for
			PpileupDirectionalSampleSummary fwd=new PpileupDirectionalSampleSummary(SignatureDirection.Forward,absfwd,srfwd,tecountfwd);
			PpileupDirectionalSampleSummary rev=new PpileupDirectionalSampleSummary(SignatureDirection.Reverse,absrev,srrev,tecountrev);
			PpileupSampleSummary ss=new PpileupSampleSummary(fwd,rev);
			sum.add(ss);
		}
		return new PpileupSite(lw.getChromosome(),lw.getPosition(),lw.getComment(),sum);
	}





}
