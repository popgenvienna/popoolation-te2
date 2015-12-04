package corete.data.teinsertion;

import corete.data.SignatureDirection;
import corete.data.hier.TEHierarchy;
import corete.data.tesignature.FrequencySampleSummary;
import corete.data.tesignature.InsertionSignature;

import java.util.ArrayList;

/**
 * Created by robertkofler on 10/28/15.
 */
public class Signature2InsertionFactory {

	private final TEHierarchy hier;

	public Signature2InsertionFactory(TEHierarchy hier)
	{
		this.hier=hier;
	}



	public  TEinsertion fromTwo(InsertionSignature fwd, InsertionSignature rev)
	{

		if(fwd.getSignatureDirection()!= SignatureDirection.Forward) throw new IllegalArgumentException("Forward signatures must be forward strand");
		if(rev.getSignatureDirection()!=SignatureDirection.Reverse) throw new IllegalArgumentException("Reverse signatures must be on reverse strand");
		if(fwd.getFrequencies().size()!=rev.getFrequencies().size()) throw new IllegalArgumentException("Both signatures must be of the same length");
		if(!fwd.getChromosome().equals(rev.getChromosome()))  throw new IllegalArgumentException("Both signatures must be on the same chromosome");
		if(fwd.getTEStrand()!=rev.getTEStrand()) throw new IllegalArgumentException("Both signatures must agree on the strand of the TE insertions");
		if(!fwd.getTefamily().equals(rev.getTefamily())) throw new IllegalArgumentException("both signatures must agree on the TE family");
		if(!fwd.getPopid().equals(rev.getPopid())) throw new IllegalArgumentException("Population IDs of the samples must fit");

		String family=fwd.getTefamily();
		String order=hier.fam2ord(family);
		int position=(int)((fwd.getStart()+rev.getEnd())/2);
		ArrayList<Double> frequencies=new ArrayList<Double>();

		for(int i=0; i<fwd.getFrequencies().size(); i++)
		{
			FrequencySampleSummary fsfwd=fwd.getFrequencies().get(i);
			FrequencySampleSummary fsrev=rev.getFrequencies().get(i);
			double popfreq=(fsfwd.getPopulationFrequency()+fsrev.getPopulationFrequency())/2.0;
			frequencies.add(popfreq);
		}

		return new TEinsertion(fwd.getPopid(),fwd.getChromosome(),position,SignatureDirection.Both, fwd.getTEStrand(),family,order,"",frequencies);

	}

	public TEinsertion fromOne(InsertionSignature one)
	{
	String family=one.getTefamily();
	String order=hier.fam2ord(family);
	int position=one.getDirectionAwarePosition();

	ArrayList<Double> frequencies=new ArrayList<Double>();

	for(int i=0; i<one.getFrequencies().size(); i++)
	{
		double popfreq=one.getFrequencies().get(i).getPopulationFrequency();
		frequencies.add(popfreq);
	}

	return new TEinsertion(one.getPopid(),one.getChromosome(),position,one.getSignatureDirection(), one.getTEStrand(),family,order,"",frequencies);

}






}
