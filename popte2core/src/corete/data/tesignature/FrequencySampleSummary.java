package corete.data.tesignature;

/**
 * Created by robertkofler on 10/21/15.
 */
public class FrequencySampleSummary {
	private final int populationid;
	private final double coverage;
	private final double givenTEInsertion;
	private final double otherTEinsertions;
	private final double structuralRearrangements;


	public FrequencySampleSummary(int popid, double coverage,double givenTEInsertion, double otherTEinsertions, double structuralRearrangements)
	{
		this.populationid=popid;
		this.givenTEInsertion=givenTEInsertion;
		this.coverage=coverage;
		this.otherTEinsertions=otherTEinsertions;
		this.structuralRearrangements=structuralRearrangements;
	}

	public int getPopulationid(){return this.populationid;}

	// counts
	public double getCoverage(){return this.coverage;}
	public double getGivenTEInsertion(){return this.givenTEInsertion;}
	public double getOtherTEinsertions(){return this.otherTEinsertions;}
	public double getStructuralRearrangements(){return this.structuralRearrangements;}

	// frequencies
	public double getPopulationFrequency(){return this.givenTEInsertion/this.coverage;}
	public double getOtherteFrequency(){return this.otherTEinsertions/this.coverage;}
	public double getStructvarFrequency(){return this.structuralRearrangements/this.coverage;}

}
