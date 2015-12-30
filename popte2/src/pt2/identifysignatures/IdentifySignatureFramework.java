package pt2.identifysignatures;

import corete.data.TEFamilyShortcutTranslator;
import corete.data.ppileup.PpileupChunk;
import corete.data.stat.EssentialPpileupStats;
import corete.data.tesignature.*;
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
		for(int i:windowsizes) this.logger.fine("Minimum valley size for sample "+i+1+" = "+valleysizes.get(0));
		compatibleModeWindow(this.mode,windowsizes,valleysizes);

		ArrayList<InsertionSignature> signatures=null;
		if(this.mode==SignatureIdentificationMode.Joint)
		{
			this.logger.info("Will conduct a joint analysis, i.e. pooling all sites across all samples to identify TE signatures");
			signatures=run_joint(pr,chunkdistance,windowsizes,valleysizes);
		}
		else if(this.mode==SignatureIdentificationMode.Separate)
		{
			this.logger.info("Will conduct a separate analysis, i.e. identifying TE signatures in each sample seperately");
			signatures=run_separate(pr,chunkdistance,windowsizes,valleysizes);
		}
		else if(this.mode==SignatureIdentificationMode.SeparateRefined)
		{
			this.logger.info("Will conduct a mixed analysis;");
			this.logger.info("TE signatures are first identified in each sample seperately and the positions of the signatures are than refined using the pooled samples");
			signatures=run_separate_jointrefined(pr,chunkdistance,windowsizes,valleysizes);
		}
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
			tmp.addAll(new Chunk2SignatureParser(chunk, windowsizes,valleysizes, this.mincount,translator).getSignatures()) ;
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
			   toret.addAll(new Chunk2SignatureParser(chunk, windowsizes,valleysizes, this.mincount,translator).getSignatures()) ;
		}
		return toret;
	}


	/**
	 * Semi analysis; first identify the insertions
	 * @param pr
	 * @param chunkdistance
	 * @param windowsizes
	 * @return
	   */
	private ArrayList<InsertionSignature> run_separate_jointrefined(PpileupReader pr, int chunkdistance, ArrayList<Integer> windowsizes, ArrayList<Integer> valleysizes)
	{
		TEFamilyShortcutTranslator translator=pr.getTEFamilyShortcutTranslator();
		ArrayList<InsertionSignature> tmp=new ArrayList<InsertionSignature>();
		PpileupChunkReader chunkReader=new PpileupChunkReader(pr,this.mincount,windowsizes,chunkdistance,logger);
		PpileupChunk chunk=null;
		while((chunk=chunkReader.next())!=null)
		{

			// Identify signatures separately in the samples
			ArrayList<SignatureRangeInfo> torefine=new Chunk2SignatureParser(chunk, windowsizes,valleysizes, this.mincount,translator).getRangeSignatures();

			// get joint sample representation
			SampleChunk2SignatureParser refineparser=new SampleChunk2SignatureParser(chunk.getPooledTrack(),chunk.getChromosome(),chunk.getStartPosition(),chunk.getEndPosition(),translator);

			// Refine
			ArrayList<InsertionSignature> toad =new RefinementChunk2SignatureParser(refineparser,torefine,chunk.getChromosome(),chunk.getStartPosition(),chunk.getEndPosition(),
					windowsizes.get(0),valleysizes.get(0),translator,this.logger).getSignatures();
			tmp.addAll(toad);

		}



		ArrayList<InsertionSignature> toret=new ArrayList<InsertionSignature>();
		PopulationID popid=getPopID(pr.getEssentialPpileupStats().countSamples());
		for(InsertionSignature is: tmp)
		{
			toret.add(is.updateSampleId(popid));
		}
		return toret;
	}


	private void compatibleModeWindow(SignatureIdentificationMode mode, ArrayList<Integer> windowsizes,ArrayList<Integer> valleysizes)
	{
		this.logger.info("Checking if window sizes and valley sizes are compatible with mode "+mode.toString());
		if(mode==SignatureIdentificationMode.Joint || mode==SignatureIdentificationMode.SeparateRefined)
		{
			int comp=windowsizes.get(0);
			for(Integer i: windowsizes)
			{
			 if(i!=comp) throw new
					 IllegalArgumentException("Invalid window sizes; A constant window size must be provided for all samples during joint or separateRefine analysis: "+comp+" vs "+i);

			}

			comp=valleysizes.get(0);
			for(Integer i: valleysizes)
			{
				if(i!=comp) throw new
						IllegalArgumentException("Invalid valley size; A constant valley size must be provided for all samples during joint or separateRefine analysis: "+comp+" vs "+i);

			}

		}




		this.logger.info("Window sizes and valley sizes are OK");
		return;


	}



}
