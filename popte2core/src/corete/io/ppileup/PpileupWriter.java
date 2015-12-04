package corete.io.ppileup;

import corete.data.TEFamilyShortcutTranslator;
import corete.data.ppileup.DirectionalPpileupSiteLightwight;
import corete.data.ppileup.PpileupSiteLightwight;
import corete.data.ppileup.PpileupSymbols;
import corete.data.ppileup.PpileupeHeaderSymbols;
import corete.data.stat.EssentialPpileupStats;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
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
	private final EssentialPpileupStats estats;

	public PpileupWriter(String outputFile, boolean zippedOutput, TEFamilyShortcutTranslator translator,EssentialPpileupStats estats,Logger logger )
	{
		this.zipped=zippedOutput;
		this.logger=logger;
		this.translator=translator;
		this.estats=estats;

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
		try {
			for (Map.Entry<String, String> e : f2s.entrySet()) {
				StringBuilder sb = new StringBuilder();
				sb.append(PpileupeHeaderSymbols.SHORTCUT);
				sb.append(sep);
				sb.append(e.getValue());
				sb.append(sep);
				sb.append(e.getKey());
				sb.append(sep);
				this.bw.write(sb.toString() + "\n");
			}


			for(int i=0; i<this.estats.getInnerDistances().size(); i++){
				int index=i+1;
				int innerdist=this.estats.getInnerDistance(i);

				this.bw.write(PpileupeHeaderSymbols.DEFAULTINNERDISTANCE+"\t" + index + "\t" +innerdist+"\n");
			}
			this.bw.write(PpileupeHeaderSymbols.MAPPINGQUALITY+"\t"+this.estats.getMinMapQual()+"\n");
			this.bw.write(PpileupeHeaderSymbols.STRUCTREARMINDIST+"\t"+this.estats.getStructuralRearrangementMinimumDistance()+"\n");
			this.bw.write(PpileupeHeaderSymbols.INNERDISTUPQUANT+"\t"+this.estats.getInnerDistanceUpperQuantile()+"\n");
			this.bw.write(PpileupeHeaderSymbols.VERSIONNUMBER+"\t"+estats.getVersionNumber()+"\n");
		}
		catch(IOException ex)
		{
				ex.printStackTrace();
				System.exit(1);
		}




	}

	public void writeEntry(PpileupSiteLightwight lw)
	{
		ArrayList<String> tos=new ArrayList<String>();
		for(int i=0; i<lw.size(); i++)
		{
			ArrayList<String> s=lw.getEntries(i);
			StringBuilder sb=new StringBuilder();
			for(String part:s)
			{
				if(part.length()>1)
				{
					sb.append(PpileupSymbols.TEstart);
					sb.append(part);
					sb.append(PpileupSymbols.TEend);
				}
				else sb.append(part);
			}
			tos.add(sb.toString());
		}
		this.writeEntry(lw.getChromosome(),lw.getPosition(),lw.getComment(),tos);
	}




	public void writeEntry(DirectionalPpileupSiteLightwight ds)
	{
		ArrayList<ArrayList<String>> merged =new ArrayList<ArrayList<String>>();

		for(int i=0; i<ds.size(); i++)
		{
			ArrayList<String> m=new ArrayList<String>();
			LinkedList<String> fwdabs=new LinkedList<String>();
			LinkedList<String> revabs=new LinkedList<String>();
			for(String s:ds.getForwardEntries(i)) {
				if (!s.equals(PpileupSymbols.ABSFWD)) m.add(s);
				else fwdabs.add(s);
			}
			for(String s:ds.getReverseEntries(i))
			{
				if(!s.equals(PpileupSymbols.ABSREV)) m.add(s);
				else revabs.add(s);
			}
			while(fwdabs.size()> 0 && revabs.size()>0)
			{
				fwdabs.remove(0); revabs.remove(0);
				m.add(PpileupSymbols.ABS);
			}
			while(fwdabs.size()>0) m.add(fwdabs.remove(0));
			while(revabs.size()>0) m.add(revabs.remove(0));
			merged.add(m);
		}
		this.writeEntry(new PpileupSiteLightwight(ds.getChromosome(),ds.getPosition(),ds.getComment(),merged));
	}



	public void writeEntry(String chromosome, int position, String comment, ArrayList<String> entries)
	{

		StringBuilder sb=new StringBuilder();
		sb.append(chromosome); sb.append(sep);
		sb.append(position);  sb.append(sep);
		if(comment.length()== 0) sb.append(PpileupSymbols.EMPTYCOMMENT); //use the empty comment symbol
		else sb.append(comment);
		for(String e : entries)
		{
			String topr=e;
			if(topr.length()==0)topr=PpileupSymbols.EMPTYLINE; 			//if null than use the empty line symbol
			sb.append(sep); sb.append(topr);
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
