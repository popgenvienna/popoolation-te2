package pt2.statreads;

import corete.data.TEFamilyShortcutTranslator;
import corete.data.hier.TEHierarchy;
import corete.data.ppileup.PpileupMultipopBuilder;
import corete.data.stat.EssentialPpileupStats;
import corete.data.stat.ISDSummary;
import corete.data.stat.MapStatReads;
import corete.data.stat.RefChrSortingGeneratorSampleConsensus;
import corete.io.AutoDetectSamBamReader;
import corete.io.TEFamilyShortcutReader;
import corete.io.TEHierarchyReader;
import corete.io.misc.PpileupHelpStatReader;
import corete.io.ppileup.PpileupWriter;
import corete.io.stat.MapStatReadsReader;
import corete.io.stat.MapStatReadsWriter;
import pt2.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 6/29/15.
 */
public class StatReadsFramework {

	private final Logger logger;
	private final ArrayList<String> inputFiles;
	private final String outputFile;
	private final int minmapqual;
	private final String hierFile;


	public StatReadsFramework(ArrayList<String> inputFiles, String outputFile, String hierFile, int minmapqual, Logger logger)
	{
		this.inputFiles=inputFiles;
		this.outputFile=outputFile;
		this.hierFile=hierFile;
		this.minmapqual=minmapqual;

		this.logger=logger;

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
		ArrayList<MapStatReads> msrs=new ArrayList<MapStatReads>();
		for(String s :this.inputFiles)
		{
			msrs.add(new MapStatReadsReader(new AutoDetectSamBamReader(s,this.logger),hier,this.minmapqual,this.logger).readMapStat());
		}

		new MapStatReadsWriter(this.outputFile).writeStats(msrs);
		this.logger.info("Done - thank you for using PoPoolation TE2 ("+ Main.getVersionNumber()+")");






	}

}
