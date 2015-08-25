package corete.io;

import com.sun.corba.se.impl.encoding.BufferManagerWriteStream;
import corete.data.ppileup.PpileupBuilder;

import java.io.*;
import java.util.ArrayList;
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

	public PpileupWriter(String outputFile, boolean zippedOutput,Logger logger)
	{
		this.zipped=zippedOutput;
		this.logger=logger;
		if(zippedOutput){
				this.logger.info("Writing zipped ppileup-file to "+outputFile);
				this.bw=getZippedWriter(outputFile);

		}

		else
		{
			this.logger.info("Writing un-zipped ppileup-file to "+outputFile);
			this.bw=getUnzippedWriter(outputFile);
		}



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



	public void writeEntry(String chromosome, int position, ArrayList<String> entries)
	{
		StringBuilder sb=new StringBuilder();
		sb.append(chromosome); sb.append(sep);
		sb.append(position);
		for(String e : entries)
		{
			sb.append(sep); sb.append(e);
		}
		try {
			bw.write(sb.toString());
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
