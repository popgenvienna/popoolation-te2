package corete.io.tesignature;

import com.sun.deploy.util.StringUtils;
import corete.data.tesignature.PopulationID;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by robertkofler on 10/22/15.
 */
public class PopolutionIDParser {



	public static String translateToString(PopulationID id)
	{
		// Increment by 1
		ArrayList<Integer> tmp=id.getIds();
		ArrayList<Integer> top=new ArrayList<Integer>();
		for(int i: tmp) top.add(i+1);



		if(top.size()==1) return top.get(0).toString();

		boolean iscontinues=true;

		if(top.size()<=2) iscontinues=false;
		for(int i=1; i<top.size(); i++)
			{
				   int prev=top.get(i-1);
				int  cur=top.get(i);
				if(prev+1!=cur) iscontinues=false;
			}


		if(iscontinues)
		{
			int first=top.get(0);
			int last=top.get(top.size()-1);
			return ((Integer)first).toString()+"-"+((Integer)last).toString();
		}
		else
		{
			ArrayList<String> strs= new ArrayList<String>();
			for(int  i:top)strs.add(String.valueOf(i));
			return StringUtils.join(strs,",");
		}
	}

	/**
	 * Translates an idea into a population ID
	 * 	example "1"
	 *  example "1,2"
	 *  example "1-5"
	 *  example "1,3-6,9-11"
	 * @param id
	 * @return
	 */
	public static PopulationID getPopulationID(String id)
	{

		// Two cases;
		// single entry (no coma)
		// multi entry separated by coma
		//    two subcases
		//    single entry
		//    range entry of the form from-to
		ArrayList<String> pre=new ArrayList<String>();
		if(id.contains(","))
		{
			String[] tmp=id.split(",");
			pre=new ArrayList<String>(Arrays.asList(tmp));
		}
		else pre.add(id);

		ArrayList<Integer> toret=new ArrayList<Integer>();
		for(String s:pre){
			if(s.contains("-"))
			{
				ArrayList<Integer> toad=parseFromToRange(s);
				toret.addAll(toad);

			}
			else toret.add(Integer.parseInt(s));
		}










		//decrement by one
		ArrayList<Integer> toad=new ArrayList<Integer>();
		for(int i: toret) toad.add(i-1);

		return new PopulationID(toad);

	}

	private static ArrayList<Integer> parseFromToRange(String toparse)
	{
		String[] minmax=toparse.split("-");
		if(minmax.length!=2) throw new IllegalArgumentException("Invalid population ID; from to range must be of the form 'from-to'");
		int from=Integer.parseInt(minmax[0]);
		int to=Integer.parseInt(minmax[1]);
		if(from>=to)throw new IllegalArgumentException("Invalid population ID; from to range must be of the form 'from-to'");
		ArrayList<Integer> toret=new ArrayList<Integer>();

		for(int i=from; i<=to; i++)
		{
			toret.add(i);
		}
		return toret;
	}
}
