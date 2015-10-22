package test.data;

import corete.data.ppileup.PpileupChunk;
import corete.data.ppileup.PpileupSite;
import corete.io.ppileup.PpileupChunkReader;
import corete.misc.LogFactory;
import org.junit.Test;
import sun.rmi.runtime.Log;
import test.TestSupport.PpileupDebugReader;
import test.TestSupport.PpileupTestSupport;

/**
 * Created by robertkofler on 9/4/15.
 */
public class TPpileupChunkReader {

	@Test
	public void Test_chunk1() {
		StringBuilder sb=new StringBuilder();
		sb.append("2L\t1\tcom\t. 10 te 2\n");
		sb.append("2L\t2\tcom\t. 10 te 2\n");
		sb.append("2L\t10\tcom\t. 10 te 2\n");
		sb.append("2L\t11\tcom\t. 10 te 2\n");

		PpileupDebugReader dr=new PpileupDebugReader(sb.toString());
		PpileupChunkReader cr=new PpileupChunkReader(dr,2,2,10, LogFactory.getNullLogger());
		PpileupChunk c= cr.next();

		int bla=0;

	}

}
