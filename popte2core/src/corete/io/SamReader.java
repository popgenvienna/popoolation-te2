package corete.io;

import corete.data.SamRecord;
import corete.io.Parser.CigarParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 6/30/15.
 */
public class SamReader implements ISamBamReader {
	private String inputFile;
	private Logger logger;
	private BufferedReader br;
	private SamRecord next;

	public SamReader(String inputFile, Logger logger)
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
		this.logger.info("Reading sam file "+ inputFile);
		this.next=this.read_next();
	}

	/**
	 * is there a next entry?
	 * @return
	 */
	public boolean hasNext()
	{
		if(next==null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * Get the next sam record
	 * @return
	 */
	public SamRecord next()
	{
		if(!this.hasNext()) throw new InvalidParameterException("no next record!");
		SamRecord toret=next;
		next=this.read_next();
		return toret;
	}


	/**
	 * actually read the next record
	 * @return
	 */
	private SamRecord read_next()
	{
		String line=readNextLine();


		if(line!=null)
		{
			      return getRecord(line);
		}
		else{

			try {
				this.br.close();
			} catch (IOException e) {
				e.printStackTrace();
				// nonzero is abnormal termination
				System.exit(1);
			}
			return null;
		}

	}

	/**
	 * Get next sam line that is not a header; (ignore sam-header)
	 * return null if end of file
	 * @return
	 */
	private String readNextLine()
	{
		while(true)
		{
			String line=null;
			try
			{
				line=br.readLine();
			}
			catch(IOException e)
			{
				e.printStackTrace();
				System.exit(1);
			}

			if(line==null)
			{
				return null;
			}
			else if(!line.startsWith("@"))
			{
				return line;
			}
			// else line startswith @ -> read next line;
		}


	}


	/**
	 * translate a sam line into a SamRecord
	 * @param line
	 * @return
	 */
	private SamRecord getRecord(String line)
	{
		//	public SamRecord(String readname, int flag, String refChr, int start, int end, int start_withs, int end_withs, int mapq, String cigar, String refChrMate,
		// int posMate, int len, String seq, String qual, String comment)
		// 0					1	2				3	4	5			6					7	8	9	10	11
		//ERR143187.43134260	113	211000022279100	746	0	60S41M	DMRER1DM_dmelwol_m33	462	0	CAA	##B	AS:i:37

		String[] a=line.split("\\t");
		String name=a[0];
		   int flag = Integer.parseInt(a[1]);
		String refChr=a[2];
		int start=Integer.parseInt(a[3]);
		int mapq=Integer.parseInt(a[4]);
		String cigar=a[5];
		String refChrMate=a[6];
		if(refChrMate=="=") {refChrMate=refChr;}
		int posMate=Integer.parseInt(a[7]);
		int distMate=Integer.parseInt(a[8]);
		String sequence=a[9];
		String quality=a[10];
		String comment=a[11];
		CigarParser cp=new CigarParser(cigar,start);
		SamRecord rec=new SamRecord(name,flag,refChr,cp.getStart(),cp.getEnd(),cp.getStart_withs(),cp.getEnd_withs(),mapq,cigar,refChrMate,posMate,100,sequence,quality,comment);
		return rec;

	}







}
