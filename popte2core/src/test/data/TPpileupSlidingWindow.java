package test.data;

import corete.data.ppileup.PpileupSite;
import corete.io.ppileup.PpileupSlidingWindow;
import org.junit.Test;
import test.TestSupport.PpileupDebugReader;
import test.TestSupport.PpileupTestSupport;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

/**
 * Created by robertkofler on 9/4/15.
 */
public class TPpileupSlidingWindow {


	@Test
	public void Test_s1() {
		StringBuilder sb=new StringBuilder();
		sb.append("2L\t1\tcom\t. 8 te 2\n");
		sb.append("2L\t2\tcom\t. 8 te 2\n");
		sb.append("2L\t3\tcom\t. 8 te 2\n");
		sb.append("2L\t11\tcom\t. 8 te 2\n");
		sb.append("2L\t12\tcom\t. 8 te 2\n");
		PpileupDebugReader dr=new PpileupDebugReader(sb.toString());
		PpileupSlidingWindow sw=new PpileupSlidingWindow(2);
		ArrayList<PpileupSite> s;

		s=sw.addSite(dr.next());
		assertEquals(s.size(),0);
		assertEquals(sw.averageMaxTEsupport(),1.0,0.00001);
		assertEquals(sw.getStartPosition(),1);
		assertEquals(sw.getEndPosition(),1);

	 	s= sw.addSite(dr.next());
		assertEquals(s.size(),0);
		assertEquals(sw.averageMaxTEsupport(),2.0,0.00001);
		assertEquals(sw.getStartPosition(),1);
		assertEquals(sw.getEndPosition(),2);


		s= sw.addSite(dr.next());
		assertEquals(s.size(),1);
		assertEquals(sw.averageMaxTEsupport(),2.0,0.00001);
		assertEquals(sw.getStartPosition(),2);
		assertEquals(sw.getEndPosition(),3);

		s=sw.flushWindow();
		assertEquals(s.size(),2);
		assertEquals(sw.averageMaxTEsupport(),0.0,0.00001);
	}


	@Test
	public void Test_s2() {
		StringBuilder sb=new StringBuilder();
		sb.append("2L\t1\tcom\t. 8 te 2\n");
		sb.append("2L\t2\tcom\t. 8 te 2\n");
		sb.append("2L\t11\tcom\t. 8 te 2\n");
		sb.append("2L\t12\tcom\t. 8 te 2\n");
		PpileupDebugReader dr=new PpileupDebugReader(sb.toString());
		PpileupSlidingWindow sw=new PpileupSlidingWindow(2);
		ArrayList<PpileupSite> s;

		s=sw.addSite(dr.next());
		assertEquals(s.size(),0);

		s= sw.addSite(dr.next());
		assertEquals(s.size(),0);


		s= sw.addSite(dr.next());
		assertEquals(s.size(),2);
		assertEquals(sw.averageMaxTEsupport(),1.0,0.00001);
		assertEquals(sw.getStartPosition(),11);
		assertEquals(sw.getEndPosition(),11);


		s= sw.addSite(dr.next());
		assertEquals(s.size(),0);
		assertEquals(sw.averageMaxTEsupport(),2.0,0.00001);
		assertEquals(sw.getStartPosition(),11);
		assertEquals(sw.getEndPosition(),12);

	}


}
