package pt2.ppileup;

import corete.data.TEFamilyShortcutTranslator;
import corete.data.hier.TEHierarchy;

import corete.data.ppileup.PpileupMultipopBuilder;
import corete.data.stat.EssentialPpileupStats;
import corete.data.stat.ISDSummary;
import corete.data.stat.RefChrSortingGeneratorSampleConsensus;
import corete.io.*;
import corete.io.misc.PpileupHelpStatReader;
import corete.io.ppileup.PpileupWriter;
import pt2.Main;


import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;
import java.io.File;

/**
 * Created by robertkofler on 6/29/15.
 */
public class PpileupFramework {

	private final Logger logger;
	private final ArrayList<String> inputFiles;
	private final String outputFile;
	private final int minmapqual;
	private final int srmd;
	private final float idof;
	private final String hierFile;
	private final String teshortcuts;
	private final boolean zippedOutput;

	public PpileupFramework(ArrayList<String> inputFiles,String outputFile, String hierFile, int minmapqual,int srmd, float idof, String shortcuts, boolean zippedOutput, Logger logger)
	{
		this.inputFiles=inputFiles;
		this.outputFile=outputFile;
		this.hierFile=hierFile;
		this.minmapqual=minmapqual;
		this.srmd=srmd;
		this.idof=idof;
		this.teshortcuts=shortcuts;
		this.logger=logger;
		this.zippedOutput=zippedOutput;

		// Set
		for(String file: inputFiles)
		{
			if(!new File(file).exists()) throw new IllegalArgumentException("Input file does not exist: "+file);
		}
		try{
			new FileWriter(outputFile);
		}
		catch(IOException e)
		{
			throw new IllegalArgumentException("Can not create output file:" +outputFile);
		}







	}

	public void run()
	{
		// Read the TE hierarchy
		TEHierarchy hier= new TEHierarchyReader(this.hierFile,this.logger).getHierarchy();

		// BASIC required statistics
		PpileupHelpStatReader hsr=new PpileupHelpStatReader(inputFiles,hier,this.minmapqual,this.srmd, this.logger);
		// Short cut translator
		TEFamilyShortcutTranslator sctr=null;
		if(new File(this.teshortcuts).exists()){sctr=new TEFamilyShortcutReader(this.teshortcuts,this.logger).getShortcutTranslator();}
		else{sctr = hsr.getTEabundance().getSCTAbundantShort();}
		// insert size distribution
		ISDSummary isdsum=hsr.getInsertSizeDistribution().getISDSummary(this.idof);
		// Get the Sorting of the reference chromosomes
		ArrayList<String> rcs = new RefChrSortingGeneratorSampleConsensus(hsr.getRefChrSorting(),this.logger).getRefChrConsensusSorting();
		HashMap<String,Integer> lastPositions = hsr.getLastPositionContainer().getLastPosition();


		//Writer
		EssentialPpileupStats estat=new EssentialPpileupStats(isdsum.getMedians(),this.minmapqual,this.srmd,Main.getVersionNumber());
		PpileupWriter writer=new PpileupWriter(this.outputFile,this.zippedOutput,sctr,estat,this.logger);

		PpileupMultipopBuilder ppmpb=new PpileupMultipopBuilder(sctr,estat,this.inputFiles,rcs,lastPositions,hier,writer,logger);
		ppmpb.buildPpileup();

		this.logger.info("Done - thank you for using PoPoolation TE2 ("+ Main.getVersionNumber()+")");






	}

}
