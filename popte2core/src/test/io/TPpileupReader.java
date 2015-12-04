package test.io;

import corete.data.TEFamilyShortcutTranslator;
import corete.data.ppileup.PpileupSiteLightwight;
import corete.data.stat.EssentialPpileupStats;
import corete.io.ppileup.PpileupLightwightReader;
import org.junit.Test;
import test.TestSupport.IoTestSupport;
import static org.junit.Assert.assertEquals;

/**
 * Created by robertkofler on 8/28/15.
 */
public class TPpileupReader {

	@Test
	public void Test_Shortcutreader()
	{
		//	"@SC\tp\tP-element\n" +
		// "@SC\tr\troo\n" +
		PpileupLightwightReader pp= IoTestSupport.get_ppr_1();
		TEFamilyShortcutTranslator tr=pp.getTEFamilyShortcutTranslator();
		assertEquals(tr.getFamilyname("p"),"P-element");
		assertEquals(tr.getFamilyname("r"),"roo");
		assertEquals(tr.getShortcutFwd("P-element"),"P");
		assertEquals(tr.getShortcutRev("P-element"),"p");

	}


	@Test
	public void Test_essentialstatreader()
	{
		//"@ID\t1\t69\n" +
		//"@ID\t2\t128\n" +
		//"@ID\t3\t60\n" +
		//"@MQ\t15\n" +
		//"@SR\t10000\n" +
		//"@VN\tv0.11"+
		PpileupLightwightReader pp= IoTestSupport.get_ppr_1();
		EssentialPpileupStats es=pp.getEssentialPpileupStats();
		assertEquals(es.getInnerDistance(0),69);
		assertEquals(es.getInnerDistance(1),128);
		assertEquals(es.getInnerDistance(2),60);
		assertEquals(es.getMinMapQual(),15);
		assertEquals(es.getInnerDistanceUpperQuantile(),0.01,0.0001);
		assertEquals(es.getStructuralRearrangementMinimumDistance(),10000);
		assertEquals(es.getVersionNumber(),"v0.11");
	}

	@Test
	public void Test_pp_1()
	{

		//"2L\t1\t-\t.\tp\tr\n"
		//"2L\t2\t-\t*\t><\t*\n"
		PpileupLightwightReader pp= IoTestSupport.get_ppr_1();
		PpileupSiteLightwight s=pp.next();
		assertEquals(s.getChromosome(),"2L");
		assertEquals(s.getComment(),"");
		assertEquals(s.getPosition(),1);
		assertEquals(s.getEntries(0).size(),1);
		assertEquals(s.getEntries(0).get(0),".");

		assertEquals(s.getEntries(1).size(),1);
		assertEquals(s.getEntries(1).get(0),"p");
		assertEquals(s.getEntries(2).size(),1);
		assertEquals(s.getEntries(2).get(0),"r");
	}

	@Test
	public void Test_pp_2()
	{

		//"2L\t1\t-\t.\tp\tr\n"
		//"2L\t2\tbla\t*\t><\t*\n"
		PpileupLightwightReader pp= IoTestSupport.get_ppr_1();
		PpileupSiteLightwight s=pp.next();
		s=pp.next();
		assertEquals(s.getChromosome(),"2L");
		assertEquals(s.getComment(),"bla");
		assertEquals(s.getPosition(),2);
		assertEquals(s.getEntries(0).size(),0);


		assertEquals(s.getEntries(1).size(),2);
		assertEquals(s.getEntries(1).get(0),">");
		assertEquals(s.getEntries(1).get(1),"<");
		assertEquals(s.getEntries(2).size(),0);

	}

	@Test
	public void Test_pp_3()
	{

		//"2L\t3\t-\t{r}{R}\t^.$r\t^{heleene}${schmarn}\n");
		PpileupLightwightReader pp= IoTestSupport.get_ppr_1();
		PpileupSiteLightwight s=pp.next();
		s=pp.next();
		s=pp.next();
		assertEquals(s.getChromosome(),"2L");
		assertEquals(s.getComment(),"");
		assertEquals(s.getPosition(),3);
		assertEquals(s.getEntries(0).size(),2);
		assertEquals(s.getEntries(0).get(0),"r");
		assertEquals(s.getEntries(0).get(1),"R");


		assertEquals(s.getEntries(1).size(),2);
		assertEquals(s.getEntries(1).get(0),".");
		assertEquals(s.getEntries(1).get(1),"r");

		assertEquals(s.getEntries(2).size(),2);
		assertEquals(s.getEntries(2).get(0),"heleene");
		assertEquals(s.getEntries(2).get(1),"schmarn");

	}

}
