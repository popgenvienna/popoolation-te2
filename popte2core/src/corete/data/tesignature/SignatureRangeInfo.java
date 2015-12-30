package corete.data.tesignature;

/**
 * Created by robertkofler on 12/30/15.
 */
public class SignatureRangeInfo {
	private InsertionSignature signature;
	private int rangeStart;
	private int rangeEnd;
	private double winStartScore;
	private double winEndScore;

	public SignatureRangeInfo(InsertionSignature signature, int rangeStart, int rangeEnd,double winStartScore,double winEndScore){
		this.signature = signature;
		this.rangeStart=rangeStart;
		this.rangeEnd = rangeEnd;
		this.winStartScore=winStartScore;
		this.winEndScore=winEndScore;
	}

	public InsertionSignature getSignature(){return this.signature;}
	public int getRangeStart(){return this.rangeStart;}
	public int getRangeEnd(){return this.rangeEnd;}
	public double getWinStartScore(){return this.winStartScore;}
	public double getWinEndScore(){return this.winEndScore;}

	public double getMinBorderScore(){
		if(this.winStartScore<this.winEndScore) return this.winStartScore;
		else return this.winEndScore;
	}


}
