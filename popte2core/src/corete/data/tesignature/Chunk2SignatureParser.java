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
	private final ArrayList<SampleChunk2SignatureParser> sc2sps;
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


		sc2sps=new ArrayList<SampleChunk2SignatureParser>();
		for(int i=0; i<this.toparse.size(); i++)
		{
			HashMap<Integer,PpileupSampleSummary> psst= toparse.getPSSTrack(i);
			sc2sps.add(new SampleChunk2SignatureParser(psst,this.chromosome,this.start,this.end,this.translator));
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
			ArrayList<SignatureRangeInfo> t=this.sc2sps.get(popindex).getRanges(s,popindex,this.windowsizes.get(popindex),
					this.valleysizes.get(popindex),this.mincount);
			signatures.addAll(t);
		}
		return signatures;
	}









}
