package corete.io;

import corete.data.SamPair;
import corete.data.SamRecord;
import corete.data.hier.TEHierarchy;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 8/17/15.
 */
public class SamPairReader {
	private final int srmd;
	private final Logger logger;
	private final ISamBamReader sbr;
	private final String inputFile;
	private final TEHierarchy tehier;
	private SamPair next=null;

	public SamPairReader(String inputFile, TEHierarchy tehier, int srmd, Logger logger)
	{
		this.logger=logger;
		this.tehier=tehier;
		this.inputFile=inputFile;
		this.srmd=srmd;
		this.sbr=new AutoDetectSamBamReader(inputFile,logger);

		this.next=getNext();
	}


	public boolean hasNext()
	{
		if(this.next==null){return false;}
		else{return true;}
	}

	public SamPair next()
	{
		if(!this.hasNext()){throw new InvalidParameterException("there is no further SamPair; use hasNext() first");
		SamPair toret=this.next;
		this.next=getNext();
		return toret;
	}



	private SamPair getNext()
	{

		HashMap<String,SamRecord> paarship=new HashMap<String,SamRecord>();
		// read from file
		while(sbr.hasNext())
		{
			SamRecord sr=sbr.next();
			String name=sr.getReadname();
			// kick out TE reads
			if(tehier.containsRefid(name)){continue;}




		}




	}
		return null;
	}




}
