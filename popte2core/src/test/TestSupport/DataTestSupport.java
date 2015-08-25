package test.TestSupport;

import corete.data.hier.HierarchyEntry;
import corete.data.hier.TEHierarchy;

import java.util.ArrayList;

/**
 * Created by robertkofler on 8/25/15.
 */
public class DataTestSupport {

	public static TEHierarchy getTEHierarchy()
	{

		ArrayList<HierarchyEntry> hes=new ArrayList<HierarchyEntry>();
		hes.add(new HierarchyEntry("p1","P-element","DNA"));
		hes.add(new HierarchyEntry("p2","P-element","DNA"));
		hes.add(new HierarchyEntry("p3","P-element","DNA"));
		hes.add(new HierarchyEntry("r1","roo","RNA"));
		hes.add(new HierarchyEntry("r2","roo","RNA"));
		hes.add(new HierarchyEntry("r3","roo","RNA"));
		TEHierarchy hier=new TEHierarchy(hes);
		return hier;

	}

}
