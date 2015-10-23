package corete.io;

import corete.data.FastaRecord;
import corete.data.SamRecord;
import corete.io.Parser.CigarParser;
import corete.io.SamValidator.ISamValidator;
import corete.io.SamValidator.SamValidatorAllValid;
import corete.misc.LogFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 6/30/15.
 */
public class FastaReader {
	private final String inputFile;
	private final Logger logger;
	private BufferedReader br;
	private String bufferedline;


	public FastaReader(String inputFile, Logger logger)
	{
		this.inputFile=inputFile;
		this.logger=logger;
		try
		{
			this.br=new BufferedReader(new FileReader(inputFile));
		}
		catch(IOException e)
		{
			e.printStackTrace();
			// non-zero indicates abnormal termination
			System.exit(1);
		}
		this.logger.info("Reading fasta file "+ inputFile);
	}


	/**
	 *  Constructor for testing purposes only
	 *  null logger + null validator
	 */
	public FastaReader(BufferedReader br)
	{
		this.br=br;
		this.logger= LogFactory.getNullLogger();
		this.inputFile="";

	}


	public String getInputFile()
	{return this.inputFile;}

	//region read Buffer - SamPair
	private String bufferedLine=null;
	private void bufferLine(String line)
	{
		assert(line!=null);
		this.bufferedLine=line;
	}

	private String nextLine()
	{
		if(this.bufferedLine!=null)
		{
			String toret=this.bufferedLine;
			this.bufferedLine=null;
			return toret;
		}
		else
		{
			try{
				return this.br.readLine();
			}
			catch(IOException e)
			{
				e.printStackTrace();
				System.exit(1);
			}
			return null;
		}
	}
	//endregion





	/**
	 * Get the next sam record
	 * @return
	 */
	public FastaRecord next()
	{

		String header=null;
		StringBuilder sb=new StringBuilder();

		String line=null;
		while((line=this.nextLine())!=null)
		{
			if(line.startsWith(">"))
			{
				if(header==null)
				{
					header=line.replaceFirst("^>","");
				}
				else
				{
					this.bufferLine(line);
					return new FastaRecord(header,sb.toString());
				}

			}
			else sb.append(line);

		}

		if(header!=null) return new FastaRecord(header,sb.toString());
		else return null;


	}







}
