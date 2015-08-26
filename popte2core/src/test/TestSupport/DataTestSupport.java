package test.TestSupport;

import corete.data.TEFamilyShortcutTranslator;
import corete.data.hier.HierarchyEntry;
import corete.data.hier.TEHierarchy;

import java.util.ArrayList;
import java.util.HashMap;

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

	public static TEFamilyShortcutTranslator getTETranslator_iniFull2Short()
	{
		HashMap<String,String> f2s=new HashMap<String,String>();
		f2s.put("P-element","p");
		f2s.put("roo","r");
		f2s.put("Ine-1","in");
		f2s.put("412","4a");

		TEFamilyShortcutTranslator tt= TEFamilyShortcutTranslator.getFull2ShortTranslator(f2s);
		return tt;
	}

	public static TEFamilyShortcutTranslator getTETranslator_iniShort2Full()
	{
		HashMap<String,String> f2s=new HashMap<String,String>();
		f2s.put("p","P-element");
		f2s.put("r","roo");
		f2s.put("in","Ine-1");
		f2s.put("4a","412");

		TEFamilyShortcutTranslator tt= TEFamilyShortcutTranslator.getShort2FullTranslator(f2s);
		return tt;
	}

}
