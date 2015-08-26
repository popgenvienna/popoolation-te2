package test.data;

import corete.data.TEFamilyShortcutTranslator;
import org.junit.Test;
import test.TestSupport.DataTestSupport;
import corete.data.hier.TEHierarchy;
import static org.junit.Assert.*;



/**
 * Created by robertkofler on 8/26/15.
 */
public class TTEFamilyShortCutTranslator {
	//f2s.put("P-element","p");
	//f2s.put("roo","r");
	//f2s.put("Ine-1","in");
	//f2s.put("412","4a");

	@Test
	public void Test_f2sfwd(){
		TEFamilyShortcutTranslator tt = DataTestSupport.getTETranslator_iniFull2Short();

		assertEquals(tt.getShortcutFwd("P-element"),"P");
		assertEquals(tt.getShortcutFwd("roo"),"R");
		assertEquals(tt.getShortcutFwd("Ine-1"),"IN");
		assertEquals(tt.getShortcutFwd("412"),"4A");
	}

	@Test
	public void Test_f2srev(){
		TEFamilyShortcutTranslator tt = DataTestSupport.getTETranslator_iniFull2Short();

		assertEquals(tt.getShortcutRev("P-element"),"p");
		assertEquals(tt.getShortcutRev("roo"),"r");
		assertEquals(tt.getShortcutRev("Ine-1"),"in");
		assertEquals(tt.getShortcutRev("412"),"4a");
	}


	@Test
	public void Test_s2fs(){
		TEFamilyShortcutTranslator tt = DataTestSupport.getTETranslator_iniFull2Short();

		assertEquals(tt.getFullname("p"),"P-element");
		assertEquals(tt.getFullname("r"),"roo");
		assertEquals(tt.getFullname("in"),"Ine-1");
		assertEquals(tt.getFullname("4a"),"412");

	}

	@Test
	public void Test_s2fu(){
		TEFamilyShortcutTranslator tt = DataTestSupport.getTETranslator_iniFull2Short();

		assertEquals(tt.getFullname("P"),"P-element");
		assertEquals(tt.getFullname("R"),"roo");
		assertEquals(tt.getFullname("IN"),"Ine-1");
		assertEquals(tt.getFullname("4A"),"412");

	}

	@Test
	public void Test_s2ffwd(){
		TEFamilyShortcutTranslator tt = DataTestSupport.getTETranslator_iniFull2Short();

		assertEquals(tt.getShortcutFwd("P-element"),"P");
		assertEquals(tt.getShortcutFwd("roo"),"R");
		assertEquals(tt.getShortcutFwd("Ine-1"),"IN");
		assertEquals(tt.getShortcutFwd("412"),"4A");
	}
}
