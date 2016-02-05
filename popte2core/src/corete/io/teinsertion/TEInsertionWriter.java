package corete.io.teinsertion;

import corete.data.SignatureDirection;
import corete.data.TEStrand;
import corete.data.teinsertion.TEinsertion;
import corete.data.tesignature.FrequencySampleSummary;
import corete.io.tesignature.FrequencySampleSummaryFormater;
import corete.io.tesignature.PopolutionIDParser;
import corete.io.tesignature.TESignatureSymbols;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 9/3/15.
 */
public class TEInsertionWriter {
	private final static String sep="\t";
	private Logger logger;
	private BufferedWriter writer;
	private final TEInsertionOutputDetailLevel odl;

	public TEInsertionWriter(String outputFile,TEInsertionOutputDetailLevel odl, Logger logger) {
		this.logger = logger;
		this.odl=odl;
		this.logger.info("Writing TE insertions to file "+outputFile);
		try {
			this.writer = new BufferedWriter(new FileWriter(outputFile));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}


	public static void writeall(String outputFile,ArrayList<TEinsertion> insertions, TEInsertionOutputDetailLevel odl, Logger logger)
	{
		TEInsertionWriter writer=new TEInsertionWriter(outputFile,odl,logger);
		for(TEinsertion ins:insertions)
		{
			writer.write(ins);
		}
		writer.close();
		logger.info("Wrote "+insertions.size() +" TE insertions to file");
	}




	public void write(TEinsertion insertion)
	{
		try
		{
			this.writer.write(format(insertion)+"\n");
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}

	}


	private String format(TEinsertion insertion)
	{

		//private final PopulationID popid;
		//private final String chromosome;
		//private final int position;
		//private final TEStrand strand;
		//private final SignatureDirection signature;
		//private final String family;
		//private final String order;
		//private final String comment;
		//private final ArrayList<Double> popfreqs;

		// popid, chrom, pos, strand, fam, order, support, comment, popfreqs.
		StringBuilder sb=new StringBuilder();
		sb.append(PopolutionIDParser.translateToString(insertion.getPopulationID())); sb.append(sep);
		sb.append(insertion.getChromosome()); sb.append(sep);
		sb.append(insertion.getPosition()); sb.append(sep);
		sb.append(formatTEstrand(insertion.getStrand())); sb.append(sep);
		sb.append(insertion.getFamily()); sb.append(sep);
		sb.append(insertion.getOrder()); sb.append(sep);
		sb.append(formatSignature(insertion.getSignature ()));  sb.append(sep);
		if(insertion.getComment().equals(""))
		{
			   sb.append(TESignatureSymbols.emptyComment);
		}   else {
			sb.append(insertion.getComment());
		}


		// For every
		for(double pf:insertion.getPopulationfrequencies())
		{
			sb.append(sep);
			sb.append(String.format("%.3f",pf));
		}
		if(this.odl==TEInsertionOutputDetailLevel.High || this.odl==TEInsertionOutputDetailLevel.Medium)
		{
			ArrayList<FrequencySampleSummary> forward=insertion.getForwardFSS();
			ArrayList<FrequencySampleSummary> reverse=insertion.getReverseFSS();
			if(forward.size()!=reverse.size()) throw new IllegalArgumentException("Invalid population numbers");
			for(int i=0; i<forward.size(); i++)
			{
				sb.append(sep);
				if(odl==TEInsertionOutputDetailLevel.High)
				{
					sb.append(FrequencySampleSummaryFormater.jointFormatHighDetail(forward.get(i),reverse.get(i)));
				}
				else if(odl==TEInsertionOutputDetailLevel.Medium)
				{
					sb.append(FrequencySampleSummaryFormater.jointFormatMediumDetail(forward.get(i),reverse.get(i)));
				}
				else
				{
					throw new IllegalArgumentException("unknown output mode"+odl);
				}

			}

		}
		return sb.toString();
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
		else if(signature==SignatureDirection.Both)
		{
			return TESignatureSymbols.forwardReverseInsertion;
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
