package corete.data.tesignature;

import java.util.ArrayList;

/**
 * Created by robertkofler on 10/22/15.
 */
public class SignatureFiltering {

	/**
	 * Filter for minimum count; at least one sample should have a count larger than this
	 * @param signatures
	 * @param minCount
	 * @return
	 */
	public static ArrayList<InsertionSignature>  filterMinCount(ArrayList<InsertionSignature> signatures, double minCount)
	{
		ArrayList<InsertionSignature> toret=new ArrayList<InsertionSignature>();
		for(InsertionSignature s:signatures)
		{
			boolean valid=false;
			ArrayList<FrequencySampleSummary> fss=s.getFrequencies();
			for(FrequencySampleSummary f:fss)
			{
				if(f.getGivenTEInsertion()>=minCount) valid=true;
			}
			if(valid) toret.add(s);
		}
		return toret;
	}


	/**
	 * Filter for minimum coverage
	 * @param signatures
	 * @param minCoverage
	 * @return
	 */
	public static ArrayList<InsertionSignature>  filterMinCoverage(ArrayList<InsertionSignature> signatures, double minCoverage)
	{
		ArrayList<InsertionSignature> toret=new ArrayList<InsertionSignature>();
		for(InsertionSignature s:signatures)
		{
			boolean valid=true;
			ArrayList<FrequencySampleSummary> fss=s.getFrequencies();
			for(FrequencySampleSummary f:fss)
			{
				if(f.getCoverage()<minCoverage) valid=false;
			}
			if(valid) toret.add(s);
		}
		return toret;
	}


	/**
	 * Get maximum coverage
	 * @param signatures
	 * @param maxCoverage
	 * @return
	 */
	public static ArrayList<InsertionSignature>  filterMaxCoverage(ArrayList<InsertionSignature> signatures, Double maxCoverage)
	{
		if(maxCoverage==null) return signatures;
		ArrayList<InsertionSignature> toret=new ArrayList<InsertionSignature>();
		for(InsertionSignature s:signatures)
		{
			boolean valid=true;
			ArrayList<FrequencySampleSummary> fss=s.getFrequencies();
			for(FrequencySampleSummary f:fss)
			{
				if(f.getCoverage()>maxCoverage) valid=false;
			}
			if(valid) toret.add(s);
		}
		return toret;
	}


	/**
	 * filter for a maximum count of other TE insertions
	 * @param signatures
	 * @param maxOthertecount
	 * @return
	 */
	public static ArrayList<InsertionSignature>  filterMaxOtherteCount(ArrayList<InsertionSignature> signatures, Double maxOthertecount)
	{
		if(maxOthertecount==null) return signatures;
		ArrayList<InsertionSignature> toret=new ArrayList<InsertionSignature>();
		for(InsertionSignature s:signatures)
		{
			boolean valid=true;
			ArrayList<FrequencySampleSummary> fss=s.getFrequencies();
			for(FrequencySampleSummary f:fss)
			{
				if(f.getOtherTEinsertions()>maxOthertecount) valid=false;
			}
			if(valid) toret.add(s);
		}
		return toret;
	}


	/**
	 * Filter for a maximum count of structural variants
	 * @param signatures
	 * @param maxStructvarcount
	 * @return
	 */
	public static ArrayList<InsertionSignature>  filterMaxStructvarCount(ArrayList<InsertionSignature> signatures, Double maxStructvarcount)
	{
		if(maxStructvarcount==null) return signatures;
		ArrayList<InsertionSignature> toret=new ArrayList<InsertionSignature>();
		for(InsertionSignature s:signatures)
		{
			boolean valid=true;
			ArrayList<FrequencySampleSummary> fss=s.getFrequencies();
			for(FrequencySampleSummary f:fss)
			{
				if(f.getStructuralRearrangements()>maxStructvarcount) valid=false;
			}
			if(valid) toret.add(s);
		}
		return toret;
	}


	/**
	 * Filter for a minimum population frequency
	 * @param signatures
	 * @param minFrequency
	 * @return
	 */
	public static ArrayList<InsertionSignature>  filterMinFrequency(ArrayList<InsertionSignature> signatures, double minFrequency)
	{
		ArrayList<InsertionSignature> toret=new ArrayList<InsertionSignature>();
		for(InsertionSignature s:signatures)
		{
			boolean valid=false;
			ArrayList<FrequencySampleSummary> fss=s.getFrequencies();
			for(FrequencySampleSummary f:fss)
			{
				if(f.getPopulationFrequency()>=minFrequency) valid=true;
			}
			if(valid) toret.add(s);
		}
		return toret;
	}


	/**
	 *
	 * @param signatures
	 * @param maxOtherteFrequency
	 * @return
	 */
	public static ArrayList<InsertionSignature>  filterMaxOtherteFrequency(ArrayList<InsertionSignature> signatures, double maxOtherteFrequency)
	{
		ArrayList<InsertionSignature> toret=new ArrayList<InsertionSignature>();
		for(InsertionSignature s:signatures)
		{
			boolean valid=true;
			ArrayList<FrequencySampleSummary> fss=s.getFrequencies();
			for(FrequencySampleSummary f:fss)
			{
				if(f.getOtherteFrequency() > maxOtherteFrequency) valid=false;
			}
			if(valid) toret.add(s);
		}
		return toret;
	}

	/**
	 *
	 * @param signatures
	 * @param maxStructvarFrequency
	 * @return
	 */
	public static ArrayList<InsertionSignature>  filterMaxStructvarFrequency(ArrayList<InsertionSignature> signatures, double maxStructvarFrequency)
	{
		ArrayList<InsertionSignature> toret=new ArrayList<InsertionSignature>();
		for(InsertionSignature s:signatures)
		{
			boolean valid=true;
			ArrayList<FrequencySampleSummary> fss=s.getFrequencies();
			for(FrequencySampleSummary f:fss)
			{
				if(f.getStructvarFrequency() > maxStructvarFrequency) valid=false;
			}
			if(valid) toret.add(s);
		}
		return toret;
	}




}
