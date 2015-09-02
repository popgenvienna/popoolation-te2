package pt2.identifysignatures;

import corete.io.ppileup.IPpileupLightwightReader;
import corete.io.ppileup.PpileupChunkReader;
import corete.io.ppileup.PpileupLightwightReader;
import pt2.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 8/31/15.
 */
public class IdentifySignatureFramework {
	private final String inputFile;
	private final String outputFile;
	private final SignatureIdentificationMode mode;
	private final int mincoverage;
	private final int mincount;
	private final int maxrearangements;
	private final int maxothertes;
	private final Integer fixedinsertsize;
	private final int chunkmultiplicator;
	private final boolean detailedLog;
	private final Logger logger;

	public IdentifySignatureFramework(String inputFile, String outputFile, SignatureIdentificationMode mode, int mincoverage, int mincount,
									  int maxrearangements, int maxothertes, Integer fixedinsertsize, int chunkmultiplicator,
									  boolean detailedlog, Logger logger)
	{
		this.inputFile=inputFile;
		if(!new File(inputFile).exists()) throw new IllegalArgumentException("Input file does not exist: "+inputFile);
		this.outputFile=outputFile;
		try{
			new FileWriter(outputFile);
		}
		catch(IOException e)
		{
			throw new IllegalArgumentException("Can not create output file:" +outputFile);
		}
		this.mode=mode;
		this.mincoverage=mincoverage;
		if(mincoverage<1) throw new IllegalArgumentException("Minimum coverage must be larger than zero");
		this.mincount=mincount;
		if(mincount<1) throw new IllegalArgumentException("Minimum count must be larger than zero");
		this.maxrearangements=maxrearangements;
		this.maxothertes=maxothertes;
		this.fixedinsertsize=fixedinsertsize;
		this.chunkmultiplicator=chunkmultiplicator;
		this.detailedLog=detailedlog;
		this.logger=logger;
	}

	public void run()
	{
		this.logger.info("Starting identification of TE signatures");
		IPpileupLightwightReader lwr=new PpileupLightwightReader(this.inputFile,this.logger);
		int chunkdistance=lwr.getEssentialPpileupStats().getMaximumInnerDistance()*this.chunkmultiplicator;
		if(this.fixedinsertsize!=null) chunkdistance=chunkmultiplicator*fixedinsertsize;
		PpileupChunkReader chunkReader=new PpileupChunkReader(lwr,chunkdistance,logger);


		this.logger.info("Done - thank you for using PoPoolation TE2 ("+ Main.getVersionNumber()+")");


	}


}
