package pt2.subsample;

import corete.data.ppileup.PpileupSiteLightwight;
import corete.io.ppileup.PpileupLightwightReader;
import corete.io.ppileup.PpileupSubsampleLightwightReader;
import corete.io.ppileup.PpileupWriter;
import pt2.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 8/31/15.
 */
public class SubsampleFramework {
	private final String inputFile;
	private final String outputFile;
	private final int targetCoverage;
	private final boolean zippedOutput;
	private final Logger logger;
	public SubsampleFramework(String inputFile, String outputFile, int targetCoverage, boolean zippedOutput, Logger logger)
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


		this.targetCoverage = targetCoverage;
		if(this.targetCoverage<1) throw new IllegalArgumentException("Target coverage can not be smaller than one");
		this.zippedOutput=zippedOutput;
		this.logger=logger;
	}

	public void run()
	{
		this.logger.info("Start sub-sampling coverage in ppileup file to "+this.targetCoverage);
		PpileupSubsampleLightwightReader plsr=new PpileupSubsampleLightwightReader(new PpileupLightwightReader(this.inputFile,this.logger),this.targetCoverage,this.logger);
		PpileupWriter writer=new PpileupWriter(this.outputFile,this.zippedOutput,plsr.getTEFamilyShortcutTranslator(),plsr.getEssentialPpileupStats(),this.logger);
		PpileupSiteLightwight s=null;
		while((s=plsr.next())!=null)
		{
			writer.writeEntry(s);
		}

		this.logger.info("Done - thank you for using PoPoolation TE2 ("+ Main.getVersionNumber()+")");


	}


}
