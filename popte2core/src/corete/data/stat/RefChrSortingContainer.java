package corete.data.stat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Created by robertkofler on 8/24/15.
 * Contains the sorting of reference chromosomes in all bam/sam files
 */
public class RefChrSortingContainer {
	private final ArrayList<ArrayList<String>> rcs;
	private final int filecount;
	public RefChrSortingContainer(ArrayList<ArrayList<String>> refchrsortings)
	{
		this.rcs=new ArrayList<ArrayList<String>>();
		for(ArrayList<String> al:refchrsortings)
		{
		   rcs.add(new ArrayList<String>(al));
		}
		this.filecount=rcs.size();
	}


	/**
	 * Get the file count, ie the number of samples
	 * @return
	 */
	public int getFilecount()
	{
		return this.filecount;
	}

	/**
	 * Get the reference sorting container
	 * @return
	 */
	public ArrayList<ArrayList<String>> getAllRefChrSortings()
	{
		ArrayList<ArrayList<String>> toret=new ArrayList<ArrayList<String>>();
		for(ArrayList<String> al:this.rcs)
		{
			rcs.add(new ArrayList<String>(al));
		}
		return toret;
	}

	/**
	 * Get the sorting of a specific file
	 * @param fileindex
	 * @return
	 */
	public ArrayList<String> getRefChrSortingOfFile(int fileindex)
	{
		return new ArrayList<String>(this.rcs.get(fileindex));
	}




	/**
	 * test if the proposed reference sorting is consistent across all samples
	 * missing values either in the files or in the reference sorting are tolerated
	 * just the consistency of the rank is tested
	 * @return
	 */
	public boolean isSortingConsistentInAllFiles(ArrayList<String> totest)
	{
		// Get indexes of the final proposed reference sorting
		int counter=1;
		HashMap<String,Integer> sortIndex=new HashMap<String,Integer>();
		for(String s:totest)
		{
			sortIndex.put(s,counter);
			counter++;
		}


		// Check if all files agree with this sorting
		for(ArrayList<String>al:this.rcs)
		{
			int lastindex=0;
			for(String s: al)
			{
				if(sortIndex.containsKey(s))
				{
					int activeIndex=sortIndex.get(s);
					if(activeIndex<lastindex) return false;
					lastindex=activeIndex;
				}
			}
		}

		return true;
	}




}
