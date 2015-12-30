package pt2.identifysignatures;

import java.util.ArrayList;

/**
 * Created by robertkofler on 11/16/15.
 */
public enum SignatureWindowMode {
	FixedWindow, MinimumSampleMedian,MaximumSampleMedian, Median;

	private ArrayList<Integer> distance;


	public void setDistance(ArrayList<Integer> distance) {this.distance=distance;}
	public ArrayList<Integer> getDistance(){return this.distance;}

}
