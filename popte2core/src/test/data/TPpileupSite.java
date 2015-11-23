package test.data;

import corete.data.ppileup.PpileupSite;
import org.junit.Test;
import test.TestSupport.PpileupTestSupport;

import static org.junit.Assert.assertEquals;

/**
 * Created by robertkofler on 9/4/15.
 */
public class TPpileupSite {

	@Test
	public void Test_ps1() {
		PpileupSite p = PpileupTestSupport.ppileupSiteFactory("2L\t10\tcom\t. 10 > 9 < 8 te 14");

		assertEquals(p.getPosition(), 10);
		assertEquals(p.getChromosome(), "2L");
		assertEquals(p.getComment(), "com");
		assertEquals(p.getMaxTESupport(), 14);
		assertEquals(p.getPpileupSampleSummary(0).getTEcount("te"), 14);
		assertEquals(p.getPpileupSampleSummary(0).getForward().getAbsence(), 10);
	}


	@Test
	public void Test_ps2() {
		PpileupSite p = PpileupTestSupport.ppileupSiteFactory("2L\t10\tcom\t. 10 > 9 < 8 te 14\t. 11 > 9 < 8 i 9 I 15");

		assertEquals(p.getPosition(), 10);
		assertEquals(p.getChromosome(), "2L");
		assertEquals(p.getComment(), "com");
		assertEquals(p.getMaxTESupport(), 15);
		assertEquals(p.getPpileupSampleSummary(0).getTEcount("te"), 14);
		assertEquals(p.getPpileupSampleSummary(0).getForward().getAbsence(), 10);
		assertEquals(p.getPpileupSampleSummary(0).getReverse().getAbsence(), 10);

		assertEquals(p.getPpileupSampleSummary(1).getTEcount("i"), 9);
		assertEquals(p.getPpileupSampleSummary(1).getForward().getAbsence(), 11);

	}

	@Test
	public void Test_fwd_rev_cov() {
		PpileupSite p = PpileupTestSupport.ppileupSiteFactory("2L\t10\tcom\t. 10 , 5 ' 4 te 2 TE 3");

		assertEquals(p.getPosition(), 10);
		assertEquals(p.getChromosome(), "2L");
		assertEquals(p.getComment(), "com");
		assertEquals(p.getMaxTESupport(), 3);
		assertEquals(p.getPpileupSampleSummary(0).getTEcount("te"),2);
		assertEquals(p.getPpileupSampleSummary(0).getTEcount("TE"),3);
		assertEquals(p.getPpileupSampleSummary(0).getForward().getAbsence(), 15);
		assertEquals(p.getPpileupSampleSummary(0).getReverse().getAbsence(), 14);
	}

}
