package corete.data.hier;

/**
 * Created by robertkofler on 6/29/15.
 */
public class HierarchyEntry {
	private final String id;
	private final String family;
	private final String order;

	public HierarchyEntry(String id, String family, String order)
	{
		this.id=id;
		this.family=family;
		this.order=order;
	}

	public String getID(){return this.id;}
	public String getFamily(){return this.family;}
	public String getOrder(){return this.order;}

}
