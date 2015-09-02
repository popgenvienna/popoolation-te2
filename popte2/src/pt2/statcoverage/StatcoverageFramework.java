package pt2.statcoverage;

import corete.data.ppileup.PpileupSiteLightwight;
import corete.data.stat.PpileupCoverageStatBuilder;
import corete.io.ppileup.PpileupLightwightReader;
import corete.io.ppileup.PpileupSubsampleLightwightReader;
import corete.io.ppileup.PpileupWriter;
import corete.io.stat.PpileupCoverageStatWriter;
import pt2.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 8/31/15.
 */
public class StatcoverageFramework {
	private final String inputFile;
	private final String outputFile;
	private final Logger logger;
	public StatcoverageFramework(String inputFile, String outputFile, Logger logger)
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

		this.logger=logger;
	}

	public void run()
	{
		this.logger.info("Will computer coverage statistics for file  "+this.inputFile);

		PpileupCoverageStatBuilder builder=new PpileupCoverageStatBuilder(new PpileupLightwightReader(this.inputFile,this.logger),this.logger);
		this.logger.info("Writing coverage statistics to file "+this.outputFile);
		PpileupCoverageStatWriter writer=new PpileupCoverageStatWriter(builder,outputFile);
		writer.writeStats();

		this.logger.info("Done - thank you for using PoPoolation TE2 ("+ Main.getVersionNumber()+")");


	}


}
