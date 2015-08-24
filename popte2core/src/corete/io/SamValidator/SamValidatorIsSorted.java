package corete.io.SamValidator;

import corete.data.SamRecord;

/**
 * Created by robertkofler on 8/17/15.
 */
public class SamValidatorIsSorted implements ISamValidator {
	private String lastRef=null;
	private int highestPosition=-1;

	public SamValidatorIsSorted()
	{

	}



	@Override
	public boolean isValid(SamRecord record) {
		if(lastRef==null){lastRef=record.getRefchr();}
		if(!lastRef.equals(record.getRefchr())){
			lastRef=record.getRefchr();
			highestPosition=-1;
		}

		if(record.getStart()<highestPosition) {
			return false;
		}
		else
		{
			highestPosition=record.getStart();
			return true;
		}
	}

	@Override
	public String errorMessage(SamRecord record)
	{
		  return "unsorted sam/bam file detected - "+record.shortID();
	}



}
