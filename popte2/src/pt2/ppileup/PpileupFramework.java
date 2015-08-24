package pt2.ppileup;

import corete.data.SamRecord;
import corete.data.TEFamilyShortcutTranslator;
import corete.data.hier.TEHierarchy;

import corete.data.stat.ISDSummary;
import corete.data.stat.RefChrSortingGeneratorSampleConsensus;
import corete.io.AutoDetectSamBamReader;
import corete.io.BamReader;
import corete.io.ISamBamReader;
import corete.io.SamPairReader;
import corete.io.misc.PpileupHelpStatReader;


import java.util.ArrayList;
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

	public PpileupFramework(ArrayList<String> inputFiles,String outputFile, String hierFile,int minmapqual,int srmd, float idof, String shortcuts, Logger logger)
	{   this.inputFiles=inputFiles;
		this.outputFile=outputFile;
		this.hierFile=hierFile;
		this.minmapqual=minmapqual;
		this.srmd=srmd;
		this.idof=idof;
		this.teshortcuts=shortcuts;
		this.logger=logger;

	}

	public void run()
	{
		// Read the TE hierarchy
		TEHierarchy hier= new corete.io.HierarchyReader(this.hierFile,this.logger).getHierarchy();

		// BASIC required statistics
		PpileupHelpStatReader hsr=new PpileupHelpStatReader(inputFiles,hier,this.minmapqual,this.srmd, this.logger);
		// Short cut translator
		TEFamilyShortcutTranslator sctr;
		if(new File(this.teshortcuts).exists()){/*TODO read from file */ }
		else{sctr = hsr.getTEabundance().getSCTAbundantShort();}
		// insert size distribution
		ISDSummary isdsum=hsr.getInsertSizeDistribution().getISDSummary(this.idof);
		// Get the Sorting of the reference chromosomes
		ArrayList<String> rcs = new RefChrSortingGeneratorSampleConsensus(hsr.getRefChrSorting(),this.logger).getRefChrConsensusSorting();


	}

}
