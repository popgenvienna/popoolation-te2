package corete.data.teinsertion;

import corete.data.SignatureDirection;
import corete.data.TEStrand;
import corete.data.tesignature.InsertionSignature;
import corete.data.tesignature.PopulationID;
import htsjdk.samtools.cram.encoding.readfeatures.Insertion;

import java.util.ArrayList;

/**
 * Created by robertkofler on 10/23/15.
 */
public class TEinsertion {
	private final PopulationID popid;
	private final String chromosome;
	private final int position;
	private final TEStrand strand;
	private final SignatureDirection signature;
	private final String family;
	private final String order;
	private final String comment;
	private final ArrayList<Double> popfreqs;

	public TEinsertion(PopulationID popid, String chromosome, int position, SignatureDirection signature, TEStrand strand, String family, String order, String comment,
					   ArrayList<Double> popfreqs)
	{
		this.popid = popid;
		this.chromosome= chromosome;
		this.position=position;
		this.family=family;
		this.strand=strand;
		this.order=order;
		this.signature=signature;
		this.comment=comment;
		this.popfreqs=new ArrayList<Double>(popfreqs);
	}

	public PopulationID getPopulationID(){return this.popid;}
	public String getChromosome(){return this.chromosome;}
	public int getPosition(){return this.position;}
	public SignatureDirection getSignature(){return this.signature;}
	public TEStrand getStrand(){return this.strand;}
	public String getFamily(){return this.family;}
	public String getOrder(){return this.order;}
	public String getComment(){return this.comment;}
	public ArrayList<Double> getPopulationfrequencies(){return new ArrayList<Double>(this.popfreqs);}

}
