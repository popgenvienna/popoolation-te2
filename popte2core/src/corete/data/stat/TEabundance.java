package corete.data.stat;

import java.util.HashMap;

/**
 * Created by robertkofler on 8/18/15.
 */
public class TEabundance {
	private final HashMap<String,Integer> famcount;

	public TEabundance(HashMap<String,Integer> famcount)
	{
		this.famcount=new HashMap<String,Integer>(famcount);
	}


	public HashMap<String,Integer> getTEabundance()
	{
		return new HashMap<String,Integer>(this.famcount);
	}



}
