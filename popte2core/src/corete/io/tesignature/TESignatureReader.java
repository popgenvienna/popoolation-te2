package corete.io.tesignature;

import corete.data.SignatureDirection;
import corete.data.TEStrand;
import corete.data.tesignature.FrequencySampleSummary;
import corete.data.tesignature.InsertionSignature;
import corete.data.tesignature.PopulationID;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 9/3/15.
 */
public class TESignatureReader {
	private BufferedReader reader;
	private Logger logger;

	public TESignatureReader(String inputFile, Logger logger)
	{
		this.logger=logger;
		this.logger.info("Reading TE insertion signatures from file "+inputFile);
		try{
			reader= new BufferedReader(new FileReader(inputFile));
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}

	}

	public static ArrayList<InsertionSignature> readall(String inputFile,Logger logger)
	{
		ArrayList<InsertionSignature> toret=new ArrayList<InsertionSignature>();
		TESignatureReader reader=new TESignatureReader(inputFile,logger);
		InsertionSignature sig=null;
		while((sig=reader.next())!=null)
		{
			toret.add(sig) ;
		}
		reader.close();
		logger.info("Read "+toret.size()+ " TE insertion signatures from file");
		return toret;
	}


	public InsertionSignature next()
	{
		String line=null;
		try{
			line=reader.readLine();
		}
		catch(IOException e)
		{
		e.printStackTrace();
			System.exit(1);
		}
		if(line==null) return null;

		return parseLine(line);

	}

	private InsertionSignature parseLine(String line)
	{
		String[] spl=line.split("\\t");
		// popid, chromosome, position, strand, familyshortcut, average-support
		PopulationID id =PopolutionIDParser.getPopulationID(spl[0]);
		String chr=spl[1];
		int start=Integer.parseInt(spl[2]);
		int end=Integer.parseInt(spl[3]);
		SignatureDirection sigDir=getSignatureDirection(spl[4]);
		String famsc=spl[5];
		TEStrand testra= getTEStrand(spl[6]);
		ArrayList<FrequencySampleSummary> fss=new ArrayList<FrequencySampleSummary>();
		if(spl.length>7)
		{
			for(int i=7; i<spl.length; i++)
			{
				FrequencySampleSummary tfs=translatefss(spl[i]);
				fss.add(tfs);
			}
		}
		return new InsertionSignature(id,chr,sigDir,start,end,famsc,testra,fss);
	}

	private FrequencySampleSummary translatefss(String totr)
	{
		// String.format("%d:%.3f:%.3f:%.3f:%.3f",
		// fss.getPopulationid(),fss.getCoverage(),fss.getGivenTEInsertion(),fss.getOtherTEinsertions(),fss.getStructuralRearrangements());
		String[] s=totr.split(":");
		int popid=Integer.parseInt(s[0]);
		double cov=Double.parseDouble(s[1]);
		double count=Double.parseDouble(s[2]);
		double other=Double.parseDouble(s[3]);
		double struc=Double.parseDouble(s[4]);
		return new FrequencySampleSummary(popid,cov,count,other,struc);
	}


	private TEStrand getTEStrand(String testr)
	{
		if(testr.equals(TESignatureSymbols.teplus))
		{
			return TEStrand.Plus;
		}
		else if (testr.equals(TESignatureSymbols.teminus))
		{
			return TEStrand.Minus;
		}
		else if(testr.equals(TESignatureSymbols.teunknown))
		{
			return TEStrand.Unknown;
		}
		else throw new IllegalArgumentException("Do not recognize strand of TE insertion " +testr);

	}


	private SignatureDirection getSignatureDirection(String st)
	{
		if(st.equals(TESignatureSymbols.forwardInsertion))
		{
			return SignatureDirection.Forward;
		}
		else if(st.equals(TESignatureSymbols.reverseInsertion))
		{
			return SignatureDirection.Reverse;
		}
		else
		{
			throw new IllegalArgumentException("Do not recognize strand "+st);
		}
	}


	public void close()
	{
		try{
			this.reader.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}

	}




}
