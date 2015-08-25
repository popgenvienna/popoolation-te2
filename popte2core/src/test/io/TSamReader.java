package test.io;


import static org.junit.Assert.*;

import corete.data.SamRecord;
import corete.io.SamReader;
import org.junit.BeforeClass;
import org.junit.Test;
import test.TestSupport.DataTestSupport;
import test.TestSupport.IoTestSupport;

/**
 * Created by robertkofler on 8/25/15.
 */
public class TSamReader {

	private SamReader sr;


	@BeforeClass
	public void inisamreader()
	{
		sr= IoTestSupport.getSamReader();
	}


	public void Test_r1()
	{
		//	sb.append("r1\t147\t2L\t302\t0\t101M\t=\t4639\t4337\tAAA\t128\tAS:i:97\n");
		assertEquals(sr.hasNext(),true);
		SamRecord s=sr.next();
		assertEquals(s.getReadname(),"r1");
		assertEquals(s.getFlag(),147);
		assertEquals(s.getRefchr(),"2L");
		assertEquals(s.getStart(),302);
		assertEquals(s.getMapq(),0);
		assertEquals(s.getCigar(),"101M");
		assertEquals(s.getRefchrMate(),"2L");
		assertEquals(s.getStartMate(),4639);
		assertEquals(s.getSequence(),"AAA");
		assertEquals(s.getQual(),"128");
		assertEquals(s.getDistance(),4337);
		assertEquals(s.ge);


	}





}
