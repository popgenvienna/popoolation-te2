package corete.data.tesignature;

import corete.data.SignatureDirection;
import corete.data.ppileup.PpileupDirectionalSampleSummary;
import corete.data.ppileup.PpileupSampleSummary;
import corete.data.ppileup.PpileupSite;

import java.util.ArrayList;
import java.util.Map;

/**
 * Estimates the frequency for a given TE insertion signature
 * Rather strict interpretation; only entries from the same family an dinsertion direction are considered as valid;
 * all other entries are considered to stem from a different family, including insertions of the same family but different direction
 * TODO; maybe it wil be necessary to ignore entries of the same family but from a different signature-direction
 * Created by robertkofler on 10/21/15.
 */
public class SignatureFrequencyBuilder {
	private final InsertionSignature signature;
	private final SignatureDirection direction;
	private final String teshortcut;
	private final String antishortcut; // of the same family
	private ArrayList<PpileupSite> sites;
	public SignatureFrequencyBuilder(InsertionSignature signature, String teshortcut, String antishortcut)
	{
		this.signature=signature;
		this.teshortcut=teshortcut;
		this.antishortcut=antishortcut;
		sites=new ArrayList<PpileupSite>();
		this.direction=signature.getSignatureDirection();
	}

	public String getChromosome(){return this.signature.getChromosome();}
	public int getStart(){return this.signature.getStart();}
	public int getEnd(){return this.signature.getEnd();}




	public void addSite(PpileupSite site)
	{
		this.sites.add(site);
	}


	/**
	 * Get an updated signature with frequency information added
	 * @return
	 */
	public InsertionSignature getUpdatedSignature()
	{
		ArrayList<FrequencySampleSummary> freqs=new ArrayList<FrequencySampleSummary>();
		ArrayList<Integer> ids=signature.getPopid().getIds();
		for(int id:ids)
		{
			        freqs.add(getFrequencySampleSummaryForID(id));
		}
		return new InsertionSignature(this.signature.getPopid(),this.signature.getChromosome(),this.signature.getSignatureDirection(),
				this.signature.getStart(),this.signature.getEnd(),this.signature.getTefamily(),this.signature.getTEStrand(),freqs);

	}


	private FrequencySampleSummary getFrequencySampleSummaryForID(int id)
	{
		ArrayList<PpileupDirectionalSampleSummary> ppss=new ArrayList<PpileupDirectionalSampleSummary>();
		for(PpileupSite s:this.sites){
			ppss.add(s.getPpileupSampleSummary(id).getStrandSpecificSampleSummary(this.direction));
		}

		double properPair=0.0;
		double givenTE=0.0;
		double otherTE=0.0;
		double structural=0.0;
		for(PpileupDirectionalSampleSummary pss:ppss)
		{
			//coverage+=(double)pss.getCoverage();
			for(Map.Entry<String,Integer> me:pss.getTecount().entrySet())
			{
				if(me.getKey().equals(this.teshortcut))
				{
					givenTE+=(double)me.getValue();

				}
				else{

					otherTE+=(double)me.getValue();
				}
			}
			properPair+=(double)pss.getAbsence();
			structural+=(double)pss.getStructuralRearrangement();
		}
		double size=(double)ppss.size();

		double coverage=givenTE+otherTE+structural+properPair;
		properPair/=size;
		coverage/=size;
		givenTE/=size;
		otherTE/=size;
		structural/=size;
		return new FrequencySampleSummary(id,coverage,givenTE,otherTE,structural);
	}











}
