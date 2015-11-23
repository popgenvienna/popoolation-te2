package test.data;

import corete.data.TEFamilyShortcutTranslator;
import corete.data.ppileup.PpileupSampleSummary;
import org.junit.Test;
import test.TestSupport.DataTestSupport;
import test.TestSupport.PpileupTestSupport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by robertkofler on 9/4/15.
 */
public class TPpileupSampleSummary {


	@Test
	public void Test_pss1(){
		PpileupSampleSummary p= PpileupTestSupport.ppileupSampleSummaryFactory(". 10 > 9 < 8 te 14");
		assertEquals(p.getForward().getAbsence(),10);
		assertEquals(p.getForward ().getStructuralRearrangement(),9);
		assertEquals(p.getReverse ().getStructuralRearrangement(),8);
		assertEquals(p.getForward ().getCoverage(),19);
		assertEquals(p.getReverse ().getCoverage(),32);
		assertEquals(p.getReverse().getTEcount("te") ,14);
		assertEquals(p.getForward().getTEcount("te"),0);
		assertEquals(p.getTEComplement().size(),1);
		assertTrue(p.getTEComplement().contains("te"));
		assertEquals(p.maxTESupport(),14);

	}

	@Test
	public void Test_pss2(){
		PpileupSampleSummary p= PpileupTestSupport.ppileupSampleSummaryFactory(". 0 > 0 < 0 i 9 I 11 ro 12 RO 8");
		assertEquals(p.getForward().getAbsence(),0);
		assertEquals(p.getForward().getStructuralRearrangement(),0);
		assertEquals(p.getReverse().getStructuralRearrangement(),0);


		assertEquals(p.getForward().getCoverage(),19);
		assertEquals(p.getReverse().getCoverage(),21);
		assertEquals(p.getReverse().getTEcount("i") ,9);
		assertEquals(p.getForward().getTEcount("i") ,0);
		assertEquals(p.getForward().getTEcount("I"),11);
		assertEquals(p.getReverse().getTEcount("ro") ,12);
		assertEquals(p.getForward().getTEcount("RO"),8);
		assertEquals(p.getForward().getTEcount("nonsense") ,0);
		assertEquals(p.getReverse().getTEcount("k"),0);

		assertEquals(p.getTEComplement().size(),4);
		assertTrue(p.getTEComplement().contains("i"));
		assertTrue(p.getTEComplement().contains("I"));
		assertTrue(p.getTEComplement().contains("ro"));
		assertTrue(p.getTEComplement().contains("RO"));

		assertEquals(p.maxTESupport(),12);
	}

	@Test
	public void Test_pss_default(){
		PpileupSampleSummary p= PpileupSampleSummary.getEmpty();
		assertEquals(p.getForward().getAbsence(),0);
		assertEquals(p.getReverse().getAbsence(),0);
		assertEquals(p.getForward().getStructuralRearrangement(),0);
		assertEquals(p.getReverse().getStructuralRearrangement(),0);
		assertEquals(p.getForward().getCoverage(),0);
		assertEquals(p.getReverse().getCoverage(),0);
		assertEquals(p.getForward().getTEcount("te") ,0);
		assertEquals(p.getReverse().getTEcount("te") ,0);
		assertEquals(p.getForward().getTEcount("hm"),0);
		assertEquals(p.getTEComplement().size(),0);
		assertTrue(!p.getTEComplement().contains("te"));
		assertEquals(p.maxTESupport(),0);


	}

}
