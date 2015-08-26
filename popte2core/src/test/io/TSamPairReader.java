package test.io;

import corete.data.SamPair;
import corete.data.SamPairType;
import corete.data.SamRecord;
import corete.io.SamPairReader;
import corete.io.SamReader;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import test.TestSupport.IoTestSupport;

import static org.junit.Assert.assertEquals;

/**
 * Created by robertkofler on 8/25/15.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TSamPairReader {


	private static SamPairReader spr;


	@BeforeClass
	public static void inisamreader()
	{
		spr= IoTestSupport.getSamPairReader();
	}

	@Test
	public void Test_r1()
	{
		// sb.append("r3\t99\t4\t555\t162\t13S88M\t=\t719\t164\tGTG\t999\trc1\n");    fwd  555+88-1= 642 End=start+cigar-1
		// sb.append("r3\t147\t4\t719\t178\t101M\t=\t555\t-164\tTAA\t899\trc3\n");    rev  dist=719-642=77

		assertEquals(spr.hasNext(),true);
		SamPair s=spr.next();
		assertEquals(s.getSamPairType(), SamPairType.Pair);
		assertEquals(s.isProperPair(20),true);
		assertEquals(s.getFirstRead().getRefchr(),"4");
		assertEquals(s.getFirstRead().getStart(),555);
		assertEquals(s.getFirstRead().getReadname(),"r3");
		assertEquals(s.getSecondRead().getRefchr(),"4");
		assertEquals(s.getSecondRead().getStart(),719);
		assertEquals(s.getSecondRead().getReadname(),"r3");
		assertEquals(s.getInnerDistance(), 76);

	}

	@Test
	public void Test_r2()
	{
		//sb.append("r4\t99\t4\t555\t162\t13S88M\t=\t1719\t1164\tGTG\t999\trc1\n");
		//sb.append("r4\t147\t4\t1719\t178\t101M\t=\t555\t-1164\tTAA\t899\trc3\n");

		assertEquals(spr.hasNext(),true);
		SamPair s=spr.next();
		assertEquals(s.getSamPairType(), SamPairType.BrokenPair);
		assertEquals(s.isProperPair(20),false);
		assertEquals(s.getFirstRead().getRefchr(),"4");
		assertEquals(s.getFirstRead().getStart(),555);
		assertEquals(s.getFirstRead().isForwardStrand(),true);
		assertEquals(s.getSecondRead(),null);

	}


	@Test
	public void Test_r3()
	{
		//sb.append("r4\t99\t4\t555\t162\t13S88M\t=\t1719\t1164\tGTG\t999\trc1\n");
		//sb.append("r4\t147\t4\t1719\t178\t101M\t=\t555\t-1164\tTAA\t899\trc3\n");

		assertEquals(spr.hasNext(),true);
		SamPair s=spr.next();
		assertEquals(s.getSamPairType(), SamPairType.BrokenPair);
		assertEquals(s.isProperPair(20),false);
		assertEquals(s.getFirstRead().getRefchr(),"4");
		assertEquals(s.getFirstRead().getStart(),1719);
		assertEquals(s.getFirstRead().isForwardStrand(),false);
		assertEquals(s.getSecondRead(),null);
	}



	@Test
	public void Test_r4()
	{
		//sb.append("r6\t99\t2L\t301\t0\t101M\tp1\t134\t0\tAAA\t888\tsome\n");

		assertEquals(spr.hasNext(),true);
		SamPair s=spr.next();
		assertEquals(s.getSamPairType(), SamPairType.TEInsert);
		assertEquals(s.isProperPair(20),false);
		assertEquals(s.getFirstRead().getRefchr(),"2L");
		assertEquals(s.getFirstRead().getStart(),301);
		assertEquals(s.getFirstRead().isForwardStrand(),true);
		assertEquals(s.getFirstRead().getReadname(),"r6");
		assertEquals(s.getSecondRead(),null);
		assertEquals(s.getFamily(),"P-element");
		assertEquals(s.getOrder(),"DNA");
	}

	@Test
	public void Test_r5()
	{
		//"r7\t147\t2L\t3001\t0\t101M\tr3\t134\t0\tAAA\t888\tsome\n");

		assertEquals(spr.hasNext(),true);
		SamPair s=spr.next();
		assertEquals(s.getSamPairType(), SamPairType.TEInsert);
		assertEquals(s.isProperPair(20),false);
		assertEquals(s.getFirstRead().getRefchr(),"2L");
		assertEquals(s.getFirstRead().getStart(),3001);
		assertEquals(s.getFirstRead().isForwardStrand(),false);
		assertEquals(s.getFirstRead().getReadname(),"r7");
		assertEquals(s.getSecondRead(),null);
		assertEquals(s.getFamily(),"roo");
		assertEquals(s.getOrder(),"RNA");
	}


	@Test
	public void Test_r6()
	{
		//sb.append("r8\t99\t4\t1\t162\t101M\t=\t150\t150\tGTG\t999\tbla\n");
		//sb.append("r9\t99\t4\t10\t162\t101M\t=\t160\t160\tGTG\t999\tbla\n");
		//sb.append("r10\t99\t4\t10\t162\t101M\t=\t170\t170\tGTG\t999\tbla\n");
		//sb.append("r8\t147\t4\t150\t178\t101M\t=\t1\t-150\tTAA\t899\tbla\n");
		//sb.append("r9\t147\t4\t160\t178\t101M\t=\t10\t-160\tTAA\t899\tbla\n");
		//sb.append("r10\t147\t4\t170\t178\t101M\t=\t10\t-170\tTAA\t899\tbla\n");

		assertEquals(spr.hasNext(),true);
		SamPair s=spr.next();
		assertEquals(s.getSamPairType(), SamPairType.Pair);
		assertEquals(s.isProperPair(20),true);
		assertEquals(s.getFirstRead().getRefchr(),"4");
		assertEquals(s.getFirstRead().getStart(),1);
		assertEquals(s.getFirstRead().isForwardStrand(),true);
		assertEquals(s.getFirstRead().getReadname(),"r8");
		assertEquals(s.getSecondRead().isForwardStrand(),false);
		assertEquals(s.getSecondRead().getReadname(),"r8");
		assertEquals(s.getSecondRead().getStart(),150);
	}

	@Test
	public void Test_r7()
	{
		//sb.append("r8\t99\t4\t1\t162\t101M\t=\t150\t150\tGTG\t999\tbla\n");
		//sb.append("r9\t99\t4\t10\t162\t101M\t=\t160\t160\tGTG\t999\tbla\n");
		//sb.append("r10\t99\t4\t10\t162\t101M\t=\t170\t170\tGTG\t999\tbla\n");
		//sb.append("r8\t147\t4\t150\t178\t101M\t=\t1\t-150\tTAA\t899\tbla\n");
		//sb.append("r9\t147\t4\t160\t178\t101M\t=\t10\t-160\tTAA\t899\tbla\n");
		//sb.append("r10\t147\t4\t170\t178\t101M\t=\t10\t-170\tTAA\t899\tbla\n");

		assertEquals(spr.hasNext(),true);
		SamPair s=spr.next();
		assertEquals(s.getSamPairType(), SamPairType.Pair);
		assertEquals(s.isProperPair(20),true);
		assertEquals(s.getFirstRead().getRefchr(),"4");
		assertEquals(s.getFirstRead().getStart(),10);
		assertEquals(s.getFirstRead().isForwardStrand(),true);
		assertEquals(s.getFirstRead().getReadname(),"r9");
		assertEquals(s.getSecondRead().isForwardStrand(),false);
		assertEquals(s.getSecondRead().getReadname(),"r9");
		assertEquals(s.getSecondRead().getStart(),160);
	}

	@Test
	public void Test_r8()
	{
		//sb.append("r8\t99\t4\t1\t162\t101M\t=\t150\t150\tGTG\t999\tbla\n");
		//sb.append("r9\t99\t4\t10\t162\t101M\t=\t160\t160\tGTG\t999\tbla\n");
		//sb.append("r10\t99\t4\t10\t162\t101M\t=\t170\t170\tGTG\t999\tbla\n");
		//sb.append("r8\t147\t4\t150\t178\t101M\t=\t1\t-150\tTAA\t899\tbla\n");
		//sb.append("r9\t147\t4\t160\t178\t101M\t=\t10\t-160\tTAA\t899\tbla\n");
		//sb.append("r10\t147\t4\t170\t178\t101M\t=\t10\t-170\tTAA\t899\tbla\n");

		assertEquals(spr.hasNext(),true);
		SamPair s=spr.next();
		assertEquals(s.getSamPairType(), SamPairType.Pair);
		assertEquals(s.isProperPair(20),true);
		assertEquals(s.getFirstRead().getRefchr(),"4");
		assertEquals(s.getFirstRead().getStart(),10);
		assertEquals(s.getFirstRead().isForwardStrand(),true);
		assertEquals(s.getFirstRead().getReadname(),"r10");
		assertEquals(s.getSecondRead().isForwardStrand(),false);
		assertEquals(s.getSecondRead().getReadname(),"r10");
		assertEquals(s.getSecondRead().getStart(),170);
	}

	@Test
	public void Test_r9()
	{

		assertEquals(spr.hasNext(),false);

	}


}
