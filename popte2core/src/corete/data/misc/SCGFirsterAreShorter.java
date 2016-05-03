package corete.data.misc;

import corete.data.TEFamilyShortcutTranslator;

import java.util.*;

/**
 * Created by robertkofler on 8/19/15.
 * Generates shortcuts for TE families, the first in the list are the shortest, may be sorted in any way.
 * All shortcuts must have at least one character that can be lower or upper case;
 * all shortcuts must only contain abcd.... 1234....
 */
public class SCGFirsterAreShorter implements ITEShortCutGenerator {

	private HashSet<String> usedShortCuts = new HashSet<String>();
	private HashSet<String> processed=new HashSet<String>();
	private ArrayList<String> letters=new ArrayList<String>(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "v", "u", "w", "x", "y", "z"));
	private HashSet<String> allowedInShortCut= new HashSet<String>(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "v", "u", "w", "x", "y", "z","1","2","3","4","5","6","7","8","9","0"));

	public SCGFirsterAreShorter()
	{

	}

	/**
	 * Get a shortcut for a TE family;
	 * Shortcut will be as short as possible and unique
	 * not contain weird characters
	 * and be able to distinguish between lower and upper case
	 * @param family
	 * @return
	 */
	@Override
	public String getShortCut(String family)
	{
		String lower=family.toLowerCase();
		if(processed.contains(lower))throw new IllegalArgumentException("Your hierarchy is invalid; Have already created shortcut for family "+family);
		processed.add(lower);
		String cleaned=cleanID(lower);
		String sc=getShortCutForFamily(cleaned);

		if(this.usedShortCuts.contains(sc)) throw new IllegalArgumentException("Error - short cut already exists "+family+" "+sc);
		this.usedShortCuts.add(sc);
		return sc;
	}

	/**
	 * remove weird characters that are not allowed in shortcuts
	 * eg _-; basically only allowed is abc......yz12...89
	 * @param toclean
	 * @return
	 */
	private String cleanID(String toclean)
	{
	   String[] sp=toclean.split("");

		StringBuilder sb=new StringBuilder();
		for(String s: sp)
		{
			if(allowedInShortCut.contains(s))sb.append(s);
		}
		return sb.toString();
	}



	/**
	 * @param fam
	 * @return
	 */
	private String getShortCutForFamily(String fam)
	{
		String shortcutchasis=getShortCutChasis(fam);

		// Make shure the shortcut may be displayed as lower and upper case
		LinkedList<String> use=new LinkedList<String>(letters);
		String basic=shortcutchasis;
		String attempt=basic;
		while(!isValid(attempt))
		{
			if(use.size()>0)
			{
				attempt=basic+use.remove(0);
			}
			else
			{
				basic=basic+letters.get((int)(Math.random()* letters.size()));
				use=new LinkedList<String>(letters);
			}

		}
		return attempt;
	}


	private boolean isValid(String attempt)
	{
		if(attempt.toLowerCase().equals(attempt.toUpperCase()))return false;
		if(this.usedShortCuts.contains(attempt)) return false;
		return true;
	}



	private String getShortCutChasis(String fam)
	{
		//             0123456
		// String fam="jockey";
		// System.out.println(fam.substring(0,1)); //j
		// System.out.println(fam.substring(5,6)); //y
		// System.out.println(fam.substring(4,6)); //ey
		int fl=fam.length();  //jockey = 6

		for(int i=1; i<fl;i++)
		{
			for(int k=0; k < fl-i;k++)
			{
				String attempt=fam.substring(k,k+i);
				if(!usedShortCuts.contains(attempt))
				{
					return attempt;
				}
			}
		}

		// this should probably never happen.. but if a family name is not even unique...well than just add a counter
		String attempt=fam;
		int counter=1;
		while(usedShortCuts.contains(attempt))
		{

			attempt=fam+counter;
			counter++;
		}
		return attempt;
	}



}
