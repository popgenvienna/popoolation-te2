package pt2.identifysignatures;

/**
 * Created by robertkofler on 11/16/15.
 */
public enum SignatureWindowMode {
	FixedWindow, MinimumSampleMedian,MaximumSampleMedian, Median;

	private  int distance;


	public void setDistance(int distance) {this.distance=distance;}
	public int getDistance(){return this.distance;}

}
