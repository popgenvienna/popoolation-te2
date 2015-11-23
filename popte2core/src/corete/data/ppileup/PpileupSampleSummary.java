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
	PpileupDirectionalSampleSummary forward;
	PpileupDirectionalSampleSummary reverse;

	public PpileupSampleSummary(PpileupDirectionalSampleSummary forward, PpileupDirectionalSampleSummary reverse)
	{
		this.forward=forward;
		this.reverse=reverse;

	}

	public static PpileupSampleSummary getEmpty()
	{

		return new PpileupSampleSummary(PpileupDirectionalSampleSummary.getEmpty(SignatureDirection.Forward),PpileupDirectionalSampleSummary.getEmpty(SignatureDirection.Reverse));
	}

	public PpileupDirectionalSampleSummary getForward(){return this.forward;}
	public PpileupDirectionalSampleSummary getReverse(){return this.reverse;}


	public PpileupDirectionalSampleSummary getStrandSpecificSampleSummary(SignatureDirection dir)
	{
		if(dir==SignatureDirection.Forward) return forward;
		else if(dir==SignatureDirection.Reverse) return reverse;
		else throw new IllegalArgumentException("Invalid direction "+dir);

	}

	public int getTEcount(String teshortcut)
	{
		int fwc=this.forward.getTEcount(teshortcut);
		int rec=this.reverse.getTEcount(teshortcut);
		if(fwc>rec)return fwc;
		else return rec;
	}



	public int maxTESupport()
	{
		int fmax=this.forward.maxTESupport();
		int rmax=this.reverse.maxTESupport();
		if(fmax>=rmax) return fmax;
		else return rmax;
	}


	/**
	 * Get a unique complement of all TEs present at the given site
	 * @return
	 */
	public HashSet<String> getTEComplement()
	{
		HashSet<String> toret=new HashSet<String>();
		toret.addAll(this.forward.getTEComplement());
		toret.addAll(this.reverse.getTEComplement());
		return toret;
	}




}
