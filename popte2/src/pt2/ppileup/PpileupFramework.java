package pt2.ppileup;

import corete.data.SamRecord;
import corete.data.hier.TEHierarchy;

import corete.io.BamReader;



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
	private final String hierFile;

	public PpileupFramework(ArrayList<String> inputFiles,String outputFile, String hierFile,int minmapqual, Logger logger)
	{   this.inputFiles=inputFiles;
		this.outputFile=outputFile;
		this.hierFile=hierFile;
		this.minmapqual=minmapqual;
		this.logger=logger;

	}

	public void run()
	{
		TEHierarchy hier= new corete.io.HierarchyReader(this.hierFile,this.logger).getHierarchy();
		BamReader br=new BamReader(inputFiles.get(0),this.logger);

		while(br.hasNext())
		{
			SamRecord sr=br.next();
			sr.getEnd();
		}
	}

}
