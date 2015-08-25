package corete.data.stat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by robertkofler on 8/24/15.
 */
public class LastPositionContainer {
	private final ArrayList<HashMap<String,Integer>> lpc;
	public LastPositionContainer(ArrayList<HashMap<String,Integer>> lpc)
	{

		ArrayList<HashMap<String,Integer>> tmp= new ArrayList<HashMap<String,Integer>>();

		for(HashMap<String,Integer> hm:lpc)
		{
			tmp.add(new HashMap<String,Integer>(hm));
		}
		this.lpc=tmp;
	}


	public HashMap<String,Integer> getLastPosition()
	{
		HashMap<String,Integer> overall=new HashMap<String,Integer>();
		for(HashMap<String,Integer> hm: this.lpc)
		{
			for(Map.Entry<String,Integer> me:hm.entrySet())
			{
				String chr=me.getKey();
				int lp=me.getValue();
				overall.putIfAbsent(chr,-1);
				if(lp>overall.get(chr)) overall.put(chr,lp);

			}
		}
		return overall;
	}


}
