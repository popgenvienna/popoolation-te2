package corete.data.tesignature;

import corete.data.SignatureDirection;
import corete.data.TEFamilyShortcutTranslator;
import corete.data.TEStrand;
import corete.data.ppileup.PpileupChunk;
import corete.data.ppileup.PpileupSampleSummary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by robertkofler on 9/3/15.
 */
public class Chunk2SignatureParser {
	private final PpileupChunk toparse;
	private final double mincount;
	private final ArrayList<Integer> windowsizes;
	//private final int windowsize;
	private final int start;
	private final int end;
	private final String chromosome;
	private final ArrayList<String> teshortcuts;
	private final TEFamilyShortcutTranslator translator;
	public Chunk2SignatureParser(PpileupChunk chunk,ArrayList<Integer> windowsizes, int mincount, TEFamilyShortcutTranslator translator)
	{
		this.toparse=chunk;
		this.windowsizes=new ArrayList<Integer>(windowsizes);
		this.mincount=(double)mincount;
		this.start=chunk.getStartPosition();
		this.end=chunk.getEndPosition();
		this.chromosome = chunk.getChromosome();
		this.teshortcuts=chunk.getShortcutsOfTEsInChunk();
		this.translator=translator;
	}


	/**
	 * Get all TE signatures in all samples
	 * @return
	 */
	public ArrayList<InsertionSignature> getSignatures()
	{
		ArrayList<InsertionSignature> signatures=new ArrayList<InsertionSignature>();
		// For all Samples
		for(int i=0; i<this.toparse.size(); i++)
		{
			HashMap<Integer,PpileupSampleSummary> psst= toparse.getPSSTrack(i);
			signatures.addAll(getSampleSignatures(psst,i,this.windowsizes.get(i)));
		}
		return signatures;
	}

	/*
	For all TEs in a given samples
	 */
	private ArrayList<InsertionSignature> getSampleSignatures(HashMap<Integer,PpileupSampleSummary> sample,int popindex,int windowsize)
	{
		ArrayList<InsertionSignature> signatures= new ArrayList<InsertionSignature>();

		// for all te shortcuts
		for(String s:this.teshortcuts) {
			ArrayList<InsertionSignature> t=getTEspecificSampleSignatures(sample,s,popindex,windowsize);
			signatures.addAll(t);
		}
		return signatures;
	}

	/**
	 * For one TE in a given sample
	 * @param sample
	 * @return
	 */
	private ArrayList<InsertionSignature> getTEspecificSampleSignatures(HashMap<Integer,PpileupSampleSummary> sample, String teshortcut,int popindex,int windowsize)
	{
		ArrayList<InsertionSignature> toret=new ArrayList<InsertionSignature>();

		LinkedList<ScoreHelper> window=new LinkedList<ScoreHelper>();

		int lastMaxStart=-1;
		int lastMaxPosition=-1;
		double lastMaxSupport=0.0;
		double runningsum=0.0;
		int minstretchcount=0;

		for(int i=start; i<=end; i++)
		{
			int tecount=sample.get(i).getTEcount(teshortcut);
			runningsum+=tecount;
			window.add(new ScoreHelper(i,tecount));
			if(window.size()>windowsize){
				ScoreHelper pop=window.remove(0);
				runningsum-=pop.score;
			}
			if(window.size()!=windowsize)continue;

			double av=runningsum/(double)windowsize;
			if(av>=this.mincount)
			{
				// larger than the mincount
				   if(av>=lastMaxSupport)
				   {
					   // larger than the previous maximum count

					   lastMaxStart=window.getFirst().position;
					   lastMaxSupport=av;
					   lastMaxPosition=i;
				   }
				minstretchcount=0;

			}
			else
			{
				// lower than the mincount
				minstretchcount++;
				if(lastMaxPosition!=-1 && minstretchcount>=windowsize)
				{   // we had a previous highscore => signify (create signature)


					InsertionSignature sig=signify(lastMaxStart,lastMaxPosition,lastMaxSupport,teshortcut,popindex);
					toret.add(sig);
					lastMaxSupport=0.0;
					lastMaxPosition=-1;
					lastMaxStart=-1;
				}


			}


		}
		// also process the last  // if a last exists
		if(lastMaxPosition!=-1) {


			InsertionSignature sig = signify(lastMaxStart, lastMaxPosition, lastMaxSupport,teshortcut,popindex);
			toret.add(sig);
		}
		return toret;

	}

	private InsertionSignature signify(int start,int end,double support,String shortcut,int popindex)
	{

		PopulationID popid=new PopulationID(new ArrayList<Integer>(Arrays.asList(popindex)));
		// forward = uppercase is end
		// reverse = lowercase is start
		SignatureDirection sigDir=translator.getSignatureDirection(shortcut);
		String tefamily=translator.getFamilyname(shortcut);


		return new InsertionSignature(popid,this.chromosome,sigDir,start,end,tefamily, TEStrand.Unknown);
	}



	private class ScoreHelper{
		public int position;
		public double score;
		ScoreHelper(int position, double score)
		{
			this.position=position;
			this.score=score;
		}

	}




}
