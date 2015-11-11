package pt2.statpairs;

import corete.data.hier.TEHierarchy;
import corete.data.stat.MapStatPairs;
import corete.data.stat.MapStatReads;
import corete.io.AutoDetectSamBamReader;
import corete.io.SamPairReader;
import corete.io.TEHierarchyReader;
import corete.io.stat.MapStatPairWriter;
import corete.io.stat.MapStatPairsReader;
import corete.io.stat.MapStatReadsReader;
import corete.io.stat.MapStatReadsWriter;
import pt2.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 6/29/15.
 */
public class StatPairsFramework {

	private final Logger logger;
	private final ArrayList<String> inputFiles;
	private final String outputFile;
	private final int minmapqual;
	private final String hierFile;
	private final int srmd;


	public StatPairsFramework(ArrayList<String> inputFiles, String outputFile, String hierFile, int minmapqual, int srmd, Logger logger)
	{
		this.inputFiles=inputFiles;
		this.outputFile=outputFile;
		this.hierFile=hierFile;
		this.minmapqual=minmapqual;
		this.srmd=srmd;

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

		this.logger.info("Starting creating statistics for the mapped pairs");
		// Read the TE hierarchy
		TEHierarchy hier= new TEHierarchyReader(this.hierFile,this.logger).getHierarchy();
		ArrayList<MapStatPairs> msps=new ArrayList<MapStatPairs>();
		for(String s :this.inputFiles)
		{
			msps.add(new MapStatPairsReader(new SamPairReader(s,hier,srmd,logger),minmapqual,logger).pairMapStat()) ;
		}

		new MapStatPairWriter(this.outputFile).writeStats(msps);
		this.logger.info("Done - thank you for using PoPoolation TE2 ("+ Main.getVersionNumber()+")");






	}

}
