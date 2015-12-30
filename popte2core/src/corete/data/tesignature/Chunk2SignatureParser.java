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
	private final ArrayList<Integer> valleysizes;
	//private final int windowsize;
	private final int start;
	private final int end;
	private final String chromosome;
	private final ArrayList<String> teshortcuts;
	private final TEFamilyShortcutTranslator translator;
	private final ArrayList<HashMap<Integer,PpileupSampleSummary>> hsms;
	public Chunk2SignatureParser(PpileupChunk chunk,ArrayList<Integer> windowsizes, ArrayList<Integer> valleysizes, int mincount, TEFamilyShortcutTranslator translator)
	{
		this.toparse=chunk;
		this.windowsizes=new ArrayList<Integer>(windowsizes);
		this.valleysizes=new ArrayList<Integer>(valleysizes);
		this.mincount=(double)mincount;
		this.start=chunk.getStartPosition();
		this.end=chunk.getEndPosition();
		this.chromosome = chunk.getChromosome();
		this.teshortcuts=chunk.getShortcutsOfTEsInChunk();
		this.translator=translator;


		hsms=new ArrayList<HashMap<Integer,PpileupSampleSummary>>();
		for(int i=0; i<this.toparse.size(); i++)
		{
			HashMap<Integer,PpileupSampleSummary> psst= toparse.getPSSTrack(i);
			hsms.add(psst);
		}

	}


	/**
	 * Get all TE signatures in all samples
	 * @return
	 */
	public ArrayList<InsertionSignature> getSignatures()
	{
		ArrayList<InsertionSignature> signatures=new ArrayList<InsertionSignature>();
		ArrayList<SignatureRangeInfo> ranges=this.getRangeSignatures();

		for(SignatureRangeInfo sri:ranges)signatures.add(sri.getSignature());
		return signatures;
	}


	public ArrayList<SignatureRangeInfo> getRangeSignatures()
	{
		ArrayList<SignatureRangeInfo> signatures=new ArrayList<SignatureRangeInfo>();
		// For all Samples
		for(int i=0; i<this.toparse.size(); i++)
		{
			signatures.addAll(getSampleSignatures(i));
		}
		return signatures;
	}




	/*
	For all TEs in a given samples
	 */
	private ArrayList<SignatureRangeInfo> getSampleSignatures(int popindex)
	{
		ArrayList<SignatureRangeInfo> signatures= new ArrayList<SignatureRangeInfo>();

		// for all te shortcuts
		for(String s:this.teshortcuts) {
			ArrayList<SignatureRangeInfo> t=this.getRanges(s,popindex);
			signatures.addAll(t);
		}
		return signatures;
	}


	/**
	 * Get the range signatures for one specific TE family and one specific sample id (popindex)
	 * @param teshortcut
	 * @param popindex
	 * @return
	 */
	public ArrayList<SignatureRangeInfo> getRanges(String teshortcut,int popindex)
	{
		return getTEspecificSampleSignatures(hsms.get(popindex),teshortcut,popindex,this.windowsizes.get(popindex),this.valleysizes.get(popindex));
	}

	/**
	 * For one TE in a given sample
	 * @param sample
	 * @return
	 */
	private ArrayList<SignatureRangeInfo> getTEspecificSampleSignatures(HashMap<Integer,PpileupSampleSummary> sample, String teshortcut,int popindex,int windowsize,int valleysize)
	{
		ArrayList<SignatureRangeInfo> toret=new ArrayList<SignatureRangeInfo>();

		LinkedList<ScoreHelper> window=new LinkedList<ScoreHelper>();

		int lastMaxStart=-1;
		int lastMaxPosition=-1;
		double lastMaxSupport=0.0;
		double runningsum=0.0;
		int minstretchcount=0;

		int rangeStart=-1;
		int rangeEnd=-1;
		double winStartScore=0.0;
		double winEndScore=0.0;

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
				if(rangeStart==-1){
					rangeStart=window.getFirst().position;
				}
				rangeEnd=window.getLast().position;

				// larger than the mincount
				   if(av>=lastMaxSupport)
				   {
					   // larger than the previous maximum count

					   lastMaxStart=window.getFirst().position;
					   lastMaxSupport=av;
					   lastMaxPosition=i;
					   winStartScore=window.getFirst().score;
					   winEndScore=window.getLast().score;
				   }
				minstretchcount=0;

			}
			else
			{
				// lower than the mincount
				minstretchcount++;


				if(lastMaxPosition!=-1 && minstretchcount>=(valleysize+windowsize-1))
				{   // we had a previous highscore => signify (create signature)


					SignatureRangeInfo sri=signify(lastMaxStart,lastMaxPosition,lastMaxSupport,teshortcut,popindex,rangeStart,rangeEnd,winStartScore,winEndScore);
					toret.add(sri);
					lastMaxSupport=0.0;
					lastMaxPosition=-1;
					lastMaxStart=-1;
				}
				if(minstretchcount>=(valleysize+windowsize-1)){
					rangeStart=-1;
					rangeEnd=-1;
				}


			}


		}
		// also process the last  // if a last exists
		if(lastMaxPosition!=-1) {


			SignatureRangeInfo sri = signify(lastMaxStart, lastMaxPosition, lastMaxSupport,teshortcut,popindex,rangeStart,rangeEnd,winStartScore,winEndScore);
			toret.add(sri);
		}
		return toret;

	}

	private SignatureRangeInfo signify(int start,int end,double support,String shortcut,int popindex,int rangeStart,int rangeEnd,double winStartScore,double winEndScore)
	{

		PopulationID popid=new PopulationID(new ArrayList<Integer>(Arrays.asList(popindex)));
		// forward = uppercase is end
		// reverse = lowercase is start
		SignatureDirection sigDir=translator.getSignatureDirection(shortcut);
		String tefamily=translator.getFamilyname(shortcut);


		InsertionSignature sig= new InsertionSignature(popid,this.chromosome,sigDir,start,end,tefamily, TEStrand.Unknown);
		SignatureRangeInfo sri=new SignatureRangeInfo(sig,rangeStart,rangeEnd,winStartScore,winEndScore);
		return sri;

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
