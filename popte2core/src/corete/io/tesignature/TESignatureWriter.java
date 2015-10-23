package corete.io.tesignature;

import corete.data.SignatureDirection;
import corete.data.TEStrand;
import corete.data.tesignature.FrequencySampleSummary;
import corete.data.tesignature.InsertionSignature;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 9/3/15.
 */
public class TESignatureWriter {
	private final static String sep="\t";
	private Logger logger;
	private BufferedWriter writer;

	public TESignatureWriter(String outputFile, Logger logger) {
		this.logger = logger;
		this.logger.info("Writing TE signatures to file "+outputFile);
		try {
			this.writer = new BufferedWriter(new FileWriter(outputFile));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}


	public static void writeall(String outputFile,ArrayList<InsertionSignature> signatures,Logger logger)
	{
		TESignatureWriter writer=new TESignatureWriter(outputFile,logger);
		for(InsertionSignature sig:signatures)
		{
			writer.write(sig);
		}
		writer.close();
		logger.info("Wrote "+signatures.size() +" TE insertion signatures to file");
	}




	public void write(InsertionSignature signature)
	{
		try
		{
			this.writer.write(format(signature)+"\n");
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}

	}


	private String format(InsertionSignature signature)
	{
		// popid, chromosome, start, end, signaturedirection, familyshortcut, testrand
		StringBuilder sb=new StringBuilder();
		sb.append(PopolutionIDParser.translateToString(signature.getPopid())); sb.append(sep);
		sb.append(signature.getChromosome()); sb.append(sep);
		sb.append(signature.getStart()); sb.append(sep);
		sb.append(signature.getEnd()); sb.append(sep);
		sb.append(formatSignature(signature.getSignatureDirection())); sb.append(sep);
		sb.append(signature.getTefamily()); sb.append(sep);
		sb.append(formatTEstrand(signature.getTEStrand()));

		// For every
		for(FrequencySampleSummary fss:signature.getFrequencies())
		{
			sb.append(sep);
			sb.append(formatFrequencySampleSummary(fss));
		}
		return sb.toString();
	}

	private String formatFrequencySampleSummary(FrequencySampleSummary fss)
	{
		    String toret=String.format("%d:%.3f:%.3f:%.3f:%.3f",fss.getPopulationid(),fss.getCoverage(),fss.getGivenTEInsertion(),fss.getOtherTEinsertions(),fss.getStructuralRearrangements());
			return toret;
	}


	private String formatSignature(SignatureDirection signature)
	{
		if(signature== SignatureDirection.Forward){
			return TESignatureSymbols.forwardInsertion;
		}
		else if (signature== SignatureDirection.Reverse)
		{
			return TESignatureSymbols.reverseInsertion;
		}
		else
		{
			throw new IllegalArgumentException("Invalid strand");
		}
	}

	private String formatTEstrand(TEStrand testrand)
	{
		if(testrand== TEStrand.Plus){
			return TESignatureSymbols.teplus;
		}
		else if (testrand== TEStrand.Minus)
		{
			return TESignatureSymbols.teminus;
		}
		else if(testrand==TEStrand.Unknown)
		{
			return TESignatureSymbols.teunknown;
		}
		else
		{
			throw new IllegalArgumentException("Invalid strand");
		}
	}


	public void close()
	{
		try {
			this.writer.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}





}
