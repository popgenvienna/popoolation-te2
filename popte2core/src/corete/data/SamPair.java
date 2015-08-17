package corete.data;

/**
 * Created by robertkofler on 8/17/15.
 */
public class SamPair {
	public final SamPairType spt;
	public final SamRecord firstRead;
	public final SamRecord secondRead;
	public SamPair(SamRecord firstRead, SamRecord secondRead, SamPairType spt)
	{
		this.firstRead=firstRead;
		this.secondRead=secondRead;
		this.spt=spt;
	}

	public SamRecord getFirstRead(){return this.firstRead;}
	public SamRecord getSecondRead(){return this.secondRead;}
	public SamPairType getSamPairType(){return this.spt;}

}
