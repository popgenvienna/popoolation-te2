package corete.io;



import corete.data.TEFamilyShortcutTranslator;
import corete.data.ppileup.PpileupSiteLightwight;
import corete.data.ppileup.PpileupSymbols;
import corete.data.stat.EssentialPpileupStats;
import corete.io.Parser.PpileupHeaderParser;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

/**
 * Created by robertkofler on 8/27/15.
 */
public class PpileupReader {
	private final BufferedReader br;
	private final Logger logger;
	private final PpileupHeaderParser pphp;



	//region initialization
	public PpileupReader(String inputFile,Logger logger)
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



	public TEFamilyShortcutTranslator getTEFamilyShortcutTranslator()
	{
		return this.pphp.getTEFamilyShortcutTranslator();
	}

	public EssentialPpileupStats getEssentialPpileupStats()
	{
		EssentialPpileupStats toret= new EssentialPpileupStats(pphp.getInnerDistances(),pphp.getMappingQuality(),pphp.getStructuralRearangementMinDistance(),
				pphp.getVersionNumber());
		return toret;
	}

	public PpileupSiteLightwight readLightwight()
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
		return new PpileupSiteLightwight(chr,pos,popentries);

	}

	public ArrayList<String> breakEntry(String tobreak)
	{
		// handle empty line
		if(tobreak.equals(PpileupSymbols.EMPTYLINE))return new ArrayList<String>();

		String[] work=tobreak.split("");
		ArrayList<String> toret=new ArrayList<String>();

		for(int i=0;i<work.length; i++)
		{
			String s=work[i];
			StringBuilder sb=null;
			boolean temode=false;
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
			}

		}
		return toret;

	}



}
