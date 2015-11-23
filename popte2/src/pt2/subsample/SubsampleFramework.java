package pt2.subsample;

import corete.data.ppileup.DirectionalPpileupSiteLightwight;
import corete.data.ppileup.SubsampleDirectionalPpileupSiteLightwight;
import corete.io.ppileup.DirectionalPpileupLightwightReader;
import corete.io.ppileup.PpileupLightwightReader;
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
		PpileupLightwightReader plr=new PpileupLightwightReader(this.inputFile,this.logger);
		DirectionalPpileupLightwightReader dr=new DirectionalPpileupLightwightReader(plr,this.logger);
		SubsampleDirectionalPpileupSiteLightwight subsframework=new SubsampleDirectionalPpileupSiteLightwight(this.targetCoverage);
		PpileupWriter writer=new PpileupWriter(this.outputFile,this.zippedOutput,plr.getTEFamilyShortcutTranslator(),plr.getEssentialPpileupStats(),this.logger);

		DirectionalPpileupSiteLightwight s=null;
		while((s=dr.next())!=null)
		{
			int minfwd=s.getMinimumForwardCoverage();
			int minrev=s.getMinimumReverseCoverage();
			// Ignore a site only if both the fwd and the rev coverage are below the threshold
			if(minfwd<this.targetCoverage && minrev< this.targetCoverage) continue;
			DirectionalPpileupSiteLightwight subsampled=subsframework.subsample(s);

			writer.writeEntry(subsampled);
		}

		this.logger.info("Done - thank you for using PoPoolation TE2 ("+ Main.getVersionNumber()+")");


	}


}
