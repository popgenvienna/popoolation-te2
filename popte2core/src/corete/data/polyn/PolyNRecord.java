package corete.data.polyn;

/**
 * Created by robertkofler on 10/23/15.
 */
public class PolyNRecord {
	private final String chromosome;
	private final int start;
	private final int end;
	public PolyNRecord(String chromosome, int start, int end)
	{
		this.chromosome=chromosome;
		this.start=start;
		this.end=end;
	}

	public String getChromosome(){return this.chromosome;}
	public int getStart(){return this.start;}
	public int getEnd(){return this.end;}
	public int length(){
		int leng=this.end-this.start+1;
		return leng;

	}

}
