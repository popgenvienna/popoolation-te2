package corete.data.hier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by robertkofler on 6/29/15.
 */
public class TEHierarchy{
	private final HashMap<String,HierarchyEntry> h;
	private final HashMap<String,String> fam2ord;

	public TEHierarchy(ArrayList<HierarchyEntry> entries)
	{
		HashMap<String,HierarchyEntry> tmp=new HashMap<String, HierarchyEntry>();
		fam2ord=new HashMap<String,String>();
		for(HierarchyEntry e: entries)
		{
			String fam=e.getFamily();
			String ord=e.getOrder();
			tmp.put(e.getID(),e);
			if(fam2ord.containsKey(fam))
			{
				if(!fam2ord.get(fam).equals(ord)) throw new IllegalArgumentException("Inconsistent TE hierarchy for family "+fam+" and order "+ord);
			}
			else
			{
				fam2ord.put(fam,ord);
			}


		}
		this.h=tmp;
	}


	/**
	 * Get the family for a TE of the given id
	 * @param id the id of a TE insertion
	 * @return the family
	 */
	public String getFamily(String id)
	{
		  if(h.containsKey(id))
		  {
			  return h.get(id).getFamily();
		  }
		else
		  {
			  throw new IllegalArgumentException("Do not recognize "+id);
		  }
	}

	/**
	 * Get the order for a TE of the given id
	 * @param id the id of a TE insertion
	 * @return the order
	 */
	public String getOrder(String id)
	{
		if(h.containsKey(id))
		{
			return h.get(id).getOrder();
		}
		else
		{
			throw new IllegalArgumentException("Do not recognize "+id);
		}
	}

	public int countFamilies()
	{
		HashSet<String> s=new HashSet<String>();
		for(HierarchyEntry he: this.h.values())
		{
			s.add(he.getFamily());
		}
		return s.size();
	}


	public int countIDs()
	{
		HashSet<String> s=new HashSet<String>();
		for(HierarchyEntry he: this.h.values())
		{
			s.add(he.getID());
		}
		return s.size();
	}

	public int countOrders()
	{
		HashSet<String> s=new HashSet<String>();
		for(HierarchyEntry he: this.h.values())
		{
			s.add(he.getOrder());
		}
		return s.size();
	}

	public String fam2ord(String fam)
	{
		return fam2ord.get(fam);
	}


	public boolean containsRefid(String id)
	{
		return h.containsKey(id);
	}




}
