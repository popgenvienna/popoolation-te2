package test.TestSupport;

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



}
