package corete.data.tesignature;

import corete.data.SignatureDirection;
import corete.data.TEFamilyShortcutTranslator;
import corete.data.TEStrand;
import corete.data.ppileup.PpileupChunk;
import corete.data.ppileup.PpileupSampleSummary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by robertkofler on 9/3/15.
 */
public class Chunk2SignatureParser {
	private final PpileupChunk toparse;
	private final double mincount;
	private final int windowsize;
	private final int start;
	private final int end;
	private final String chromosome;
	private final ArrayList<String> teshortcuts;
	private final TEFamilyShortcutTranslator translator;
	public Chunk2SignatureParser(PpileupChunk chunk,int windowsize, int mincount, TEFamilyShortcutTranslator translator)
	{
		this.toparse=chunk;
		this.windowsize=windowsize;
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
		for(int i=0; i<this.toparse.size(); i++)
		{
			HashMap<Integer,PpileupSampleSummary> psst= toparse.getPSSTrack(i);
			signatures.addAll(getSampleSignatures(psst,i));
		}
		return signatures;
	}

	/*
	For all TEs in a given samples
	 */
	private ArrayList<InsertionSignature> getSampleSignatures(HashMap<Integer,PpileupSampleSummary> sample,int popindex)
	{
		ArrayList<InsertionSignature> signatures= new ArrayList<InsertionSignature>();

		// for all te shortcuts
		for(String s:this.teshortcuts) {
			ArrayList<InsertionSignature> t=getTEspecificSampleSignatures(sample,s,popindex);
			signatures.addAll(t);
		}
		return signatures;
	}

	/**
	 * For one TE in a given sample
	 * @param sample
	 * @return
	 */
	private ArrayList<InsertionSignature> getTEspecificSampleSignatures(HashMap<Integer,PpileupSampleSummary> sample, String teshortcut,int popindex)
	{
		ArrayList<InsertionSignature> toret=new ArrayList<InsertionSignature>();

		LinkedList<Integer> window=new LinkedList<Integer>();

		int lastMaxPosition=-1;
		double lastMaxSupport=0.0;
		double runningsum=0.0;

		for(int i=start; i<=end; i++)
		{
			int tecount=sample.get(i).getTEcount(teshortcut);
			runningsum+=tecount;
			window.add(tecount);
			if(window.size()>this.windowsize){
				int pop=window.remove(0);
				runningsum-=pop;
			}
			if(window.size()!=this.windowsize)continue;

			double av=runningsum/(double)this.windowsize;
			if(av>=this.mincount)
			{
				// larger than the mincount
				   if(av>=lastMaxSupport)
				   {
					   // larger than the previous maximum count
					   lastMaxSupport=av;
					   lastMaxPosition=i;
				   }

			}
			else
			{
				// lower than the mincount
				if(lastMaxPosition!=-1)
				{   // we had a previous highscore => signify (create signature)

					int start=lastMaxPosition-this.windowsize+1;
					InsertionSignature sig=signify(start,lastMaxPosition,lastMaxSupport,teshortcut,popindex);
					toret.add(sig);
					lastMaxSupport=0.0;
					lastMaxPosition=-1;
				}

			}


		}
		// also process the last  // if a last exists
		if(lastMaxPosition!=-1) {

			int start = lastMaxPosition - this.windowsize + 1;
			InsertionSignature sig = signify(start, lastMaxPosition, lastMaxSupport,teshortcut,popindex);
			toret.add(sig);
		}
		return toret;

	}

	private InsertionSignature signify(int start,int end,double support,String shortcut,int popindex)
	{
		  String sampleid=getSampleid(popindex);
		// forward = uppercase is end
		// reverse = lowercase is start
		SignatureDirection sigDir=translator.getSignatureDirection(shortcut);
		String tefamily=translator.getFamilyname(shortcut);


		return new InsertionSignature(sampleid,this.chromosome,sigDir,start,end,tefamily, TEStrand.Unknown);
	}




	private String getSampleid(int popindex)
	{
		return Integer.toString(popindex);
	}



}
