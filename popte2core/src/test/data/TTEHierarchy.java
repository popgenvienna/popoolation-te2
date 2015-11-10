package test.data;
import corete.data.hier.TEHierarchy;
import static org.junit.Assert.*;
import org.junit.Test;
import test.TestSupport.DataTestSupport;


public class TTEHierarchy {

	@Test
	public void Test_pele_fam()
	{

		TEHierarchy h= DataTestSupport.getTEHierarchy();
		assertEquals(h.getFamily("p1"),"P-element");
		assertEquals(h.getFamily("p2"),"P-element");
		assertEquals(h.getFamily("p3"),"P-element");

	}

	@Test
	public void Test_pele_ord()
	{

		TEHierarchy h=DataTestSupport.getTEHierarchy();
		assertEquals(h.getOrder("p1"),"DNA");
		assertEquals(h.getOrder("p2"),"DNA");
		assertEquals(h.getOrder("p3"),"DNA");

	}

	@Test
	public void Test_roo_fam()
	{

		TEHierarchy h=DataTestSupport.getTEHierarchy();
		assertEquals(h.getFamily("r1"),"roo");
		assertEquals(h.getFamily("r2"),"roo");
		assertEquals(h.getFamily("r3"),"roo");

	}

	@Test
	public void Test_roo_ord()
	{

		TEHierarchy h=DataTestSupport.getTEHierarchy();
		assertEquals(h.getOrder("r1"),"RNA");
		assertEquals(h.getOrder("r2"),"RNA");
		assertEquals(h.getOrder("r3"),"RNA");

	}

	@Test
	public void Test_fam_count()
	{

		TEHierarchy h=DataTestSupport.getTEHierarchy();
		assertEquals(h.countFamilies(),2);
	}


	@Test
	public void Test_id_count()
	{

		TEHierarchy h=DataTestSupport.getTEHierarchy();
		assertEquals(h.countIDs(),6);
	}

	@Test
	public void Test_ord_count()
	{

		TEHierarchy h=DataTestSupport.getTEHierarchy();
		assertEquals(h.countOrders(),2);
	}

	@Test
	public void Test_fam2ord()
	{
		TEHierarchy h=DataTestSupport.getTEHierarchy();
		assertEquals(h.fam2ord("roo"),"RNA");
		assertEquals(h.fam2ord("P-element"),"DNA");
	}

	@Test
	public void Test_containsID()
	{
		TEHierarchy h=DataTestSupport.getTEHierarchy();
		assertTrue(h.containsRefid("p1"));
		assertTrue(h.containsRefid("p2"));
		assertTrue(h.containsRefid("p3"));
		assertTrue(h.containsRefid("r1"));
		assertTrue(h.containsRefid("r2"));
		assertTrue(h.containsRefid("r3"));

	}



}
