package test.data;

import corete.data.ppileup.PpileupChunk;
import corete.data.ppileup.PpileupSampleSummary;
import corete.io.ppileup.PpileupChunkReader;
import corete.io.ppileup.PpileupChunkReaderRefined;
import corete.misc.LogFactory;
import org.junit.Test;
import test.TestSupport.PpileupDebugReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Created by robertkofler on 9/4/15.
 */
public class TPpileupChunkReaderRefined {
	private static ArrayList<Integer> ws=new ArrayList<Integer>(
			Arrays.asList(2, 2, 2));

	@Test
	public void Test_chunk1() {
		StringBuilder sb=new StringBuilder();
		sb.append("2L\t1\tcom\t. 10 te 2\n");
		sb.append("2L\t2\tcom\t. 10 te 2\n");

		PpileupDebugReader dr=new PpileupDebugReader(sb.toString());
		PpileupChunkReaderRefined cr=new PpileupChunkReaderRefined(dr,2,ws,10, LogFactory.getNullLogger());
		PpileupChunk c= cr.next();

		assertEquals(c.getStartPosition(),1);
		assertEquals(c.getEndPosition(),2);
		assertEquals(c.size(),1);
		assertEquals(c.siteCount(),2);
		assertEquals(c.getChromosome(),"2L");
		assertEquals(c.getShortcutsOfTEsInChunk().size(),1);
		assertTrue(c.getShortcutsOfTEsInChunk().contains("te"));
	}


	@Test
	public void Test_chunk2() {
		StringBuilder sb=new StringBuilder();
		sb.append("2L\t1\tcom\t. 1\tte 2\n");
		sb.append("2L\t2\tcom\tte 2\t. 1\n");

		PpileupDebugReader dr=new PpileupDebugReader(sb.toString());
		PpileupChunkReaderRefined cr=new PpileupChunkReaderRefined(dr,2,ws,10, LogFactory.getNullLogger());
		PpileupChunk c= cr.next();

		assertEquals(c.getStartPosition(),1);
		assertEquals(c.getEndPosition(),2);
		assertEquals(c.size(),2);
		assertEquals(c.siteCount(),2);
		assertEquals(c.getChromosome(),"2L");
		assertEquals(c.getShortcutsOfTEsInChunk().size(),1);
		assertTrue(c.getShortcutsOfTEsInChunk().contains("te"));
	}


	@Test
	public void Test_chunk3() {
		StringBuilder sb=new StringBuilder();
		sb.append("2L\t1\tcom\t. 10 te 2\n");
		sb.append("2L\t2\tcom\t. 10 te 2\n");
		sb.append("2L\t3\tcom\t. 10 te 2\n");

		PpileupDebugReader dr=new PpileupDebugReader(sb.toString());
		PpileupChunkReaderRefined cr=new PpileupChunkReaderRefined(dr,2,ws,10, LogFactory.getNullLogger());
		PpileupChunk c= cr.next();

		assertEquals(c.getStartPosition(),1);
		assertEquals(c.getEndPosition(),3);

	}




	@Test
	public void Test_chunk4() {
		StringBuilder sb=new StringBuilder();
		sb.append("2L\t1\tcom\t. 10 te 2\n");
		sb.append("2L\t2\tcom\t. 10 te 2\n");
		sb.append("2L\t3\tcom\t. 10 te 2\n");
		sb.append("2L\t4\tcom\t. 10 te 1\n");
		sb.append("2L\t5\tcom\t. 10 te 1\n");
		sb.append("2L\t6\tcom\t. 10\n");
		sb.append("2L\t7\tcom\t. 10\n");


		PpileupDebugReader dr=new PpileupDebugReader(sb.toString());
		PpileupChunkReaderRefined cr=new PpileupChunkReaderRefined(dr,2,ws,10, LogFactory.getNullLogger());
		PpileupChunk c= cr.next();

		assertEquals(c.getStartPosition(),1);
		assertEquals(c.getEndPosition(),7);
		c=cr.next();
		assertEquals(c,null);


	}






	@Test
	public void Test_chunk5() {
		StringBuilder sb=new StringBuilder();
		sb.append("2L\t1\tcom\t. 10 te 2\n");
		sb.append("2L\t2\tcom\t. 10 te 2\n");
		sb.append("2L\t12\tcom\t. 10 te 2\n");
		sb.append("2L\t13\tcom\t. 10 te 2\n");
		PpileupDebugReader dr=new PpileupDebugReader(sb.toString());
		PpileupChunkReaderRefined cr=new PpileupChunkReaderRefined(dr,2,ws,10, LogFactory.getNullLogger());
		PpileupChunk c= cr.next();


		assertEquals(c.getStartPosition(),1);
		assertEquals(c.getEndPosition(),13);
		assertEquals(c.siteCount(),4);
		c= cr.next();
		assertEquals(c, null);

	}


	@Test
	public void Test_chunk6() {
		StringBuilder sb=new StringBuilder();
		sb.append("2L\t1\tcom\t. 10 te 2\n");
		sb.append("2L\t2\tcom\t. 10 te 2\n");
		sb.append("2L\t13\tcom\t. 10 te 2\n");
		sb.append("2L\t14\tcom\t. 10 te 2\n");
		PpileupDebugReader dr=new PpileupDebugReader(sb.toString());           //new-pos - lastendpos > chunkdistance
		PpileupChunkReaderRefined cr=new PpileupChunkReaderRefined(dr,2,ws,10, LogFactory.getNullLogger());
		PpileupChunk c= cr.next();


		assertEquals(c.getStartPosition(),1);
		assertEquals(c.getEndPosition(),2);
		assertEquals(c.siteCount(),2);
		assertEquals(c.getChromosome(),"2L");
		c= cr.next();
		assertEquals(c.getStartPosition(),13);
		assertEquals(c.getEndPosition(),14);
		assertEquals(c.siteCount(),2);
		assertEquals(c.getChromosome(),"2L");

		c= cr.next();
		assertEquals(c,null);

	}


	@Test
	public void Test_chunk7() {
		StringBuilder sb=new StringBuilder();
		sb.append("2L\t1\tcom\t. 10 te 2\n");
		sb.append("2L\t2\tcom\t. 10 te 2\n");
		sb.append("2R\t1\tcom\t. 10 te 2\n");
		sb.append("2R\t2\tcom\t. 10 te 2\n");
		PpileupDebugReader dr=new PpileupDebugReader(sb.toString());
		PpileupChunkReaderRefined cr=new PpileupChunkReaderRefined(dr,2,ws,10, LogFactory.getNullLogger());
		PpileupChunk c= cr.next();


		assertEquals(c.getStartPosition(),1);
		assertEquals(c.getEndPosition(),2);
		assertEquals(c.siteCount(),2);
		assertEquals(c.getChromosome(),"2L");
		c= cr.next();
		assertEquals(c.getStartPosition(),1);
		assertEquals(c.getEndPosition(),2);
		assertEquals(c.siteCount(),2);
		assertEquals(c.getChromosome(),"2R");

		c= cr.next();
		assertEquals(c,null);

	}


	@Test
	public void Test_chunk7a() {
		StringBuilder sb=new StringBuilder();
		sb.append("2L\t1\tcom\t. 10 te 1\n");
		sb.append("2L\t2\tcom\t. 10 te 1\n");

		sb.append("2R\t1\tcom\t. 10 te 2\n");
		sb.append("2R\t2\tcom\t. 10 te 2\n");
		PpileupDebugReader dr=new PpileupDebugReader(sb.toString());
		PpileupChunkReaderRefined cr=new PpileupChunkReaderRefined(dr,2,ws,10, LogFactory.getNullLogger());
		PpileupChunk c= cr.next();
		assertEquals(c.getStartPosition(),1);
		assertEquals(c.getEndPosition(),2);
		assertEquals(c.siteCount(),2);
		assertEquals(c.getChromosome(),"2R");

		c= cr.next();
		assertEquals(c,null);

	}





	@Test
	public void Test_chunk8() {
		StringBuilder sb=new StringBuilder();
		sb.append("2L\t1\tcom\t. 10 te 2\n");
		sb.append("2L\t2\tcom\t. 10 te 2\n");
		sb.append("2R\t1\tcom\t. 10 te 2\n");
		sb.append("2R\t2\tcom\t. 10 te 2\n");
		sb.append("2R\t13\tcom\t. 10 te 2\n");
		sb.append("2R\t14\tcom\t. 10 te 2\n");

		PpileupDebugReader dr=new PpileupDebugReader(sb.toString());
		PpileupChunkReaderRefined cr=new PpileupChunkReaderRefined(dr,2,ws,10, LogFactory.getNullLogger());
		PpileupChunk c= cr.next();


		assertEquals(c.getStartPosition(),1);
		assertEquals(c.getEndPosition(),2);
		assertEquals(c.siteCount(),2);
		assertEquals(c.getChromosome(),"2L");
		c= cr.next();
		assertEquals(c.getStartPosition(),1);
		assertEquals(c.getEndPosition(),2);
		assertEquals(c.siteCount(),2);
		assertEquals(c.getChromosome(),"2R");

		c= cr.next();
		assertEquals(c.getStartPosition(),13);
		assertEquals(c.getEndPosition(),14);
		assertEquals(c.siteCount(),2);
		assertEquals(c.getChromosome(),"2R");
		c= cr.next();
		assertEquals(c,null);

	}





	@Test
	public void Test_chunk8longstrecth() {
		StringBuilder sb=new StringBuilder();
		sb.append("2L\t1\tcom\t. 10 te 1\n");
		sb.append("2L\t2\tcom\t. 10 te 1\n");
		sb.append("2L\t3\tcom\t. 10 te 2\n");
		sb.append("2L\t4\tcom\t. 10 te 2\n");
		sb.append("2L\t5\tcom\t. 10 te 1\n");
		sb.append("2L\t6\tcom\t. 10 te 1\n");
		sb.append("2L\t7\tcom\t. 10 te 1\n");
		sb.append("2L\t8\tcom\t. 10 te 1\n");
		sb.append("2L\t9\tcom\t. 10 te 1\n");
		sb.append("2L\t10\tcom\t. 10 te 2\n");
		sb.append("2L\t11\tcom\t. 10 te 2\n");
		sb.append("2L\t12\tcom\t. 10 te 1\n");
		sb.append("2L\t13\tcom\t. 10 te 1\n");
		sb.append("2L\t14\tcom\t. 10 te 1\n");
		sb.append("2L\t15\tcom\t. 10 te 1\n");
		sb.append("2L\t16\tcom\t. 10 te 1\n");
		sb.append("2L\t17\tcom\t. 10 te 2\n");
		sb.append("2L\t18\tcom\t. 10 te 2\n");
		sb.append("2L\t19\tcom\t. 10 te 1\n");
		sb.append("2L\t20\tcom\t. 10 te 1\n");

		PpileupDebugReader dr=new PpileupDebugReader(sb.toString());
		PpileupChunkReaderRefined cr=new PpileupChunkReaderRefined(dr,2,ws,5, LogFactory.getNullLogger());
		PpileupChunk c= cr.next();


		assertEquals(c.getStartPosition(),1);
		assertEquals(c.getEndPosition(),6);
		assertEquals(c.siteCount(),6);
		assertEquals(c.getChromosome(),"2L");
		c= cr.next();
		assertEquals(c.getStartPosition(),8);
		assertEquals(c.getEndPosition(),13);
		assertEquals(c.siteCount(),6);
		assertEquals(c.getChromosome(),"2L");

		c= cr.next();
		assertEquals(c.getStartPosition(),15);
		assertEquals(c.getEndPosition(),20);
		assertEquals(c.siteCount(),6);
		assertEquals(c.getChromosome(),"2L");
		c= cr.next();
		assertEquals(c,null);

	}

	@Test
	public void Test_chunk8longstretchtakefour() {
		StringBuilder sb=new StringBuilder();
		sb.append("2L\t1\tcom\t. 10 te 1\n");
		sb.append("2L\t2\tcom\t. 10 te 1\n");
		sb.append("2L\t3\tcom\t. 10 te 2\n");
		sb.append("2L\t4\tcom\t. 10 te 2\n");
		sb.append("2L\t5\tcom\t. 10 te 1\n");
		sb.append("2L\t6\tcom\t. 10 te 1\n");
		sb.append("2L\t7\tcom\t. 10 te 1\n");
		sb.append("2L\t8\tcom\t. 10 te 1\n");
		sb.append("2L\t9\tcom\t. 10 te 2\n");
		sb.append("2L\t10\tcom\t. 10 te 2\n");



		PpileupDebugReader dr=new PpileupDebugReader(sb.toString());
		PpileupChunkReaderRefined cr=new PpileupChunkReaderRefined(dr,2,ws,4, LogFactory.getNullLogger());
		PpileupChunk c= cr.next();


		assertEquals(c.getStartPosition(),1);
		assertEquals(c.getEndPosition(),6);
		assertEquals(c.siteCount(),6);
		assertEquals(c.getChromosome(),"2L");
		c= cr.next();
		assertEquals(c.getStartPosition(),7);
		assertEquals(c.getEndPosition(),10);
		assertEquals(c.siteCount(),4);
		assertEquals(c.getChromosome(),"2L");

		c= cr.next();
		assertEquals(c,null);

	}







	@Test
	public void Test_chunkhash() {
		StringBuilder sb=new StringBuilder();
		sb.append("2L\t1\tcom\t. 1 te 2\n");
		sb.append("2L\t2\tcom\t. 2 te 2\n");
		sb.append("2L\t12\tcom\t. 3 te 2\n");
		sb.append("2L\t13\tcom\t. 4 te 2\n");
		PpileupDebugReader dr=new PpileupDebugReader(sb.toString());
		PpileupChunkReaderRefined cr=new PpileupChunkReaderRefined(dr,2,ws,10, LogFactory.getNullLogger());
		PpileupChunk c= cr.next();
		HashMap<Integer,PpileupSampleSummary> t = c.getPSSTrack(0);
		assertEquals(t.get(1).getReverse().getCoverage(),3);
		assertEquals(t.get(2).getReverse().getCoverage(),4);
		assertEquals(t.get(3).getReverse().getCoverage(),0);
		assertEquals(t.get(11).getReverse().getCoverage(),0);
		assertEquals(t.get(12).getReverse().getCoverage(),5);
		assertEquals(t.get(13).getReverse().getCoverage(),6);
	}



	@Test
	public void Test_pooledTrack() {
		StringBuilder sb=new StringBuilder();
		sb.append("2L\t1\tcom\t. 1 te 2\t. 10 r 3\n");
		sb.append("2L\t2\tcom\t. 2 te 2\t. 10 r 5\n");
		sb.append("2L\t3\tcom\t. 3 te 2\t. 8 te 6\n");
		sb.append("2L\t4\tcom\t. 4 te 2\t. 9 te 4\n");
		PpileupDebugReader dr=new PpileupDebugReader(sb.toString());
		PpileupChunkReaderRefined cr=new PpileupChunkReaderRefined(dr,2,ws,10, LogFactory.getNullLogger());
		PpileupChunk c= cr.next();
		HashMap<Integer,PpileupSampleSummary> t= c.getPooledTrack();
		assertEquals(t.get(1).getReverse().getCoverage(),16);
		assertEquals((int)t.get(1).getReverse().getTecount().get("r") ,3);

		assertEquals(t.get(2).getReverse().getCoverage(),19);
		assertEquals(t.get(3).getReverse().getCoverage(),19);
		assertEquals(t.get(4).getReverse().getCoverage(),19);
		assertEquals((int)t.get(4).getReverse().getTecount().get("te") ,6);

	}



}
