package corete.data.stat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 8/24/15.
 */
public class RefChrSortingGeneratorSampleConsensus {
	private final RefChrSortingContainer rcscontainer;
	private final Logger logger;

	public RefChrSortingGeneratorSampleConsensus(RefChrSortingContainer rcscontainer,Logger logger)
	{
		this.rcscontainer=rcscontainer;
		this.logger=logger;
	}


	public ArrayList<String> getSorting()
	{
		return null;
	}




	public ArrayList<String> getRefChrConsensusSorting()
	{
		this.logger.info("Extracting consensus sorting of reference chromosomes across all bam/sam files");

		HashMap<String,Integer> rccount=getRefChrCounts();
		ArrayList<String> toret=new ArrayList<String>();
		int filecount=this.rcscontainer.getFilecount();
		for(String s: this.rcscontainer.getRefChrSortingOfFile(0))
		{
			if(rccount.get(s)==filecount) toret.add(s);
		}

		// Validate and log
		boolean valid=this.rcscontainer.isSortingConsistentInAllFiles(toret);
		if(!valid) throw new IllegalArgumentException("Sorting of reference chromosomes in bam/sam files is inconsistent;  Please resort your files using one consistent approach");
		this.logger.info("Finished extracting valid sorting of reference chromosomes");
		this.logger.info("Will generate ppileup-file for "+toret.size()+ " reference chromosomes and "+filecount+" files");
		this.logger.fine("Will use the following sorting:");
		for(String s:toret)
		{
		     this.logger.fine(s);
		}

		return toret;
	}




	private  HashMap<String,Integer> getRefChrCounts()
	{
		HashMap<String,Integer> rcc=new HashMap<String,Integer>();
		for(ArrayList<String> al:this.rcscontainer.getAllRefChrSortings())
		{
			HashSet<String> doublecheck=new HashSet<String>();
			for(String s: al)
			{
				// First check.. just to be sure that no entry is present multiple times
				if(doublecheck.contains(s)) throw new IllegalArgumentException("Reference chromosome present multiple times "+s);
				doublecheck.add(s);

				// Than add the counts
				if(!rcc.containsKey(s)) rcc.put(s,0);
				rcc.put(s,rcc.get(s)+1);
			}
		}
		return rcc;
	}




}
