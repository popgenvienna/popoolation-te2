package corete.data.hier;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by robertkofler on 6/29/15.
 */
public class TEHierarchy{
	private final HashMap<String,HierarchyEntry> h;

	public TEHierarchy(HashMap<String,HierarchyEntry> hier)
	{
		     h=new HashMap<String, HierarchyEntry>(hier);
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

	public boolean containsRefid(String id)
	{
		return h.containsKey(id);
	}




}
