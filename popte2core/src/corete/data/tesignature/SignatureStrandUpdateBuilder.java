package corete.data.tesignature;

import corete.data.SignatureDirection;
import corete.data.TEStrand;

/**
 * Created by robertkofler on 11/10/15.
 */
public class SignatureStrandUpdateBuilder {
	private final InsertionSignature sig;
	private int countPlus;
	private int countMinus;
	public SignatureStrandUpdateBuilder(InsertionSignature sig)
	{
		this.sig=sig;
		this.countMinus=0;
		this.countPlus=0;
	}

	public void incrementMinus(){countMinus++;}
	public void incrementPlus(){countPlus++;}

	public InsertionSignature getUpdatedSignature(double maxDisagreement)
	{
		assert(maxDisagreement<=0.5);

		// make sure coverage is sufficient; otherwise return unknown
		int cov=countMinus+countPlus;
		if(cov<1) return this.sig.updateSampleStrand(TEStrand.Unknown);

		// compute the fraction of reads supportint the plus strand
		double fractionPlus=(((double)countPlus)/((double)cov));

		// test if the disagreement is to high
		double antiagrement=1.0-maxDisagreement;
		if(fractionPlus >maxDisagreement && fractionPlus<antiagrement) return this.sig.updateSampleStrand(TEStrand.Unknown);

		// if not estimate the strand of the te insertion
		if(fractionPlus>0.5) return this.sig.updateSampleStrand(TEStrand.Plus);
		else if(fractionPlus<0.5) return this.sig.updateSampleStrand(TEStrand.Minus);
		else return this.sig.updateSampleStrand(TEStrand.Unknown);


	}


	public boolean containsPopulationSample(int popid)
	{
		return this.sig.getPopid().containsPopulationSample(popid);
	}


	public String getRefChr()
	{
		return this.sig.getChromosome();
	}

	public int getPosition()
	{
		if(this.sig.getSignatureDirection() == SignatureDirection.Reverse)
		{
			// reverse is end
			return this.sig.getEnd();

		}
		else if(this.sig.getSignatureDirection()==SignatureDirection.Forward)
		{

			// Forward is start
			return this.sig.getStart();
		}
		else throw new IllegalArgumentException("invalid signature direction");
	}

	public SignatureDirection getDirection()
	{return this.sig.getSignatureDirection();}
	public String getFamily(){return this.sig.getTefamily();}






}
