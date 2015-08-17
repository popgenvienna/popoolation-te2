package pt2.ppileup;

import corete.data.SamRecord;
import corete.data.hier.TEHierarchy;

import corete.io.AutoDetectSamBamReader;
import corete.io.BamReader;
import corete.io.ISamBamReader;


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
	private final double idof;
	private final String hierFile;

	public PpileupFramework(ArrayList<String> inputFiles,String outputFile, String hierFile,int minmapqual,int srmd,double idof, Logger logger)
	{   this.inputFiles=inputFiles;
		this.outputFile=outputFile;
		this.hierFile=hierFile;
		this.minmapqual=minmapqual;
		this.srmd=srmd;
		this.idof=idof;
		this.logger=logger;

	}

	public void run()
	{
		TEHierarchy hier= new corete.io.HierarchyReader(this.hierFile,this.logger).getHierarchy();
		ISamBamReader reader=new AutoDetectSamBamReader(this.inputFiles.get(0),this.logger);

		while(reader.hasNext())
		{
			SamRecord sr=reader.next();
			sr.getEnd();
		}
	}

}
