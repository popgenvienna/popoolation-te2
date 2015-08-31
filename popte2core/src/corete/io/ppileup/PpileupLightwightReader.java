package corete.io.ppileup;



import corete.data.TEFamilyShortcutTranslator;
import corete.data.ppileup.PpileupSiteLightwight;
import corete.data.ppileup.PpileupSymbols;
import corete.data.stat.EssentialPpileupStats;
import corete.io.Parser.PpileupHeaderParser;
import corete.misc.LogFactory;

import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

/**
 * Created by robertkofler on 8/27/15.
 */
public class PpileupLightwightReader implements IPpileupLightwightReader {
	private final BufferedReader br;
	private final Logger logger;
	private final PpileupHeaderParser pphp;



	//region initialization
	public PpileupLightwightReader(String inputFile, Logger logger)
	{
		this.logger=logger;
		this.logger.info("Reading ppileup file "+inputFile);
		this.logger.info("Scanning for gzip compression; valid extensions: .gz|.gzip");
		if(inputFile.endsWith(".gz")|| inputFile.endsWith(".gzip"))
		{
			this.logger.info("Ppileup is zipped");
			this.br=getZippedReader(inputFile);
		}
		else{
			this.logger.info("Ppileup is unzipped");
			this.br=getUnzippedReader(inputFile);
		}

		this.pphp=this.readHeader();

	}

	/**
	 * Only for debugging purposes
	 * @param br
	 */
	public PpileupLightwightReader(BufferedReader br)
	{
		this.br=br;
		this.logger= LogFactory.getNullLogger();
		this.pphp=this.readHeader();
	}


	private PpileupHeaderParser readHeader()
	{
		ArrayList<String> header=new ArrayList<String>();
		String line=null;


			while((line=this.nextLine())!=null)
			{
				if(!line.startsWith("@"))
				{
					this.bufferLine(line);
					break;
				}
				else
				{
					header.add(line);
				}
			}
		return new PpileupHeaderParser(header);
	}


	private BufferedReader getZippedReader(String inputFile)
	{
		try{
			return new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(inputFile))));

		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		return null;

	}

	private BufferedReader getUnzippedReader(String inputFile)
	{
		try{
			      return new BufferedReader(new FileReader(inputFile));
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}


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

	//endregion



	@Override
	public TEFamilyShortcutTranslator getTEFamilyShortcutTranslator()
	{
		return this.pphp.getTEFamilyShortcutTranslator();
	}

	@Override
	public EssentialPpileupStats getEssentialPpileupStats()
	{
		EssentialPpileupStats toret= new EssentialPpileupStats(pphp.getInnerDistances(),pphp.getMappingQuality(),pphp.getStructuralRearangementMinDistance(),
				pphp.getVersionNumber());
		return toret;
	}

	/**
	 * Get a lightwight representation of a ppileup sites;
	 * without resolved TE names.
	 * @return
	 */
	@Override
	public PpileupSiteLightwight next()
	{
		String line=this.nextLine();
		if(line==null) return null;
		String[] t=line.split("\\t");

		//chr
		String chr=t[0];
		//pos
		int pos=Integer.parseInt(t[1]);
		//comment
		String comment=t[2];
		if(comment.equals(PpileupSymbols.EMPTYCOMMENT))comment="";


		ArrayList<ArrayList<String>> popentries=new ArrayList<ArrayList<String>>();
		for(int i=3; i<t.length; i++)
		{
			ArrayList<String> tmp= breakEntry(t[i]);
			popentries.add(tmp);
		}
		return new PpileupSiteLightwight(chr,pos,comment,popentries);

	}

	private ArrayList<String> breakEntry(String tobreak)
	{
		// handle empty line
		if(tobreak.equals(PpileupSymbols.EMPTYLINE))return new ArrayList<String>();

		String[] work=tobreak.split("");
		ArrayList<String> toret=new ArrayList<String>();

		StringBuilder sb=null;
		boolean temode=false;
		for(int i=0;i<work.length; i++)
		{
			String s=work[i];
			switch(s) {
				// useless symbols $^ don't do anything
				case PpileupSymbols.PPSTART:
				case PpileupSymbols.PPEND:
					break;
				case PpileupSymbols.TEstart:
					temode=true;
					sb=new StringBuilder();
					break;
				case PpileupSymbols.TEend:
					temode=false;
					toret.add(sb.toString());
					break;
				default:
					if(temode){sb.append(s);}
					else toret.add(s);
					break;
			}

		}
		return toret;
	}



}
