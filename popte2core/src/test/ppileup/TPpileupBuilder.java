package test.ppileup;
import corete.data.ppileup.PpileupBuilder;
import org.junit.Test;
import test.TestSupport.PpileupTestSupport;

import static org.junit.Assert.*;

/**
 * Created by robertkofler on 8/25/15.
 */


public class TPpileupBuilder {

	@Test
	public void Test_startconditions()
	{
		PpileupBuilder pp= PpileupTestSupport.get_simplePpBuilder();
		assertEquals(pp.requireChromosomeSwitch(),false);
		assertEquals(pp.eof(),false);
		assertEquals(pp.doneUntil(),-1);
	}

	@Test
	public void Test_simpleppileup()
	{
		//sb.append("r3\t99\t2L\t1\t162\t100M\t=\t200\t10\tGTG\t999\trc1\n");
		//sb.append("r3\t147\t2L\t200\t178\t100M\t=\t1\t-10\tTAA\t899\trc3\n");
		PpileupBuilder pp= PpileupTestSupport.get_simplePpBuilder();
		String chr=pp.switchChromosome();
		assertEquals(chr,"2L");
		assertEquals(pp.addRead(),true);
		assertEquals(pp.addRead(),false);

		//String s1=pp.getSite(150);
		//String s2=pp.getSite(199);
		assertEquals(pp.getSite(100),null);
		assertEquals(pp.getSite(101),"^.");
		assertEquals(pp.getSite(150),".");
		assertEquals(pp.getSite(199),".$");
		assertEquals(pp.getSite(200),null);
		assertEquals(pp.eof(),true);
		assertEquals(pp.requireChromosomeSwitch(),false);
	}

	@Test
	public void Test_pp2()
	{
		// 	sb.append("r3\t99\t2L\t1\t20\t100M\t=\t200\t10\tGTG\t999\trc1\n");
		//	sb.append("r3\t147\t2L\t200\t20\t100M\t=\t1\t-10\tTAA\t899\trc3\n");
		//	sb.append("r3\t99\t2L\t5\t19\t100M\t=\t205\t10\tGTG\t999\trc1\n");
		//	sb.append("r3\t147\t2L\t205\t19\t100M\t=\t5\t-10\tTAA\t899\trc3\n");
		//	sb.append("r3\t99\t2L\t10\t21\t100M\t=\t210\t10\tGTG\t999\trc1\n");
		//	sb.append("r3\t147\t2L\t210\t21\t100M\t=\t10\t-10\tTAA\t899\trc3\n");
		PpileupBuilder pp= PpileupTestSupport.get_simplePpBuildermq(20);
		String chr=pp.switchChromosome();
		assertEquals(chr,"2L");
		assertEquals(pp.addRead(),true);
		assertEquals(pp.addRead(),false);
		assertEquals(pp.addRead(),true);
		assertEquals(pp.addRead(),false);

		//String s1=pp.getSite(150);
		//String s2=pp.getSite(199);
		assertEquals(pp.getSite(100),null);
		assertEquals(pp.getSite(101),"^.");
		assertEquals(pp.getSite(110),".^.");
		assertEquals(pp.getSite(150),"..");
		assertEquals(pp.getSite(199),".$.");
		assertEquals(pp.getSite(200),".");
		assertEquals(pp.getSite(209),".$");
		assertEquals(pp.getSite(210),null);
		assertEquals(pp.eof(),true);
		assertEquals(pp.requireChromosomeSwitch(),false);
	}
	@Test
		 public void Test_pp3()
	{
		// 	sb.append("r3\t99\t2L\t1\t20\t100M\t=\t200\t10\tGTG\t999\trc1\n");
		//	sb.append("r3\t147\t2L\t200\t20\t100M\t=\t1\t-10\tTAA\t899\trc3\n");
		//	sb.append("r3\t99\t2L\t5\t19\t100M\t=\t205\t10\tGTG\t999\trc1\n");
		//	sb.append("r3\t147\t2L\t205\t19\t100M\t=\t5\t-10\tTAA\t899\trc3\n");
		//	sb.append("r3\t99\t2L\t10\t21\t100M\t=\t210\t10\tGTG\t999\trc1\n");
		//	sb.append("r3\t147\t2L\t210\t21\t100M\t=\t10\t-10\tTAA\t899\trc3\n");
		PpileupBuilder pp= PpileupTestSupport.get_simplePpBuildermq(19);
		String chr=pp.switchChromosome();
		assertEquals(chr,"2L");
		assertEquals(pp.addRead(),true);
		assertEquals(pp.addRead(),true);
		assertEquals(pp.addRead(),true);
		assertEquals(pp.addRead(),false);

		//String s1=pp.getSite(150);
		//String s2=pp.getSite(199);
		assertEquals(pp.getSite(100),null);
		assertEquals(pp.getSite(101),"^.");
		assertEquals(pp.getSite(110),"..^.");
		assertEquals(pp.getSite(150),"...");
		assertEquals(pp.getSite(199),".$..");
		assertEquals(pp.getSite(200),"..");
		assertEquals(pp.getSite(209),".$");
		assertEquals(pp.getSite(210),null);
		assertEquals(pp.eof(),true);
		assertEquals(pp.requireChromosomeSwitch(),false);
	}

	@Test
	public void Test_pp4()
	{
		// 	sb.append("r3\t99\t2L\t1\t20\t100M\t=\t200\t10\tGTG\t999\trc1\n");
		//	sb.append("r3\t147\t2L\t200\t20\t100M\t=\t1\t-10\tTAA\t899\trc3\n");
		//	sb.append("r3\t99\t2L\t5\t19\t100M\t=\t205\t10\tGTG\t999\trc1\n");
		//	sb.append("r3\t147\t2L\t205\t19\t100M\t=\t5\t-10\tTAA\t899\trc3\n");
		//	sb.append("r3\t99\t2L\t10\t21\t100M\t=\t210\t10\tGTG\t999\trc1\n");
		//	sb.append("r3\t147\t2L\t210\t21\t100M\t=\t10\t-10\tTAA\t899\trc3\n");
		PpileupBuilder pp= PpileupTestSupport.get_simplePpBuildermq(21);
		String chr=pp.switchChromosome();
		assertEquals(chr,"2L");
		assertEquals(pp.addRead(),false);
		assertEquals(pp.addRead(),false);
		assertEquals(pp.addRead(),true);
		assertEquals(pp.addRead(),false);

		//String s1=pp.getSite(150);
		//String s2=pp.getSite(199);

		assertEquals(pp.getSite(110),"^.");
		assertEquals(pp.getSite(150),".");
		assertEquals(pp.getSite(209),".$");
		assertEquals(pp.getSite(210),null);
		assertEquals(pp.eof(),true);
		assertEquals(pp.requireChromosomeSwitch(),false);
	}




}
