package corete.data.tesignature;

import corete.data.hier.TEHierarchy;
import corete.io.SamPairReader;

import java.util.ArrayList;

/**
 * Created by robertkofler on 11/10/15.
 */
public class SignatureStrandUpdateReader {
	private SamPairReader reader;
	private ArrayList<SignatureStrandUpdateBuilder> builders;
	private int medianDistance;
	private TEHierarchy hier;

	public SignatureStrandUpdateReader(SamPairReader reader, TEHierarchy hier,int medianDistance, ArrayList<SignatureStrandUpdateBuilder> builder)
	{
		this.reader=reader;
		this.hier=hier;
		this.medianDistance=medianDistance;
		this.builders=new ArrayList<SignatureStrandUpdateBuilder>(builder);
	}


	public void updateSignatures()
	{

	}



}
