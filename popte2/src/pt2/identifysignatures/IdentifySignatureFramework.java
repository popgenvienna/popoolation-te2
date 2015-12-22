package pt2.identifysignatures;

import corete.data.TEFamilyShortcutTranslator;
import corete.data.ppileup.PpileupChunk;
import corete.data.stat.EssentialPpileupStats;
import corete.data.tesignature.Chunk2SignatureParser;
import corete.data.tesignature.InsertionSignature;
import corete.data.tesignature.PopulationID;
import corete.io.ppileup.*;
import corete.io.tesignature.TESignatureSymbols;
import corete.io.tesignature.TESignatureWriter;
import pt2.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 8/31/15.
 */
public class IdentifySignatureFramework {
	private final String inputFile;
	private final String outputFile;
	private final SignatureIdentificationMode mode;
	private final int mincount;
	private final SignatureWindowMode windowMode;
	private final int chunkmultiplicator;
	private final boolean detailedLog;
	private final Logger logger;
	//private final int refinmentmultiplicator;

	public IdentifySignatureFramework(String inputFile, String outputFile, SignatureIdentificationMode mode, int mincount
									  ,SignatureWindowMode windowMode, int chunkmultiplicator,
									  boolean detailedlog, Logger logger)
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
		this.mode=mode;
		this.mincount=mincount;
		if(mincount<1) throw new IllegalArgumentException("Minimum count must be larger than zero");
		this.windowMode=windowMode;
		this.chunkmultiplicator=chunkmultiplicator;
		//this.refinmentmultiplicator=refinmentmultiplicator;
		this.detailedLog=detailedlog;
		this.logger=logger;
	}

	public void run()
	{
		this.logger.info("Starting identification of TE signatures");

		// compute the common variables
		PpileupReader pr=new PpileupReader(this.inputFile,this.logger);

		int chunkdistance=pr.getEssentialPpileupStats().getMaximumInnerDistance() * this.chunkmultiplicator;
		// int refinementdistance=pr.getEssentialPpileupStats().getMaximumInnerDistance() * this.refinmentmultiplicator;

		ArrayList<Integer> windowsizes= getWindowSize(pr.getEssentialPpileupStats());

		this.logger.info("Will use a window-mode "+this.windowMode);
		this.logger.info("Will use a minimum distance to next chromosomal chunk of "+chunkdistance);


		ArrayList<InsertionSignature> signatures=null;
		if(this.mode==SignatureIdentificationMode.Joint)
		{
			this.logger.info("Will conduct a joint analysis, ie pooling all sites across all samples to identify TE signatures");
			signatures=run_joint(pr,chunkdistance,windowsizes);
		}
		else if(this.mode==SignatureIdentificationMode.Separate)
		{
			this.logger.info("Will conduct a separate analysis, ie identifying TE signatures in each sample individually");
			signatures=run_separate(pr,chunkdistance,windowsizes);
		}
		//else if(this.mode==SignatureIdentificationMode.SeparateRefined)
		//{
		//	this.logger.info("Will conduct a mixed analysis; TE signatures are identified in each sample seperately and the positions are refined using the joint analysis, pooling all samples");
		//	this.logger.info("Will iteratively scan for refined positions within "+refinementdistance+" the TE signature;");
		//	signatures=run_separate_jointrefined(pr,chunkdistance,windowsize,refinementdistance);
		//}
		else throw new IllegalArgumentException("Unknown mode " +this.mode);


		TESignatureWriter.writeall(this.outputFile,signatures,logger);

		this.logger.info("Done - thank you for using PoPoolation TE2 ("+ Main.getVersionNumber()+")");
	}


	/**
	 * Joint sample analysis
	 */
	private ArrayList<InsertionSignature> run_joint(PpileupReader pr, int chunkdistance, ArrayList<Integer> windowsizes)
	{
		TEFamilyShortcutTranslator translator=pr.getTEFamilyShortcutTranslator();

		ArrayList<InsertionSignature> tmp=new ArrayList<InsertionSignature>();
		PpileupChunkReader chunkReader=new PpileupChunkReader(new PpileupPoolsampleReader(pr),this.mincount,windowsizes,chunkdistance,logger);
		PpileupChunk chunk=null;
		while((chunk=chunkReader.next())!=null)
		{
			tmp.addAll(new Chunk2SignatureParser(chunk, windowsizes, this.mincount,translator).getSignatures()) ;
		}

		ArrayList<InsertionSignature> toret=new ArrayList<InsertionSignature>();
		PopulationID popid=getPopID(pr.getEssentialPpileupStats().countSamples());
		for(InsertionSignature is: tmp)
		{
			toret.add(is.updateSampleId(popid));
		}
		return toret;
	}

	private ArrayList<Integer> getWindowSize(EssentialPpileupStats estats)
	{
		int sampleSize=estats.countSamples();
		ArrayList<Integer> toret=new ArrayList<Integer>();
		for(int i=0; i<sampleSize; i++)
		{
			if(this.windowMode==SignatureWindowMode.FixedWindow)
			{

				toret.add(windowMode.getDistance());
			}
			else if(this.windowMode==SignatureWindowMode.MaximumSampleMedian)
			{

				toret.add(estats.getMaximumInnerDistance());
			}
			else if(this.windowMode==SignatureWindowMode.MinimumSampleMedian)
			{

				toret.add(estats.getMinimumInnerDistance());
			}
			else if(this.windowMode==SignatureWindowMode.Median)
			{

				toret.add(estats.getInnerDistances().get(i));
			}
			else throw new IllegalArgumentException("illegal sample mode "+this.windowMode);
		}
		return toret;
	}




	private PopulationID getPopID(int samplesize)
	{
		 ArrayList<Integer> tot=new ArrayList<Integer>();
		for(int i=0; i<samplesize; i++)
		{
			tot.add(i);
		}
		return new PopulationID(tot);
	}



	/**
	 * Separate analysis of the samples
	 */
	private ArrayList<InsertionSignature> run_separate(PpileupReader pr, int chunkdistance, ArrayList<Integer> windowsizes)
	{
		TEFamilyShortcutTranslator translator=pr.getTEFamilyShortcutTranslator();
		ArrayList<InsertionSignature> toret=new ArrayList<InsertionSignature>();
		PpileupChunkReader chunkReader=new PpileupChunkReader(pr,this.mincount,windowsizes,chunkdistance,logger);
		PpileupChunk chunk=null;
		while((chunk=chunkReader.next())!=null)
		{
			   toret.addAll(new Chunk2SignatureParser(chunk, windowsizes, this.mincount,translator).getSignatures()) ;
		}
		return toret;
	}


	/**
	 * Semi analysis; first identify the insertions
	 * @param pr
	 * @param chunkdistance
	 * @param windowsize
	 * @return

	private ArrayList<InsertionSignature> run_separate_jointrefined(PpileupReader pr, int chunkdistance, int windowsize, int refinementdistance)
	{
		ArrayList<InsertionSignature> tmp=new ArrayList<InsertionSignature>();
		PpileupChunkReader chunkReader=new PpileupChunkReader(pr,this.mincount,windowsize,chunkdistance,logger);
		PpileupChunk chunk=null;
		while((chunk=chunkReader.next())!=null)
		{
			ArrayList<InsertionSignature> torefine=new Chunk2SignatureParser(chunk, windowsize, this.mincount).getSignatures();
			PpileupPoooledPositionRefinementChunk refinementChunk=new PpileupPoooledPositionRefinementChunk(chunk);

			HashSet<InsertionSignature> refined=new HashSet<InsertionSignature>();
			for(InsertionSignature s:torefine)
			{
				refined.add(refinementChunk.getIterativelyRefined(s,windowsize,refinementdistance));
			}
			tmp.addAll(refined);
		}




		ArrayList<InsertionSignature> toret=new ArrayList<InsertionSignature>();
		for(InsertionSignature is: tmp)
		{
			toret.add(is.updateSampleId(TESignatureSymbols.jointSample));
		}
		return toret;
	}
	*/


}
