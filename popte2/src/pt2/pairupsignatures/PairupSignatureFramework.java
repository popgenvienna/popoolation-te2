package pt2.pairupsignatures;

import corete.data.polyn.PolyNRecord;
import corete.data.hier.TEHierarchy;
import corete.data.polyn.PolyNRecordCollection;
import corete.data.teinsertion.Signature2InsertionFactory;
import corete.data.teinsertion.SignaturePairupFramework;
import corete.data.teinsertion.TEinsertion;
import corete.data.tesignature.InsertionSignature;
import corete.io.FastaReader;
import corete.io.TEHierarchyReader;
import corete.io.teinsertion.TEInsertionWriter;
import corete.io.tesignature.PolyNReader;
import corete.io.tesignature.TESignatureReader;
import pt2.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 8/31/15.
 */
public class PairupSignatureFramework {
	private final String refgenomeFile;
	private final String signatureFile;
	private final String hierFile;
	private final String outputFile;
	private final int minDistance;
	private final int maxDistance;
	private final double maxfreqdiff;
	private final boolean detailedLog;
	private final Logger logger;


	public PairupSignatureFramework(String signatureFile, String refgenomeFile, String hierFile, String outputFile,
									int minDistance, int maxDistance, double maxfreqdiff, boolean detailedlog, Logger logger)
	{
		// signature, refgenome, hierfile, outputFile,
		// minDistance,maxDistance,maxfreqdiff,detailedLog,logger
		this.refgenomeFile=refgenomeFile;
		if(!new File(refgenomeFile).exists()) throw new IllegalArgumentException("Input file does not exist: "+refgenomeFile);
		this.hierFile=hierFile;
		if(!new File(hierFile).exists()) throw new IllegalArgumentException("Input file does not exist: "+hierFile);
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
		this.minDistance=minDistance;
		this.maxDistance=maxDistance;
		this.maxfreqdiff=maxfreqdiff;
		this.logger=logger;
	}

	public void run()
	{
		this.logger.info("Start pairing up signatures of TE insertions");

		Signature2InsertionFactory s2i =new Signature2InsertionFactory(new TEHierarchyReader(this.hierFile,this.logger).getHierarchy());
		ArrayList<InsertionSignature> signatures= TESignatureReader.readall(signatureFile,logger);
		PolyNRecordCollection polys=new PolyNReader(new FastaReader(this.refgenomeFile,this.logger),this.logger).getPolyNRecords();

		ArrayList<TEinsertion> teinsertions=new SignaturePairupFramework(signatures,s2i,polys,this.maxfreqdiff,this.minDistance, this.maxDistance, logger).getTEinsertions();

		TEInsertionWriter.writeall(this.outputFile,teinsertions,logger);
		this.logger.info("Done - thank you for using PoPoolation TE2 ("+ Main.getVersionNumber()+")");
	}







}
