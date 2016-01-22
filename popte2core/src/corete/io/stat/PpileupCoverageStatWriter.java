package corete.io.stat;

import corete.data.stat.PpileupCoverageStatBuilder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by robertkofler on 8/31/15.
 */
public class PpileupCoverageStatWriter {
	private final PpileupCoverageStatBuilder builder;
	private final String outputFile;
	private final String sep="\t";
	private BufferedWriter bw;

	public PpileupCoverageStatWriter(PpileupCoverageStatBuilder builder,String outputFile)
	{
		this.builder=builder;
		this.outputFile=outputFile;
		try
		{
			bw=new BufferedWriter(new FileWriter(outputFile));
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}


	public void writeStats()
	{
		writeJointStats();
		writeSampleStats();
		try
		{
			bw.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}


	private void writeSampleStats()
	{
		int totalsites=builder.getTotalsites();
	   for(int i=0;i<this.builder.getSamplecount(); i++)
	   {
		   HashMap<Integer,Integer> act=builder.getCoverageStatistics(i);
		   int lost=0;
		   for(int k=1; k<=builder.getMaxcoverage(); k++)
		   {
			   int count= act.getOrDefault(k,0);
			   if(count==0)continue;
			   double pc=100.0*((double)lost/totalsites);
			   String topr=formatCovStat(Integer.toString(i+1),k,count,lost,pc);

			   //finally write and increent lost
				writeLine(topr);
				lost+=count;


		   }
	   }
	}

	public void close()
	{
		try {
			this.bw.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}


	private String formatCovStat(String sample, int coverage, int count, int lost, double percentlost)
	{
		StringBuilder sb=new StringBuilder();
		sb.append(sample); sb.append(sep);
		sb.append(coverage); sb.append(sep);
		sb.append(count); sb.append(sep);
		sb.append(lost); sb.append(sep);
		String fpercent=String.format("%.2f",percentlost);
		sb.append(fpercent);
		return sb.toString();
	}


	private void writeJointStats()
	{
		// format: Joint coverage sites sumlost percentlost
		double totalsites=(double)builder.getTotalsites();

		HashMap<Integer,Integer> joint=builder.getJointCoverageStatistics();
		int lost=0; // lost is everything that is smaller
		for(int i=1; i<=this.builder.getMaxcoverage(); i++)
		{
			int count=joint.getOrDefault(i,0);
			if(count==0) continue;
			double pc=100.0*((double)lost/totalsites);
			String topr=formatCovStat("joint",i,count,lost,pc);

			// WRITE
			writeLine(topr);
			lost+=count;
		}
	}




	private void writeLine(String line)
	{
		try{
			this.bw.write(line + "\n");
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}

	}




}
