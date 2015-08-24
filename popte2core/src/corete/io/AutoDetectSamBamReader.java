package corete.io;

import corete.data.SamRecord;
import corete.io.SamValidator.ISamValidator;
import corete.io.SamValidator.SamValidatorAllValid;

import java.security.InvalidParameterException;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 8/17/15.
 */
public class AutoDetectSamBamReader implements ISamBamReader {
	private ISamBamReader sbr;



	public AutoDetectSamBamReader(String inputFile,Logger logger){this(inputFile,logger,new SamValidatorAllValid());}

	public AutoDetectSamBamReader(String inputFile,Logger logger,ISamValidator validator)
	{
		logger.info("Auto detecting sam or bam file by extension");
		if(inputFile.endsWith(".bam"))
		{
			logger.fine("Detected .bam file " + inputFile);
			sbr=new BamReader(inputFile,logger,validator);
		}
		else if(inputFile.endsWith(".sam"))
		{
			logger.fine("Detected .sam file");
			sbr=new SamReader(inputFile,logger,validator);
		}
		else
		{
			throw new InvalidParameterException("File " +inputFile + " is neither .bam nor .sam");
		}
	}



	public boolean hasNext() {
		return sbr.hasNext();
	}


	public SamRecord next() {
		SamRecord sr =sbr.next();

		return sr;
	}
}
