package corete.data.ppileup;

import corete.data.SignatureDirection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by robertkofler on 11/23/15.
 */
public class PpileupDirectionalSampleSummary {
	private final int absence;
	private final SignatureDirection direction;
	private final int sr;
	private final HashMap<String, Integer> tecount;
	public PpileupDirectionalSampleSummary(SignatureDirection direction, int absence, int sr, HashMap<String,Integer> tecount)
	{
		if(direction !=SignatureDirection.Forward && direction!=SignatureDirection.Reverse) throw new IllegalArgumentException("Invalid direction "+direction);
		this.absence=absence;
		this.direction=direction;
		this.sr=sr;
		this.tecount=new HashMap<String,Integer>(tecount);
	}

	public int getAbsence(){return this.absence;}
	public SignatureDirection getSignatureDirection(){return this.direction;}
	public int getStructuralRearrangement(){return this.sr;}

	public HashMap<String,Integer> getTecount(){return new HashMap<String,Integer>(this.tecount);}
	public int getCoverage(){
		int cov=this.sr+this.absence;
		for(Map.Entry<String,Integer> me:this.tecount.entrySet())
		{
			cov+=me.getValue();
		}
		return cov;
	}

	public static PpileupDirectionalSampleSummary getEmpty(SignatureDirection direction)
	{
		HashMap<String,Integer> hm=new HashMap<String,Integer>();
		return new PpileupDirectionalSampleSummary(direction,0,0,hm);
	}


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


	public HashSet<String> getTEComplement()
	{
		return new HashSet<String>(this.tecount.keySet());
	}


}
