package corete.io.tesignature;

import corete.data.tesignature.FrequencySampleSummary;


/**
 * Created by robertkofler on 1/21/16.
 */
public class FrequencySampleSummaryFormater {

	public static String formatHighDetail(FrequencySampleSummary fss)
	{

		String toret=String.format("%d:%.3f:%.3f:%.3f:%.3f",fss.getPopulationid()+1,fss.getCoverage(),fss.getGivenTEInsertion(),fss.getOtherTEinsertions(),fss.getStructuralRearrangements());
		return toret;
	}


	public static String jointFormatMediumDetail(FrequencySampleSummary forward, FrequencySampleSummary reverse)
	{
		double presence=0;
		double total=0;
		int counter=0;
		if(forward!=null)
		{
		 	presence+=forward.getGivenTEInsertion();
			total+=forward.getCoverage();
			counter++;
		}
		if(reverse!=null)
		{
			presence+=reverse.getGivenTEInsertion();
			total+=reverse.getCoverage();
			counter++;
		}
		presence/=((double)counter);
		total/=((double)counter);

		String toret=String.format("%.3f/%.3f",presence,total);
		return toret;
	}

	public static String jointFormatHighDetail(FrequencySampleSummary forward, FrequencySampleSummary reverse)
	{
		int popid=0;                                             // plus one
		String fwdFormat;
		String revFormat;

		if(forward!=null)
		{
			popid=forward.getPopulationid();
			fwdFormat=String.format("%.3f:%.3f:%.3f:%.3f",forward.getCoverage(),forward.getGivenTEInsertion(),forward.getOtherTEinsertions(),forward.getStructuralRearrangements());
		}
		else fwdFormat="-";

		if(reverse!=null)
		{
			popid=reverse.getPopulationid();
			revFormat=String.format("%.3f:%.3f:%.3f:%.3f",reverse.getCoverage(),reverse.getGivenTEInsertion(),reverse.getOtherTEinsertions(),reverse.getStructuralRearrangements());
		}
		else revFormat="-";



		String toret=String.format("%d/%s/%s",popid+1,fwdFormat,revFormat);
		return toret;
	}


}
