package pt2.ppileup;

import corete.data.TEFamilyShortcutTranslator;
import corete.data.hier.TEHierarchy;

import corete.data.ppileup.PpileupBuilder;
import corete.data.ppileup.PpileupMultipopBuilder;
import corete.data.stat.EssentialPpileupStats;
import corete.data.stat.ISDSummary;
import corete.data.stat.InformativeReadCountContainer;
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
	private final boolean homogenizeReads;
	private final boolean extendClipped;

	public PpileupFramework(ArrayList<String> inputFiles,String outputFile, String hierFile, int minmapqual,int srmd, float idof, String shortcuts,
							boolean homogenizeReads,boolean extendClipped, boolean zippedOutput, Logger logger)
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
		this.homogenizeReads=homogenizeReads;
		this.extendClipped=extendClipped;

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
		// Get the Sorting of the reference chromosomes  ignoring TE sequences
		ArrayList<String> rcs = new RefChrSortingGeneratorSampleConsensus(hsr.getRefChrSorting(),hier, this.logger).getRefChrConsensusSorting();
		HashMap<String,Integer> lastPositions = hsr.getLastPositionContainer().getLastPosition();


		//Writer
		EssentialPpileupStats estat=new EssentialPpileupStats(isdsum.getMedians(),this.minmapqual,this.srmd, this.idof, Main.getVersionNumber());
		PpileupWriter writer=new PpileupWriter(this.outputFile,this.zippedOutput,sctr,estat,this.logger);

		ArrayList<ISamPairReader> sampairreaders=getSampairReaders(this.inputFiles,estat,hier, hsr.getInformativeReadCountContainer());
		PpileupMultipopBuilder ppmpb=new PpileupMultipopBuilder(sctr,estat,srmd,sampairreaders,rcs,lastPositions,hier,extendClipped,writer,logger);
		ppmpb.buildPpileup();

		this.logger.info("Done - thank you for using PoPoolation TE2 ("+ Main.getVersionNumber()+")");



	}

	public ArrayList<ISamPairReader> getSampairReaders(ArrayList<String> inputFiles, EssentialPpileupStats estats, TEHierarchy hier, InformativeReadCountContainer ircc)
	{
		 ArrayList<ISamPairReader> toret     =new ArrayList<ISamPairReader>();


		for(int i=0; i<this.inputFiles.size(); i++)
		{
			String fileName=this.inputFiles.get(i);

			ISamPairReader spr=new SamPairReader(fileName,hier,estats.getStructuralRearrangementMinimumDistance(),logger);
			if(this.homogenizeReads) {
			    this.logger.info("Will homogenize reads of fiel "+fileName);
				this.logger.info("File contains informative read pairs: "+ircc.getInformativeReadCount(i)+"; will subsample to: "+ircc.minInformativeReadCount());
					spr = new SamPairReaderSubsampleReads(spr, ircc.minInformativeReadCount(), ircc.getInformativeReadCount(i));
			}
			toret.add(spr);
		}
		return toret;
	}


}
