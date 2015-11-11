package pt2.updateTEstrand;

import corete.data.TEStrand;
import corete.data.hier.TEHierarchy;
import corete.data.stat.ISDSummary;
import corete.data.stat.InsertSizeDistributionContainer;
import corete.data.tesignature.InsertionSignature;
import corete.data.tesignature.SignatureFrequencyEstimationFramework;
import corete.data.tesignature.SignatureStrandUpdateBuilder;
import corete.data.tesignature.SignatureStrandUpdateReader;
import corete.io.SamPairReader;
import corete.io.TEHierarchyReader;
import corete.io.misc.PpileupHelpStatReader;
import corete.io.ppileup.PpileupReader;
import corete.io.tesignature.TESignatureReader;
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
public class UpdateStrandFramework {
	private final ArrayList<String> inputFiles;
	private final String signatureFile;
	private final String outputFile;
	private final double maxDisagreement;
	private final boolean detailedLog;
	private final String hierFile;
	private final Logger logger;
	private final int mapQual;
	private final int srmd;
	private final float idof;


	public UpdateStrandFramework(ArrayList<String> inputFiles, String signatureFile, String outputFile, String hierFile,
								double maxDisagreement, int mapQual, int srmd, float idof, boolean detailedlog, Logger logger)
	{
		this.hierFile=hierFile;
		if(!new File(hierFile).exists()) throw new IllegalArgumentException("Input file does not exist: "+hierFile);
		this.maxDisagreement=maxDisagreement;
		if(maxDisagreement<0 || maxDisagreement>0.5) throw new IllegalArgumentException("Max disagreement must be between 0 and 0.5");
		for(String file: inputFiles)
		{
			if(!new File(file).exists()) throw new IllegalArgumentException("Input file does not exist: "+file);
		}
		this.inputFiles=new ArrayList<String>(inputFiles);
		this.signatureFile=signatureFile;
		if(!new File(signatureFile).exists()) throw new IllegalArgumentException("Input file does not exist: "+signatureFile);
		this.outputFile=outputFile;
		try{
			new FileWriter(outputFile);
		}
		catch(IOException e)
		{
			throw new IllegalArgumentException("Can not create output file:" +outputFile);
		}
		this.detailedLog=detailedlog;
		this.logger=logger;
		this.srmd=srmd;
		this.mapQual=mapQual;
		this.idof=idof;
	}




	public void run()
	{
		this.logger.info("Starting updating the strand of TE insertions");
		ArrayList<SignatureStrandUpdateBuilder> builders= getBuilder(TESignatureReader.readall(signatureFile, logger));
		// Read the TE hierarchy
		TEHierarchy hier= new TEHierarchyReader(this.hierFile,this.logger).getHierarchy();

		// BASIC required statistics
		PpileupHelpStatReader hsr=new PpileupHelpStatReader(inputFiles,hier,this.mapQual,this.srmd, this.logger);
		ISDSummary is =hsr.getInsertSizeDistribution().getISDSummary(idof);


		for(int i=0; i<this.inputFiles.size(); i++)
		{
			ArrayList<SignatureStrandUpdateBuilder> toupdate=getBuildersForPopulationNumber(builders,i);
			SamPairReader spr=new SamPairReader(this.inputFiles.get(i),hier,srmd,logger);

			SignatureStrandUpdateReader ssur=new SignatureStrandUpdateReader(spr,hier,is.getMedian(i),toupdate);
			ssur.updateSignatures();
		}




		 ArrayList<InsertionSignature> newSignatures=updateSignatures(builders);

		TESignatureWriter.writeall(this.outputFile,newSignatures,logger);

		this.logger.info("Done - thank you for using PoPoolation TE2 ("+ Main.getVersionNumber()+")");
	}

	private ArrayList<SignatureStrandUpdateBuilder> getBuildersForPopulationNumber(ArrayList<SignatureStrandUpdateBuilder> builders, int populationID)
	{
		ArrayList<SignatureStrandUpdateBuilder> toret=new ArrayList<SignatureStrandUpdateBuilder>();
		for(SignatureStrandUpdateBuilder ssb: builders)
		{
			if(ssb.containsPopulationSample(populationID))toret.add(ssb);
		}
		return toret;
	}



	private ArrayList<InsertionSignature> updateSignatures(ArrayList<SignatureStrandUpdateBuilder> builders)
	{
		int countPlus=0;
		int countMinus=0;
		int countUnknown=0;

		ArrayList<InsertionSignature> toret=new ArrayList<InsertionSignature>();
		for(SignatureStrandUpdateBuilder b:builders) {
			InsertionSignature s=b.getUpdatedSignature(maxDisagreement);
			if(s.getTEStrand()== TEStrand.Minus)countMinus++;
			else if(s.getTEStrand()==TEStrand.Plus)countPlus++;
			else if(s.getTEStrand()==TEStrand.Unknown)countUnknown++;
			else throw new IllegalArgumentException("Unknwon strand "+s.getTEStrand());
			toret.add(s);
		}

		this.logger.info("Finished updating strand of TE signatures");
		this.logger.info("Final results; plus strand "+countPlus+"; minus strand "+countMinus+"; unknown "+countUnknown);

		return toret;
	}


	private ArrayList<SignatureStrandUpdateBuilder> getBuilder(ArrayList<InsertionSignature> sig)
	{
		ArrayList<SignatureStrandUpdateBuilder> builders=new ArrayList<SignatureStrandUpdateBuilder>();
		for(InsertionSignature s:sig) builders.add(new SignatureStrandUpdateBuilder(s));
		return builders;
	}







}
