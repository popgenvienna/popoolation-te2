package corete.data.ppileup;

import com.sun.javaws.exceptions.InvalidArgumentException;
import corete.data.SignatureDirection;
import sun.util.resources.cldr.zh.CalendarData_zh_Hans_HK;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Container class
 * Created by robertkofler on 9/2/15.
 */
public class PpileupSampleSummary {
	private final int absence;
	private final int srfwd;
	private final int srrev;
	private final HashMap<String, Integer> tecount;
	private final int coverage;

	public PpileupSampleSummary(int absence, int srfwd, int srrev, HashMap<String, Integer> tecount)
	{
		this.absence = absence;
		this.srrev = srrev;
		this.srfwd = srfwd;
		this.tecount = new HashMap<String, Integer>(tecount);

		// Compute coverage
		// I decided to compute it inside the class ensure sanity of the container
		int coverage=absence+srfwd+srrev;
		for(Map.Entry<String,Integer> me:tecount.entrySet())
		{
			coverage+=me.getValue();
		}
		this.coverage=coverage;
	}

	public static PpileupSampleSummary getEmpty()
	{
		HashMap<String,Integer> hm=new HashMap<String,Integer>();
		return new PpileupSampleSummary(0,0,0,hm);
	}

	/**
	 * Coverage
	 * sum of absence, structural-fwd, structural-rev, and all TEs
	 * @return
	 */
	public int getCoverage()
	{
		 return this.coverage;
	}

	public int getCountAbsence(){return this.absence;}

	public int getCountSrFwd(){return this.srfwd;}

	public int getCountSrRev(){return this.srrev;}

	public HashMap<String,Integer> getAllTEcounts()
	{
		return new HashMap<String,Integer>(this.tecount);
	}


	public PpileupSampleSummary getStrandSpecificSampleSummary(SignatureDirection dir)
	{
		if(dir==SignatureDirection.Forward)
		{
			HashMap<String,Integer> tokeep=new HashMap<String,Integer>();
			for(Map.Entry<String,Integer> me:this.tecount.entrySet())
			{
				if(me.getKey().toUpperCase().equals(me.getKey())) tokeep.put(me.getKey(),me.getValue());
			}

			 return new PpileupSampleSummary(this.absence,this.srfwd,0,tokeep);
		}
		else if(dir==SignatureDirection.Reverse)
		{
				HashMap<String,Integer> tokeep=new HashMap<String,Integer>();
				for(Map.Entry<String,Integer> me:this.tecount.entrySet())
				{
					if(me.getKey().toLowerCase().equals(me.getKey())) tokeep.put(me.getKey(),me.getValue());
				}
			 return new PpileupSampleSummary(this.absence,0,this.srrev,tokeep);
		}
		else throw new IllegalArgumentException("Invalid strand "+dir);

	}



	/**
	 * Get the counts for a specific TE family
	 * @param teshortcut
	 * @return
	 */
	public int getTEcount(String teshortcut)
	{
		return tecount.getOrDefault(teshortcut,0);
	}

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
