package pt2.freqforsig;

import corete.data.TEFamilyShortcutTranslator;
import corete.data.ppileup.PpileupChunk;
import corete.data.tesignature.Chunk2SignatureParser;
import corete.data.tesignature.InsertionSignature;
import corete.data.tesignature.SignatureFrequencyEstimationFramework;
import corete.io.ppileup.PpileupChunkReader;
import corete.io.ppileup.PpileupPoolsampleReader;
import corete.io.ppileup.PpileupReader;
import corete.io.tesignature.TESignatureReader;
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
public class FrequencyForSignatureFramework {
	private final String ppileupFile;
	private final String signatureFile;
	private final String outputFile;
	private final boolean detailedLog;
	private final Logger logger;


	public FrequencyForSignatureFramework(String ppileupFile, String signatureFile, String outputFile,
										  boolean detailedlog, Logger logger)
	{
		this.ppileupFile=ppileupFile;
		if(!new File(ppileupFile).exists()) throw new IllegalArgumentException("Input file does not exist: "+ppileupFile);
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
	}

	public void run()
	{
		this.logger.info("Starting estimating frequencies of features for signatures of TE insertions");

		// compute the common variables
		PpileupReader pr=new PpileupReader(this.ppileupFile,this.logger);

		ArrayList<InsertionSignature> signatures= TESignatureReader.readall(signatureFile,logger);


		ArrayList<InsertionSignature> updatedSignatures=new SignatureFrequencyEstimationFramework(pr,signatures,logger).getSignaturesWithFrequencies();

	   assert(signatures.size()==updatedSignatures.size());

		TESignatureWriter.writeall(this.outputFile,updatedSignatures,logger);

		this.logger.info("Done - thank you for using PoPoolation TE2 ("+ Main.getVersionNumber()+")");
	}







}
