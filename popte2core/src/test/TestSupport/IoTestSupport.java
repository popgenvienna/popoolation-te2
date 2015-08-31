package test.TestSupport;

import corete.io.ppileup.PpileupLightwightReader;
import corete.io.SamPairReader;
import corete.io.SamReader;

import java.io.BufferedReader;
import java.io.StringReader;

/**
 * Created by robertkofler on 8/25/15.
 */
public class IoTestSupport {

	public static SamReader getSamReader()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("r1\t147\t2L\t302\t0\t101M\t=\t4639\t4337\tAAA\t128\tAS:i:97\n");
		sb.append("r2\t113\t2L\t302\t0\t101M\tp1\t5549\t0\tAAA\t888\tsome\n");
		sb.append("r3\t99\t4\t555\t162\t13S88M\t=\t719\t164\tGTG\t999\trc1\n");
		sb.append("r3\t147\t4\t719\t178\t101M\t=\t555\t-164\tTAA\t899\trc3\n");

		SamReader sr = new SamReader(new BufferedReader(new StringReader(sb.toString())));

		return sr;
	}


	public static SamPairReader getSamPairReader()
	{
		StringBuilder sb = new StringBuilder();
		// proper pair
		sb.append("r3\t99\t4\t555\t162\t13S88M\t=\t719\t164\tGTG\t999\trc1\n");
		sb.append("r3\t147\t4\t719\t178\t101M\t=\t555\t-164\tTAA\t899\trc3\n");

		// structural variant with some distracting reads in the middle
		sb.append("r4\t99\t4\t555\t162\t13S88M\t=\t1719\t1164\tGTG\t999\trc1\n");
		sb.append("r5\t113\t2L\t302\t0\t101M\t=\t134\t0\tAAA\t888\tsome\n");
		sb.append("r4\t147\t4\t1719\t178\t101M\t=\t555\t-1164\tTAA\t899\trc3\n");

		// tes
		sb.append("r6\t99\t2L\t301\t0\t101M\tp1\t134\t0\tAAA\t888\tsome\n");
		sb.append("r7\t147\t2L\t3001\t0\t101M\tr3\t134\t0\tAAA\t888\tsome\n");

		// overlapping proper pairs
		sb.append("r8\t99\t4\t1\t162\t101M\t=\t150\t150\tGTG\t999\tbla\n");
		sb.append("r9\t99\t4\t10\t162\t101M\t=\t160\t160\tGTG\t999\tbla\n");
		sb.append("r10\t99\t4\t10\t162\t101M\t=\t170\t170\tGTG\t999\tbla\n");
		sb.append("r8\t147\t4\t150\t178\t101M\t=\t1\t-150\tTAA\t899\tbla\n");
		sb.append("r9\t147\t4\t160\t178\t101M\t=\t10\t-160\tTAA\t899\tbla\n");
		sb.append("r10\t147\t4\t170\t178\t101M\t=\t10\t-170\tTAA\t899\tbla\n");

		SamPairReader spr=new SamPairReader(new BufferedReader(new StringReader(sb.toString())),DataTestSupport.getTEHierarchy(),1000);
	  return spr;
	}

	public static PpileupLightwightReader get_ppr_1()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(
				"@SC\tp\tP-element\n" +
				"@SC\tr\troo\n" +
				"@ID\t1\t69\n" +
				"@ID\t2\t128\n" +
				"@ID\t3\t60\n" +
				"@MQ\t15\n" +
				"@SR\t10000\n" +
				"@VN\tv0.11\n"+
		"2L\t1\t-\t.\tp\tr\n"+
		"2L\t2\tbla\t*\t><\t*\n"+
		"2L\t3\t-\t{r}{R}\t^.$r\t^{heleene}${schmarn}\n");
		return new PpileupLightwightReader(new BufferedReader(new StringReader(sb.toString())));


	}





}
