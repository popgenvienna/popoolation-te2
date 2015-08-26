package test.io;


import static org.junit.Assert.*;

import corete.data.SamRecord;
import corete.io.SamReader;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import test.TestSupport.DataTestSupport;
import test.TestSupport.IoTestSupport;

/**
 * Created by robertkofler on 8/25/15.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TSamReader {

	private static SamReader sr;


	@BeforeClass
	public static void inisamreader()
	{
		sr= IoTestSupport.getSamReader();
	}

	@Test
	public void Test_r1()
	{
		//	sb.append("r1\t147\t2L\t302\t0\t101M\t=\t4639\t4337\tAAA\t128\tAS:i:97\n");
		// http://broadinstitute.github.io/picard/explain-flags.html
		assertEquals(sr.hasNext(),true);
		SamRecord s=sr.next();
		assertEquals(s.getReadname(),"r1");
		assertEquals(s.getFlag(),147);
		assertEquals(s.getRefchr(),"2L");
		assertEquals(s.getStart(),302);
		assertEquals(s.getMapq(),0);
		assertEquals(s.getCigar(),"101M");
		assertEquals(s.getEnd(),402);
		assertEquals(s.getRefchrMate(),"2L");
		assertEquals(s.getStartMate(),4639);
		assertEquals(s.getSequence(),"AAA");
		assertEquals(s.getQual(),"128");
		assertEquals(s.getDistance(),4337);
		assertEquals(s.getComment(),"AS:i:97");

		// flags
		assertEquals(s.isUnmapped(),false);
		assertEquals(s.isUnmappedMate(),false);
		assertEquals(s.isForwardStrand(),false);

	}

	@Test
	public void Test_r2()
	{
		//	"r2\t113\t2L\t302\t0\t101M\tp1\t5549\t0\tAAA\t888\tsome\n"
		// http://broadinstitute.github.io/picard/explain-flags.html
		assertEquals(sr.hasNext(),true);
		SamRecord s=sr.next();
		assertEquals(s.getReadname(),"r2");
		assertEquals(s.getFlag(),113);
		assertEquals(s.getRefchr(),"2L");
		assertEquals(s.getStart(),302);
		assertEquals(s.getMapq(),0);
		assertEquals(s.getCigar(),"101M");
		assertEquals(s.getRefchrMate(),"p1");
		assertEquals(s.getStartMate(),5549);
		assertEquals(s.getSequence(),"AAA");
		assertEquals(s.getQual(),"888");
		assertEquals(s.getDistance(),0);
		assertEquals(s.getComment(),"some");


		// flags
		assertEquals(s.isUnmapped(),false);
		assertEquals(s.isUnmappedMate(),false);
		assertEquals(s.isForwardStrand(),false);

	}


	@Test
	public void Test_r3()
	{
		//	"r3\t99\t4\t555\t162\t13S88M\t=\t719\t164\tGTG\t999\trc1\n");
		// http://broadinstitute.github.io/picard/explain-flags.html
		assertEquals(sr.hasNext(),true);
		SamRecord s=sr.next();
		assertEquals(s.getReadname(),"r3");
		assertEquals(s.getFlag(),99);
		assertEquals(s.getRefchr(),"4");
		assertEquals(s.getStart(),555);

		assertEquals(s.getStartWithS(),542);
		assertEquals(s.getMapq(),162);
		assertEquals(s.getCigar(),"13S88M");
		assertEquals(s.getRefchrMate(),"4");
		assertEquals(s.getStartMate(),719);
		assertEquals(s.getSequence(),"GTG");
		assertEquals(s.getQual(),"999");
		assertEquals(s.getDistance(),164);
		assertEquals(s.getComment(),"rc1");


		// flags
		assertEquals(s.isUnmapped(),false);
		assertEquals(s.isUnmappedMate(),false);
		assertEquals(s.isForwardStrand(),true);
	}

	@Test
	public void Test_r4()
	{
		//	("r3\t147\t4\t719\t178\t101M\t=\t555\t-164\tTAA\t899\trc3\n")
		// http://broadinstitute.github.io/picard/explain-flags.html
		assertEquals(sr.hasNext(),true);
		SamRecord s=sr.next();
		assertEquals(s.getReadname(),"r3");
		assertEquals(s.getFlag(),147);
		assertEquals(s.getRefchr(),"4");
		assertEquals(s.getStart(),719);
		assertEquals(s.getStartWithS(),719);
		assertEquals(s.getMapq(),178);
		assertEquals(s.getCigar(),"101M");
		assertEquals(s.getRefchrMate(),"4");
		assertEquals(s.getStartMate(),555);
		assertEquals(s.getSequence(),"TAA");
		assertEquals(s.getQual(),"899");
		assertEquals(s.getDistance(),-164);
		assertEquals(s.getComment(),"rc3");


		// flags
		assertEquals(s.isUnmapped(),false);
		assertEquals(s.isUnmappedMate(),false);
		assertEquals(s.isForwardStrand(),false);

	}

	@Test
	public void Test_r9()
	{
		assertEquals(sr.hasNext(),false);
	}




}
