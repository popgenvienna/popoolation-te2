package corete.data.stat;

import java.util.ArrayList;

/**
 * Created by robertkofler on 2/7/16.
 */
public class InformativeReadCountContainer {
	private final ArrayList<Integer> informativeReads;
			public InformativeReadCountContainer(ArrayList<Integer> informativeReads)
			{
				this.informativeReads=new ArrayList<Integer>(informativeReads);
			}


	public int minInformativeReadCount()
	{
		Integer toret=null;
		for(int i: this.informativeReads)
		{
			if(toret==null) toret=i;
			if(i<toret) toret=i;
		}
		return toret;
	}

	public int getInformativeReadCount(int index)
	{
		return this.informativeReads.get(index);
	}





}
