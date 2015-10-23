package pt2.filtersignatures;

import corete.data.tesignature.InsertionSignature;
import corete.data.tesignature.SignatureFiltering;
import corete.data.tesignature.SignatureFrequencyEstimationFramework;
import corete.io.ppileup.PpileupReader;
import corete.io.tesignature.TESignatureReader;
import corete.io.tesignature.TESignatureWriter;
import pt2.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 8/31/15.
 */
public class FilterSignatureFramework {

	private final String signatureFile;
	private final String outputFile;
	private final double mincoverage;
	private final Double maxcoverage;

	private final double minsupport;
	private final Double maxotherte;
	private final Double maxstructvar;

	private final double minsupportfraction;
	private final double maxothertefraction;
	private final double maxstructvarfraction;



	private final boolean detailedLog;
	private final Logger logger;


	public FilterSignatureFramework(String signatureFile, String outputFile,  double mincoverage,
									Double maxcoverage, double minsupport, Double maxotherte, Double maxstructvar, double minsupportfraction,
									double maxothertefraction, double maxstructvarfraction, boolean detailedlog, Logger logger)
	{
		this.signatureFile=signatureFile;
		if(!new File(signatureFile).exists()) throw new IllegalArgumentException("Input file does not exist: "+signatureFile);
		this.outputFile=outputFile;
		try{
			new FileWriter(outputFile);
		}
		catch(IOException e)
		{
			throw new IllegalArgumentException("Can not create output file:" +outputFile);
		}
		this.mincoverage=mincoverage;
		this.maxcoverage=maxcoverage;
		this.minsupport=minsupport;
		this.maxotherte=maxotherte;
		this.maxstructvar = maxstructvar;
		this.minsupportfraction=minsupportfraction;
		this.maxothertefraction=maxothertefraction;
		this.maxstructvarfraction=maxstructvarfraction;




		this.detailedLog=detailedlog;
		this.logger=logger;
	}

	public void run()
	{
		this.logger.info("Start filtering signatures of TE insertions");
		ArrayList<InsertionSignature> signatures= TESignatureReader.readall(signatureFile, logger);

		// min coverage
		String text="Filtered for min. coverage; before filtering "+signatures.size()+ "; after filtering ";
		signatures= SignatureFiltering.filterMinCoverage(signatures,this.mincoverage);
		text+=signatures.size();
		this.logger.info(text);

		// max coverage
		text="Filtered for max. coverage; before filtering "+signatures.size()+ "; after filtering ";
		signatures= SignatureFiltering.filterMaxCoverage(signatures,this.maxcoverage);
		text+=signatures.size();
		this.logger.info(text);

		// min count
		text="Filtered for min. count; before filtering "+signatures.size()+ "; after filtering ";
		signatures= SignatureFiltering.filterMinCount(signatures,this.minsupport);
		text+=signatures.size();
		this.logger.info(text);

		// max other te count
		text="Filtered for max. count of other TEs; before filtering "+signatures.size()+ "; after filtering ";
		signatures= SignatureFiltering.filterMaxOtherteCount(signatures,this.maxotherte);
		text+=signatures.size();
		this.logger.info(text);

		// max structural variants count
		text="Filtered for max. count of structural rearrangements; before filtering "+signatures.size()+ "; after filtering ";
		signatures= SignatureFiltering.filterMaxStructvarCount(signatures,this.maxstructvar);
		text+=signatures.size();
		this.logger.info(text);

		// min frequency
		text="Filtered for min. population frequency; before filtering "+signatures.size()+ "; after filtering ";
		signatures= SignatureFiltering.filterMinFrequency(signatures,this.minsupportfraction);
		text+=signatures.size();
		this.logger.info(text);

		// max other te frequency
		text="Filtered for max. frequency of other TE insertions; before filtering "+signatures.size()+ "; after filtering ";
		signatures= SignatureFiltering.filterMaxOtherteFrequency(signatures,this.maxothertefraction);
		text+=signatures.size();
		this.logger.info(text);

		// max structvar frequency
		text="Filtered for max. frequency of structural rearrangements; before filtering "+signatures.size()+ "; after filtering ";
		signatures= SignatureFiltering.filterMaxStructvarFrequency(signatures,this.maxstructvarfraction);
		text+=signatures.size();
		this.logger.info(text);


		TESignatureWriter.writeall(this.outputFile,signatures,logger);

		this.logger.info("Done - thank you for using PoPoolation TE2 ("+ Main.getVersionNumber()+")");
	}







}
