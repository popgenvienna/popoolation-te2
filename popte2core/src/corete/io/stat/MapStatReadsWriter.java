package corete.io.stat;

import corete.data.stat.MapStatReads;
import corete.data.stat.PpileupCoverageStatBuilder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by robertkofler on 8/31/15.
 */
public class MapStatReadsWriter {

	private final String outputFile;
	private BufferedWriter bw;
	private final String separator="\t";

	public MapStatReadsWriter( String outputFile)
	{
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




	public void writeStats(ArrayList<MapStatReads> msrs)
	{
		int samples=msrs.size();

		// No comment...just brilliant...
		writeLine(format("reads_in_file"+separator,msrs,new IStatReducer(){
			@Override
			public int reduce(MapStatReads msr){return msr.getReadsInFile(); }}));

		writeLine(format("reads_mapped"+separator,msrs,new IStatReducer(){
			@Override
			public int reduce(MapStatReads msr){return msr.getReadsMapped(); }}));

		writeLine(format("reads_mapped_with_mq"+separator,msrs,new IStatReducer(){
			@Override
			public int reduce(MapStatReads msr){return msr.getReadsWithMq(); }}));

		writeLine(format("reads_mapped_to_te"+separator,msrs,new IStatReducer(){
			@Override
			public int reduce(MapStatReads msr){return msr.getReadsTe(); }}));

		writeLine(format("reads_mapped_to_te_with_mq"+separator,msrs,new IStatReducer(){
			@Override
			public int reduce(MapStatReads msr){return msr.getReadsTeWithMq(); }}));

		for(String rc:this.getRefChrs(msrs))
		{
			writeLine(format("refchr"+separator+rc+separator,msrs,new IStatReducer(){
				@Override
				public int reduce(MapStatReads msr){return msr.getMappedRefChr().getOrDefault(rc,0);}}));
		}
		for(String te:this.getTEs(msrs))
		{
			writeLine(format("te"+separator+te+separator,msrs,new IStatReducer(){
				@Override
				public int reduce(MapStatReads msr){return msr.getMappedTE().getOrDefault(te,0);}}));
		}

		try{
			this.bw.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(1);
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


	private String format(String linestart, ArrayList<MapStatReads> msrs, IStatReducer reducer)
	{
		StringBuilder sb=new StringBuilder();
		sb.append(linestart);
		for(MapStatReads msr:msrs)
		{
			sb.append(separator);
			sb.append(reducer.reduce(msr));
		}
		return sb.toString();
	}


	private HashSet<String> getTEs(ArrayList<MapStatReads> msrs)
	{
		HashSet<String> toret=new HashSet<String>();
		for(MapStatReads msr:msrs)
		{
			for(String te:msr.getMappedTE().keySet()) toret.add(te);
		}
		return toret;
	}


	private HashSet<String> getRefChrs(ArrayList<MapStatReads> msrs)
	{
		HashSet<String> toret=new HashSet<String>();
		for(MapStatReads msr:msrs)
		{
			for(String rc:msr.getMappedRefChr().keySet()) toret.add(rc);
		}
		return toret;
	}




}



interface IStatReducer
{
	public abstract int reduce(MapStatReads msr);
}

