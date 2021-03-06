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

	@Override
	public String toString()
	{
		if(this == SignatureWindowMode.MaximumSampleMedian) return "MaximumSampleMedian";
		else if(this== SignatureWindowMode.MinimumSampleMedian) return "MinimumSampleMedian";
		else if(this==SignatureWindowMode.Median) return "Median";
		else if(this==SignatureWindowMode.FixedWindow) {
			StringBuilder sb=new StringBuilder();
			sb.append("Fixed");
			for(int i:this.distance)
			{
				sb.append(" "+i);
			}
			return sb.toString();
		}
		else return "Unknown";
	}


}
