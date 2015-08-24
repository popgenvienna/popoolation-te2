package corete.data.stat;

import corete.data.TEFamilyShortcutTranslator;
import corete.data.misc.ITEShortCutGenerator;
import corete.data.misc.SCGFirsterAreShorter;

import java.util.*;

/**
 * Created by robertkofler on 8/18/15.
 */
public class TEabundanceContainer {
	private final ArrayList<TEabundance> abundances;
	private ITEShortCutGenerator scg;


	 public TEabundanceContainer(ArrayList<TEabundance> abundances)
	 {
		 this.abundances=new ArrayList<TEabundance>(abundances);
		 this.scg=new SCGFirsterAreShorter();
	 }


	/**
	 * TE abundance summed over all populations/samples
	 * @return
	 */
	public HashMap<String,Integer> getOverallTEabundance()
	{
		HashMap<String,Integer> tesum=new HashMap<String,Integer>();
		for(TEabundance ab:this.abundances)
		{
			for(Map.Entry<String,Integer> e: ab.getTEabundance().entrySet())
			{
				if(tesum.containsKey(e.getKey()))
				{
					tesum.put(e.getKey(),tesum.get(e.getKey())+e.getValue());
				}
				else
				{
					tesum.put(e.getKey(),e.getValue());
				}

			}
		}
		return tesum;
	}


	/**
	 * Get a shortcut translator where the most abundant TE families have the shortest shortcut
	 * @return
	 */
	public TEFamilyShortcutTranslator getSCTAbundantShort()
	{

		ArrayList<String>  families=getTEfamiliesDescendingByAbundance(getOverallTEabundance());

		HashMap<String,String> long2short=new HashMap<String,String>();
		for(String fam: families)
		{
			 String sc = this.scg.getShortCut(fam);
			long2short.put(fam,sc);
		}
		return  TEFamilyShortcutTranslator.getFull2ShortTranslator(long2short);

	}


	private ArrayList<String> getTEfamiliesDescendingByAbundance(HashMap<String,Integer> abundance)
	{
		// sort by abundance, descending, most abundant on top
		ArrayList<Map.Entry<String,Integer>> mapes= new ArrayList<Map.Entry<String,Integer>>(abundance.entrySet());
		Collections.sort(mapes,new Comparator<Map.Entry<String,Integer>>() {
			 @Override
		public int compare(Map.Entry<String,Integer> m1, Map.Entry<String,Integer> m2)
			 {
				 if(m1.getValue() <  m2.getValue()) return 1;
				 if(m1.getValue() > m2.getValue()) return -1;
				 return 0;
			 }
		});

		// just get the names, of the sorted ones
		ArrayList<String> toret=new ArrayList<String>();
		for(Map.Entry<String,Integer> me: mapes)
		{
			toret.add(me.getKey());
		}

		return toret;
	}






}
