package pt2.identifysignatures;

import java.util.ArrayList;

/**
 * Created by robertkofler on 11/16/15.
 */
public enum SignatureValleyMode {
	FixedWindow, MinimumSampleMedian,MaximumSampleMedian, Median;

	private ArrayList<Integer> distance;


	public void setDistance(ArrayList<Integer> distance) {this.distance=distance;}
	public ArrayList<Integer> getDistance(){return this.distance;}

	@Override
	public String toString()
	{
		if(this == SignatureValleyMode.MaximumSampleMedian) return "MaximumSampleMedian";
		else if(this== SignatureValleyMode.MinimumSampleMedian) return "MinimumSampleMedian";
		else if(this== SignatureValleyMode.Median) return "Median";
		else if(this== SignatureValleyMode.FixedWindow) {
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
