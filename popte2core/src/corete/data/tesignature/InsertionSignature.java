package corete.data.tesignature;

import corete.data.SignatureDirection;
import corete.data.TEStrand;

import java.util.ArrayList;

/**
 * Created by robertkofler on 9/3/15.
 */
public class InsertionSignature {
	private final PopulationID popid;  // eg all, 1, 2, 3
	private final String chromosome;      // hash: chromosome, strand, position, family
	private final SignatureDirection signature;      // +,-,.
	private final TEStrand testrand;
	private final int start;
	private final int end;
	private final String tefamily;
	private final ArrayList<FrequencySampleSummary> frequencies;
	//private final double support;

	public InsertionSignature(PopulationID popid, String chromosome, SignatureDirection signature, int start, int end, String tefamily, TEStrand testrand )
	{
		this(popid,chromosome,signature,start,end,tefamily,testrand,new ArrayList<FrequencySampleSummary>());
	}

	public InsertionSignature(PopulationID popid, String chromosome, SignatureDirection signature, int start, int end, String tefamily, TEStrand testrand, ArrayList<FrequencySampleSummary> frequencies )
	{
		this.popid=popid;
		this.chromosome=chromosome;
		this.signature=signature;
		this.start=start;
		this.end=end;
		this.tefamily=tefamily;
		this.testrand=testrand;
		this.frequencies=frequencies;
		//	this.support=support;
	}



	public PopulationID getPopid(){return this.popid;}
	public String getChromosome(){return this.chromosome;}
	public SignatureDirection getSignatureDirection(){return this.signature;}
	public int getStart(){return this.start;}
	public int getEnd(){return this.end;}
	public String getTefamily(){return this.tefamily;}
	public TEStrand getTEStrand(){return this.testrand;}

	public ArrayList<FrequencySampleSummary> getFrequencies(){return  new ArrayList<FrequencySampleSummary>(this.frequencies);}
	//public double getSupport(){return this.support;}

	public InsertionSignature updateSampleId(PopulationID newId)
	{
		return new InsertionSignature(newId,this.chromosome,this.signature,this.start,this.end,this.tefamily,this.testrand);
	}

	public InsertionSignature updateSampleStrand(TEStrand newStrand)
	{
		return new InsertionSignature(this.popid,this.chromosome,this.signature,this.start,this.end,this.tefamily,newStrand);
	}






	/*
	 * Sort the Insertion signatures by chromosome, position, family and strand
	 * @param b the Snp to compare this SNP to
	 * @return the sort order

	@Override
	public int compareTo(InsertionSignature b)
	{
		//sort by chromosome, position,fam, strand
		int chrcomp=this.chromosome.compareTo(b.getChromosome());
		if(chrcomp!=0) return chrcomp;

		if(this.getStart() < b.getStart()) return -1;
		if(this.getStart() > b.getStart()) return 1;

		if(this.getEnd() < b.getEnd()) return -1;
		if(this.getEnd() > b.getEnd()) return 1;

		int famcomp=this.getTefamily().compareTo(b.getTefamily());
		if(famcomp!=0) return famcomp;

		int sigcomp=this.getSignatureDirection().compareTo(b.getSignatureDirection());
		if(sigcomp!=0) return sigcomp;

		int testrandcomp=this.getTEStrand().compareTo(b.getTEStrand());
		return testrandcomp; // last one, does not matter if zero or not, cause: if zero than equal
	}

	@Override
	public int hashCode()
	{
		// hash: chromosome, strand, position, family
		int hc=17;
		hc=hc*37+this.start;
		hc=hc*37+this.end;
		hc=hc*37+this.chromosome.hashCode();
		hc=hc*37+this.signature.hashCode();
		hc=hc*37+this.testrand.hashCode();
		hc=hc*37+this.tefamily.hashCode();
		return hc;
	}

	@Override
	public boolean equals(Object o)
	{
		// hash: chromosome, strand, position, family
		if(!(o instanceof InsertionSignature)){return false;}
		InsertionSignature is=(InsertionSignature)o;
		if(is.start!=this.start)return false;
		if(is.end!=this.end)return false;
		if(is.signature !=this.signature )return false;
		if(is.testrand !=this.testrand )return false;
		if(!is.chromosome.equals(this.chromosome)) return false;
		if(!is.tefamily.equals(this.tefamily)) return false;
		return true;
	}
	*/



}
