package corete.io.stat;

import corete.data.stat.MapStatPairs;
import corete.data.stat.MapStatReads;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import corete.io.stat.IStatReducer;


/**
 * Created by robertkofler on 11/11/15.
 */
public class MapStatPairWriter {


		private final String outputFile;
		private BufferedWriter bw;
		private final String separator="\t";

		public MapStatPairWriter( String outputFile)
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




		public void writeStats(ArrayList<MapStatPairs> msps)
		{

			/*
	private final int mappedPairs;
	private final int properPairs;
	private final int properPairsWithMq;
	private final int structuralVariants;
	private final int structuralVariantsWithMq;
	private final int tePair;
	private final int tePairWithMq;
	private final HashMap<String,Integer> mapProperPair;
	private final HashMap<String,Integer> mapStructuralRearrangement;
	private final HashMap<String,Integer> mapTE;
			 */

			// No comment...just brilliant...
			writeLine(format("pe_fragments"+separator,msps,new IStatPairReducer(){
				@Override
				public int reduce(MapStatPairs msp){return msp.getMapped(); }}));

			writeLine(format("proper_pair_candidates"+separator,msps,new IStatPairReducer(){
				@Override
				public int reduce(MapStatPairs msp){return msp.getPairs(); }}));

			writeLine(format("proper_pairs"+separator,msps,new IStatPairReducer(){
				@Override
				public int reduce(MapStatPairs msp){return msp.getProperPairsWithMq(); }}));

			writeLine(format("sv_fragments"+separator,msps,new IStatPairReducer(){
				@Override
				public int reduce(MapStatPairs msp){return msp.getStructuralVariants(); }}));

			writeLine(format("sv_fragments_mq"+separator,msps,new IStatPairReducer(){
				@Override
				public int reduce(MapStatPairs msp){return msp.getStructuralVariantsWithMq(); }}));

			writeLine(format("te_fragments"+separator,msps,new IStatPairReducer(){
				@Override
				public int reduce(MapStatPairs msp){return msp.getTePair(); }}));

			writeLine(format("te_fragments_mq"+separator,msps,new IStatPairReducer(){
				@Override
				public int reduce(MapStatPairs msp){return msp.getTePairWithMq(); }}));



			for(String rc:this.getPPKeys(msps))
			{
				writeLine(format("pp"+separator+rc,msps,new IStatPairReducer(){
					@Override
					public int reduce(MapStatPairs msp){return msp.getMapProperPair().getOrDefault(rc,0);}}));
			}

			for(String rc:this.getSVKeys(msps))
			{
				writeLine(format("sv"+separator+rc,msps,new IStatPairReducer(){
					@Override
					public int reduce(MapStatPairs msp){return msp.getMapStructuralRearrangement().getOrDefault(rc,0);}}));
			}

			for(String rc:this.getTEs(msps))
			{
				writeLine(format("te"+separator+rc,msps,new IStatPairReducer(){
					@Override
					public int reduce(MapStatPairs msp){return msp.getMapTE().getOrDefault(rc,0);}}));
			}


			for(String rc:this.getTEchrKeys(msps))
			{
				writeLine(format("te_chr"+separator+rc,msps,new IStatPairReducer(){
					@Override
					public int reduce(MapStatPairs msp){return msp.getMapTEChr().getOrDefault(rc,0);}}));
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


		private String format(String linestart, ArrayList<MapStatPairs> msps, IStatPairReducer reducer)
		{
			StringBuilder sb=new StringBuilder();
			sb.append(linestart);
			for(MapStatPairs ms:msps)
			{
				sb.append(separator);
				sb.append(reducer.reduce(ms));
			}
			return sb.toString();
		}


		private HashSet<String> getTEs(ArrayList<MapStatPairs> msps)
		{
			HashSet<String> toret=new HashSet<String>();
			for(MapStatPairs msp:msps)
			{
				for(String te: msp.getMapTE().keySet()) toret.add(te);
			}
			return toret;
		}


		private HashSet<String> getSVKeys(ArrayList<MapStatPairs> msps)
		{
			HashSet<String> toret=new HashSet<String>();
			for(MapStatPairs msp:msps)
			{
				for(String rc:msp.getMapStructuralRearrangement().keySet()) toret.add(rc);
			}
			return toret;
		}


	private HashSet<String> getPPKeys(ArrayList<MapStatPairs> msps)
	{
		HashSet<String> toret=new HashSet<String>();
		for(MapStatPairs msp:msps)
		{
			for(String rc:msp.getMapProperPair().keySet()) toret.add(rc);
		}
		return toret;
	}

	private HashSet<String> getTEchrKeys(ArrayList<MapStatPairs> msps)
	{
		HashSet<String> toret=new HashSet<String>();
		for(MapStatPairs msp:msps)
		{
			for(String rc:msp.getMapTEChr().keySet()) toret.add(rc);
		}
		return toret;
	}



	}



interface IStatPairReducer {

	public abstract int reduce(MapStatPairs msr);
}





