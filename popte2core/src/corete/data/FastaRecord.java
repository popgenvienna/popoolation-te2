package corete.data;

/**
 * Created by robertkofler on 10/23/15.
 */
public class FastaRecord {
	private final String header;
	private final String sequence;
	public FastaRecord(String header, String sequence)
	{
		this.header=header;
		this.sequence=sequence;
	}

	public String getHeader(){return this.header;}
	public String getSequence(){return this.sequence;}



}
