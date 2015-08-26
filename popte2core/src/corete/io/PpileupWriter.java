package corete.io;

import com.sun.corba.se.impl.encoding.BufferManagerWriteStream;
import corete.data.TEFamilyShortcutTranslator;
import corete.data.ppileup.PpileupBuilder;
import corete.data.ppileup.PpileupSymbols;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;

/**
 * Created by robertkofler on 8/21/15.
 */
public class PpileupWriter {
	private static final String sep="\t";
	private final BufferedWriter bw;
	private final Logger logger;
	private final boolean zipped;
	private final TEFamilyShortcutTranslator translator;

	public PpileupWriter(String outputFile, boolean zippedOutput, TEFamilyShortcutTranslator translator,Logger logger )
	{
		this.zipped=zippedOutput;
		this.logger=logger;
		this.translator=translator;
		if(zippedOutput){
				this.logger.info("Writing zipped ppileup-file to "+outputFile);
				this.bw=getZippedWriter(outputFile);

		}

		else
		{
			this.logger.info("Writing un-zipped ppileup-file to "+outputFile);
			this.bw=getUnzippedWriter(outputFile);
		}

		write_header();


	}

	private BufferedWriter getUnzippedWriter(String outputFile)
	{
		BufferedWriter bw=null;
		try {
			bw=new BufferedWriter(new FileWriter(outputFile));
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		return bw;
	}

	private BufferedWriter getZippedWriter(String outputFile)
	{

		BufferedWriter bw=null;
		try
		{
			bw= new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(outputFile))));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		return bw;
	}


	private void write_header()
	{
		HashMap<String,String> f2s=this.translator.getFull2short();
		for(Map.Entry<String,String> e:f2s.entrySet())
		{
			StringBuilder sb=new StringBuilder();
			sb.append("@SC");sb.append(sep);
			sb.append(e.getValue());sb.append(sep);
			sb.append(e.getKey());sb.append(sep);

			try {
			  this.bw.write(sb.toString()+"\n");


			}
			catch(IOException ex)
			{
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}


	public void writeEntry(String chromosome, int position, String comment, ArrayList<String> entries)
	{
		StringBuilder sb=new StringBuilder();
		sb.append(chromosome); sb.append(sep);
		sb.append(position);  sb.append(sep);
		if(comment==null) sb.append(PpileupSymbols.EMPTYCOMMENT);
		else sb.append(comment);
		for(String e : entries)
		{
			sb.append(sep); sb.append(e);
		}
		try {
			bw.write(sb.toString()+"\n");
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void close()
	{
		this.logger.info("Finished writing ppileup file");
		try {
			this.bw.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

}
