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
	private final SignatureWindowMode minValley;
	private final int chunkmultiplicator;
	private final boolean detailedLog;
	private final Logger logger;
	//private final int refinmentmultiplicator;

	public IdentifySignatureFramework(String inputFile, String outputFile, SignatureIdentificationMode mode, int mincount
									  ,SignatureWindowMode windowMode, SignatureWindowMode minValley, int chunkmultiplicator,
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
		this.minValley=minValley;
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

		ArrayList<Integer> windowsizes= getWindowSize(this.windowMode,pr.getEssentialPpileupStats());
		ArrayList<Integer> valleysizes= getWindowSize(this.minValley,pr.getEssentialPpileupStats());

		this.logger.info("Will use a window-mode "+this.windowMode);
		this.logger.info("Will use a minimum distance to next chromosomal chunk of "+chunkdistance);
		for(int i:windowsizes) this.logger.fine("Window size for sample "+i+1+" = "+windowsizes.get(0));
		for(int i:windowsizes) this.logger.fine("Minimum vallye size for sample "+i+1+" = "+valleysizes.get(0));

		ArrayList<InsertionSignature> signatures=null;
		if(this.mode==SignatureIdentificationMode.Joint)
		{
			this.logger.info("Will conduct a joint analysis, ie pooling all sites across all samples to identify TE signatures");
			signatures=run_joint(pr,chunkdistance,windowsizes,valleysizes);
		}
		else if(this.mode==SignatureIdentificationMode.Separate)
		{
			this.logger.info("Will conduct a separate analysis, ie identifying TE signatures in each sample individually");
			signatures=run_separate(pr,chunkdistance,windowsizes,valleysizes);
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
	private ArrayList<InsertionSignature> run_joint(PpileupReader pr, int chunkdistance, ArrayList<Integer> windowsizes, ArrayList<Integer> valleysizes)
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

	/**
	 * Get the window sizes for a given window mode and pileup statisitics
	 * @param win
	 * @param estats
	 * @return
	 */
	private ArrayList<Integer> getWindowSize(SignatureWindowMode win, EssentialPpileupStats estats)
	{
		int sampleSize=estats.countSamples();
		ArrayList<Integer> toret=new ArrayList<Integer>();
		for(int i=0; i<sampleSize; i++)
		{
			if(win==SignatureWindowMode.FixedWindow)
			{

				ArrayList<Integer> distances=windowMode.getDistance();


				int mod=i%distances.size();
				toret.add(distances.get(mod));
				// size 1, 3 sample => 012=000
				// size 2, 4 sample => 0123=0101
				// size 3, 3 sample => 012=012
			}
			else if(win==SignatureWindowMode.MaximumSampleMedian)
			{

				toret.add(estats.getMaximumInnerDistance());
			}
			else if(win==SignatureWindowMode.MinimumSampleMedian)
			{

				toret.add(estats.getMinimumInnerDistance());
			}
			else if(win==SignatureWindowMode.Median)
			{

				toret.add(estats.getInnerDistances().get(i));
			}
			else throw new IllegalArgumentException("illegal sample mode "+win);
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
	private ArrayList<InsertionSignature> run_separate(PpileupReader pr, int chunkdistance, ArrayList<Integer> windowsizes,ArrayList<Integer> valleysizes)
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
