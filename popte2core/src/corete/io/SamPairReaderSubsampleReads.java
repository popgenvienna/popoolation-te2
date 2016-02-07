package corete.io;

import corete.data.SamPair;

import java.security.InvalidParameterException;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 2/7/16.
 */
public class SamPairReaderSubsampleReads implements ISamPairReader {
	private SamPair next;
	private final ISamPairReader spr;
	private  int remainingInFile;
	private int remainingToRet;



	public SamPairReaderSubsampleReads(ISamPairReader spr, int toRead, int content)
	{
		this.spr=spr;
		this.remainingInFile=content;
		this.remainingToRet=toRead;


		if(toRead>content)throw new IllegalArgumentException("Can not read more than file content "+toRead+" "+content);

		this.next=	getNext();


	}




	public boolean hasNext()
	{
		if(this.next==null){return false;}
		else{return true;}
	}

	public SamPair next()
	{
		if(!this.hasNext()){throw new InvalidParameterException("there is no further SamPair; use hasNext() first"); }
		SamPair toret=this.next;
		this.next=getNext();
		return toret;
	}


	private SamPair getNext()
	{

		while(this.spr.hasNext())
		{

			SamPair test=spr.next();
			double ratio=(double)remainingToRet/(double)remainingInFile;
			double rand=Math.random();
			if(rand<ratio)
			{
				remainingInFile--;
				remainingToRet--;
				return test;
			}
			else remainingInFile--;
		}

	return null;
	}


}
