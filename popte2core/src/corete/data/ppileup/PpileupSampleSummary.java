package corete.data.ppileup;

import sun.util.resources.cldr.zh.CalendarData_zh_Hans_HK;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by robertkofler on 9/2/15.
 */
public class PpileupSampleSummary {
	private final int absence;
	private final int srfwd;
	private final int srrev;
	private final HashMap<String, Integer> tecount;
	private final int coverage;

	public PpileupSampleSummary(int absence, int srfwd, int srrev, int coverage, HashMap<String, Integer> tecount)
	{
		this.absence = absence;
		this.srrev = srrev;
		this.srfwd = srfwd;
		this.tecount = new HashMap<String, Integer>(tecount);
		this.coverage=coverage;
	}

	public int getCoverage()
	{
		 return this.coverage;
	}

	public int getCountAbsence(){return this.absence;}

	public int getCountSrFwd(){return this.srfwd;}

	public int getCountSrRev(){return this.srrev;}

	public HashMap<String,Integer> getTECount(){return new HashMap<String,Integer>(this.tecount);}


	public int maxTESupport()
	{
		int maxsupport=0;
		for(Map.Entry<String,Integer> e:this.tecount.entrySet())
		{
			if(e.getValue()>maxsupport)maxsupport=e.getValue();
		}
		return maxsupport;
	}


	/**
	 * Get a unique complement of all TEs present at the given site
	 * @return
	 */
	public HashSet<String> getTEComplement()
	{
		  return new HashSet<String>(this.tecount.keySet());
	}




}
