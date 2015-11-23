package corete.io.ppileup;

import corete.data.TEFamilyShortcutTranslator;
import corete.data.ppileup.DirectionalPpileupSiteLightwight;
import corete.data.ppileup.PpileupSiteLightwight;
import corete.data.ppileup.PpileupSymbols;
import corete.data.stat.EssentialPpileupStats;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 8/31/15.
 */
public class DirectionalPpileupLightwightReader {

	private final PpileupLightwightReader lwr;
	private final Logger logger;

	public DirectionalPpileupLightwightReader(PpileupLightwightReader lwr, Logger logger)
	{
		this.lwr=lwr;
		this.logger=logger;
	}


	public DirectionalPpileupSiteLightwight next()
	{
		PpileupSiteLightwight plr=null;


		while((plr=lwr.next())!=null)
		{
			ArrayList<ArrayList<String>> fwd=new ArrayList<ArrayList<String>>();
			ArrayList<ArrayList<String>> rev=new ArrayList<ArrayList<String>>();
			for(int i=0; i<plr.size(); i++)
			{
				ArrayList<String> tofwd=new ArrayList<String>();
				ArrayList<String> torev=new ArrayList<String>();
				for(String s:plr.getEntries(i))
				{
					switch(s) {
						case PpileupSymbols.ABS:
							tofwd.add(PpileupSymbols.ABSFWD);
							torev.add(PpileupSymbols.ABSREV);
							break;
						case PpileupSymbols.ABSFWD:
							tofwd.add(s);
							break;
						case PpileupSymbols.ABSREV:
							torev.add(s);
							break;
						case PpileupSymbols.SvFWD:
							tofwd.add(s);
							break;
						case PpileupSymbols.SvRev:
							torev.add(s);
							break;
						default:
							if (s.toLowerCase().equals(s)) {
								torev.add(s);
							} else if (s.toUpperCase().equals(s)) {
								tofwd.add(s);
							}
					}
				}
				fwd.add(tofwd);
				rev.add(torev);
			}
			return new DirectionalPpileupSiteLightwight(plr.getChromosome(),plr.getPosition(),plr.getComment(),fwd,rev);
		}
		return null;


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
				int index=(int)(Math.random() * (double)ts.size());
				String sampled=ts.remove(index);
				sample.add(sampled);
			}
			newEntries.add(sample);
		}
		PpileupSiteLightwight toret=new PpileupSiteLightwight(plr.getChromosome(),plr.getPosition(),plr.getComment(),newEntries);
		return toret;

	}






}
