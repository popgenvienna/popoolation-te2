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
		assertEquals(pp.addRead(), true);
		assertEquals(pp.addRead(),false);

		//String s1=pp.getSite(150);
		//String s2=pp.getSite(199);
		assertEquals(pp.getSite(100),null);
		assertEquals(pp.getSite(101), "^.");
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

	@Test
	public void Test_pp_split()
	{
		// sb.append("r3\t99\t2L\t1\t20\t100M\t=\t300\t10\tGTG\t999\trc1\n");
		// sb.append("r3\t147\t2L\t300\t20\t100M\t=\t1\t-10\tTAA\t899\trc3\n");

		PpileupBuilder pp=PpileupTestSupport.get_PpB_split();
		String chr=pp.switchChromosome();
		assertEquals(chr,"2L");
		assertEquals(pp.addRead(),true);
		assertEquals(pp.addRead(),false);

		assertEquals(pp.getSite(100),null);
		assertEquals(pp.getSite(101),"^.");
		assertEquals(pp.getSite(150),".");
		assertEquals(pp.getSite(200),".$");
		assertEquals(pp.getSite(201),null);


		assertEquals(pp.getSite(299),null);
		assertEquals(pp.getSite(300),"^.");
		assertEquals(pp.getSite(350),".");
		assertEquals(pp.getSite(399),".$");
		assertEquals(pp.getSite(400), null);


	}

	@Test
	public void Test_pp_tepele()
	{

		//sb.append("r3\t99\t2L\t1\t20\t100M\tP-element\t400\t10\tGTG\t999\trc1\n");
		//sb.append("r4\t147\t2L\t400\t20\t100M\tP-element\t1\t-10\tTAA\t899\trc3\n");
		PpileupBuilder pp=PpileupTestSupport.get_PpB_tepele();
		String chr=pp.switchChromosome();
		assertEquals(chr,"2L");
		assertEquals(pp.addRead(),true);
		assertEquals(pp.addRead(),true);
		assertEquals(pp.addRead(),false);

		assertEquals(pp.getSite(100),null);
		assertEquals(pp.getSite(101),"^P");
		assertEquals(pp.getSite(150),"P");
		assertEquals(pp.getSite(200),"P$");
		assertEquals(pp.getSite(201),null);


		assertEquals(pp.getSite(299),null);
		assertEquals(pp.getSite(300),"^p");
		assertEquals(pp.getSite(350),"p");
		assertEquals(pp.getSite(399),"p$");
		assertEquals(pp.getSite(400),null);
	}

	@Test
	public void Test_pp_teine()
	{

		//sb.append("r3\t99\t2L\t1\t20\t100M\tP-element\t400\t10\tGTG\t999\trc1\n");
		//sb.append("r4\t147\t2L\t400\t20\t100M\tP-element\t1\t-10\tTAA\t899\trc3\n");
		PpileupBuilder pp=PpileupTestSupport.get_PpB_teine();
		String chr=pp.switchChromosome();
		assertEquals(chr,"2L");
		assertEquals(pp.addRead(),true);
		assertEquals(pp.addRead(),true);
		assertEquals(pp.addRead(),false);

		assertEquals(pp.getSite(100),null);
		assertEquals(pp.getSite(101),"^{IN}");
		assertEquals(pp.getSite(150),"{IN}");
		assertEquals(pp.getSite(200),"{IN}$");
		assertEquals(pp.getSite(201),null);


		assertEquals(pp.getSite(299),null);
		assertEquals(pp.getSite(300),"^{in}");
		assertEquals(pp.getSite(350),"{in}");
		assertEquals(pp.getSite(399),"{in}$");
		assertEquals(pp.getSite(400),null);
	}

	@Test
	public void Test_pp_peleine()
	{

		PpileupBuilder pp=PpileupTestSupport.get_PpB_te_peleine();
		String chr=pp.switchChromosome();
		assertEquals(chr,"2L");
		assertEquals(pp.addRead(),true);
		assertEquals(pp.addRead(),true);
		assertEquals(pp.addRead(),true);
		assertEquals(pp.addRead(),true);
		assertEquals(pp.addRead(),false);

		assertEquals(pp.getSite(100),null);
		assertEquals(pp.getSite(101),"^P^{IN}");
		assertEquals(pp.getSite(150),"P{IN}");
		assertEquals(pp.getSite(200),"P${IN}$");
		assertEquals(pp.getSite(201),null);


		assertEquals(pp.getSite(299),null);
		assertEquals(pp.getSite(300),"^p^{in}");
		assertEquals(pp.getSite(350),"p{in}");
		assertEquals(pp.getSite(399),"p${in}$");
		assertEquals(pp.getSite(400),null);
	}


	@Test
	public void Test_pp_sv()
	{

		PpileupBuilder pp=PpileupTestSupport.get_PpB_SV();
		String chr=pp.switchChromosome();
		assertEquals(chr,"2L");
		assertEquals(pp.addRead(),true);
		assertEquals(pp.addRead(),true);
		assertEquals(pp.addRead(),false);
		assertEquals(pp.requireChromosomeSwitch(),false);
		assertEquals(pp.eof(),true);

		assertEquals(pp.getSite(100),null);
		assertEquals(pp.getSite(101),"^>");
		assertEquals(pp.getSite(150),">");
		assertEquals(pp.getSite(200),">$");
		assertEquals(pp.getSite(201),null);


		assertEquals(pp.getSite(299),null);
		assertEquals(pp.getSite(300),"^<");
		assertEquals(pp.getSite(350),"<");
		assertEquals(pp.getSite(399),"<$");
		assertEquals(pp.getSite(400),null);
	}

	@Test
	public void Test_pp_chrswitch() {

		// sb.append("r3\t99\t2L\t1\t20\t100M\t2R\t400\t10\tGTG\t999\trc1\n");
		// sb.append("r4\t99\t2R\t1\t20\t100M\t2L\t1\t-10\tTAA\t899\trc3\n");
		PpileupBuilder pp = PpileupTestSupport.get_PpB_chrswitch();
		String chr = pp.switchChromosome();
		assertEquals(chr, "2L");
		assertEquals(pp.addRead(), true);
		assertEquals(pp.addRead(), false);
		assertEquals(pp.requireChromosomeSwitch(), true);
		assertEquals(pp.eof(), false);

		assertEquals(pp.getSite(100), null);
		assertEquals(pp.getSite(101), "^>");
		assertEquals(pp.getSite(150), ">");
		assertEquals(pp.getSite(200), ">$");
		assertEquals(pp.getSite(201), null);

		chr = pp.switchChromosome();
		assertEquals(chr, "2R");
		assertEquals(pp.addRead(), true);
		assertEquals(pp.addRead(), false);
		assertEquals(pp.requireChromosomeSwitch(), false);
		assertEquals(pp.eof(), true);

		assertEquals(pp.getSite(100), null);
		assertEquals(pp.getSite(101), "^>");
		assertEquals(pp.getSite(150), ">");
		assertEquals(pp.getSite(200), ">$");
		assertEquals(pp.getSite(201), null);

	}



}
