package test.data;

import corete.data.SignatureDirection;
import corete.data.TEFamilyShortcutTranslator;
import corete.data.TEStrand;
import corete.data.ppileup.PpileupChunk;
import corete.data.ppileup.PpileupSampleSummary;
import corete.data.ppileup.PpileupSite;
import corete.data.tesignature.Chunk2SignatureParser;
import corete.data.tesignature.InsertionSignature;
import corete.io.ppileup.PpileupChunkReader;
import corete.misc.LogFactory;
import org.junit.Test;
import sun.rmi.runtime.Log;
import test.TestSupport.DataTestSupport;
import test.TestSupport.PpileupDebugReader;
import test.TestSupport.PpileupTestSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Created by robertkofler on 9/4/15.
 */
public class TChunk2SignatureParser {
	private static ArrayList<Integer> ws=new ArrayList<Integer>(
			Arrays.asList(2, 2, 2));



	@Test
	public void Test_chunk_simplefind() {
		StringBuilder sb=new StringBuilder();
		sb.append("2L\t1\tcom\t. 10 r 2\n");
		sb.append("2L\t2\tcom\t. 10 r 2\n");

		PpileupDebugReader dr=new PpileupDebugReader(sb.toString());
		PpileupChunkReader cr=new PpileupChunkReader(dr,2,ws,10, LogFactory.getNullLogger());
		PpileupChunk c =cr.next();
		Chunk2SignatureParser c2p=new Chunk2SignatureParser(c,ws,2, DataTestSupport.getTETranslator_iniFull2Short()); // p, r, in, 4a
		ArrayList<InsertionSignature> a= c2p.getSignatures();

		assertEquals(a.size(),1);
		assertEquals(a.get(0).getChromosome(),"2L");
		assertEquals(a.get(0).getSignatureDirection(), SignatureDirection.Reverse);
		assertEquals(a.get(0).getTEStrand(), TEStrand.Unknown);
		assertEquals(a.get(0).getStart() ,1);
		assertEquals(a.get(0).getEnd() ,2);
		assertEquals(a.get(0).getTefamily() ,"roo");
	}


	@Test
	public void Test_chunk_findtwo() {
		StringBuilder sb=new StringBuilder();
		sb.append("2L\t1\tcom\t. 10 r 2\n");
		sb.append("2L\t2\tcom\t. 10 r 2\n");
		sb.append("2L\t3\tcom\t. 10 r 1\n");
		sb.append("2L\t4\tcom\t. 10 r 1\n");
		sb.append("2L\t5\tcom\t. 10 r 2\n");
		sb.append("2L\t6\tcom\t. 10 r 2\n");


		PpileupDebugReader dr=new PpileupDebugReader(sb.toString());
		PpileupChunkReader cr=new PpileupChunkReader(dr,2,ws,10, LogFactory.getNullLogger());
		PpileupChunk c =cr.next();
		Chunk2SignatureParser c2p=new Chunk2SignatureParser(c,ws,2, DataTestSupport.getTETranslator_iniFull2Short()); // p, r, in, 4a
		ArrayList<InsertionSignature> a= c2p.getSignatures();


		assertEquals(a.size(),2);
		assertEquals(a.get(0).getSignatureDirection(), SignatureDirection.Reverse);
		assertEquals(a.get(0).getTEStrand(), TEStrand.Unknown);
		assertEquals(a.get(0).getStart() ,1);
		assertEquals(a.get(0).getEnd() ,2);
		assertEquals(a.get(0).getTefamily() ,"roo");

		assertEquals(a.get(1).getSignatureDirection(), SignatureDirection.Reverse);
		assertEquals(a.get(1).getTEStrand(), TEStrand.Unknown);
		assertEquals(a.get(1).getStart() ,5);
		assertEquals(a.get(1).getEnd() ,6);
		assertEquals(a.get(1).getTefamily() ,"roo");

	}


	@Test
	public void Test_chunk_bridgedip() {
		StringBuilder sb = new StringBuilder();
		sb.append("2L\t1\tcom\t. 10 r 2\n");
		sb.append("2L\t2\tcom\t. 10 r 3\n");
		sb.append("2L\t3\tcom\t. 10 r 1\n");
		sb.append("2L\t4\tcom\t. 10 r 2\n");
		sb.append("2L\t5\tcom\t. 10 r 2\n");
		sb.append("2L\t6\tcom\t. 10 r 2\n");


		PpileupDebugReader dr = new PpileupDebugReader(sb.toString());
		PpileupChunkReader cr = new PpileupChunkReader(dr, 2, ws, 10, LogFactory.getNullLogger());
		PpileupChunk c = cr.next();
		Chunk2SignatureParser c2p = new Chunk2SignatureParser(c, ws, 2, DataTestSupport.getTETranslator_iniFull2Short()); // p, r, in, 4a
		ArrayList<InsertionSignature> a = c2p.getSignatures();


		assertEquals(a.size(), 1);
		assertEquals(a.get(0).getSignatureDirection(), SignatureDirection.Reverse);
		assertEquals(a.get(0).getTEStrand(), TEStrand.Unknown);
		assertEquals(a.get(0).getStart(), 1);
		assertEquals(a.get(0).getEnd(), 2);
		assertEquals(a.get(0).getTefamily(), "roo");
	}


	@Test
	public void Test_chunk_bridgedip_newpeak() {
		StringBuilder sb = new StringBuilder();
		sb.append("2L\t1\tcom\t. 10 r 2\n");
		sb.append("2L\t2\tcom\t. 10 r 3\n");
		sb.append("2L\t3\tcom\t. 10 r 1\n");
		sb.append("2L\t4\tcom\t. 10 r 2\n");
		sb.append("2L\t5\tcom\t. 10 r 4\n");
		sb.append("2L\t6\tcom\t. 10 r 2\n");


		PpileupDebugReader dr = new PpileupDebugReader(sb.toString());
		PpileupChunkReader cr = new PpileupChunkReader(dr, 2, ws, 10, LogFactory.getNullLogger());
		PpileupChunk c = cr.next();
		Chunk2SignatureParser c2p = new Chunk2SignatureParser(c, ws, 2, DataTestSupport.getTETranslator_iniFull2Short()); // p, r, in, 4a
		ArrayList<InsertionSignature> a = c2p.getSignatures();


		assertEquals(a.size(), 1);
		assertEquals(a.get(0).getSignatureDirection(), SignatureDirection.Reverse);
		assertEquals(a.get(0).getTEStrand(), TEStrand.Unknown);
		assertEquals(a.get(0).getStart(), 5);
		assertEquals(a.get(0).getEnd(), 6);
		assertEquals(a.get(0).getTefamily(), "roo");
	}


	@Test
	public void Test_chunk_multipeak() {
		StringBuilder sb=new StringBuilder();
		sb.append("2L\t1\tcom\t. 10 r 1 in 3 P 1\n");
		sb.append("2L\t2\tcom\t. 10 r 2 in 3 P 1\n");
		sb.append("2L\t3\tcom\t. 10 r 3 in 1 P 1\n");
		sb.append("2L\t4\tcom\t. 10 r 3 in 1 P 2\n");
		sb.append("2L\t5\tcom\t. 10 r 2 in 1 P 3\n");
		sb.append("2L\t6\tcom\t. 10 r 1 in 1 P 3\n");


		PpileupDebugReader dr=new PpileupDebugReader(sb.toString());
		PpileupChunkReader cr=new PpileupChunkReader(dr,2,ws,10, LogFactory.getNullLogger());
		PpileupChunk c =cr.next();
		Chunk2SignatureParser c2p=new Chunk2SignatureParser(c,ws,2, DataTestSupport.getTETranslator_iniFull2Short()); // p, r, in, 4a
		ArrayList<InsertionSignature> a= c2p.getSignatures();

		assertEquals(a.size(),3);
		assertEquals(a.get(0).getChromosome(),"2L");
		assertEquals(a.get(0).getSignatureDirection(), SignatureDirection.Forward);
		assertEquals(a.get(0).getTEStrand(), TEStrand.Unknown);
		assertEquals(a.get(0).getStart() ,5);
		assertEquals(a.get(0).getEnd() ,6);
		assertEquals(a.get(0).getTefamily() ,"P-element");


		assertEquals(a.get(1).getChromosome(),"2L");
		assertEquals(a.get(1).getSignatureDirection(), SignatureDirection.Reverse);
		assertEquals(a.get(1).getTEStrand(), TEStrand.Unknown);
		assertEquals(a.get(1).getStart() ,3);
		assertEquals(a.get(1).getEnd() ,4);
		assertEquals(a.get(1).getTefamily() ,"roo");

		assertEquals(a.get(2).getChromosome(),"2L");
		assertEquals(a.get(2).getSignatureDirection(), SignatureDirection.Reverse);
		assertEquals(a.get(2).getTEStrand(), TEStrand.Unknown);
		assertEquals(a.get(2).getStart() ,1);
		assertEquals(a.get(2).getEnd() ,2);
		assertEquals(a.get(2).getTefamily() ,"Ine-1");


	}

	@Test
	public void Test_chunk_solo() {
		// No solo allowed; must have min window length
		StringBuilder sb = new StringBuilder();
		sb.append("2L\t1\tcom\t. 10 r 4\n");



		PpileupDebugReader dr = new PpileupDebugReader(sb.toString());
		PpileupChunkReader cr = new PpileupChunkReader(dr, 2, ws, 10, LogFactory.getNullLogger());
		PpileupChunk c = cr.next();
		Chunk2SignatureParser c2p = new Chunk2SignatureParser(c, ws, 2, DataTestSupport.getTETranslator_iniFull2Short()); // p, r, in, 4a
		ArrayList<InsertionSignature> a = c2p.getSignatures();


		assertEquals(a.size(), 0);
	//	assertEquals(a.get(0).getSignatureDirection(), SignatureDirection.Reverse);
	//	assertEquals(a.get(0).getTEStrand(), TEStrand.Unknown);
	//	assertEquals(a.get(0).getStart(), 1);
	//	assertEquals(a.get(0).getEnd(), 1);
	//	assertEquals(a.get(0).getTefamily(), "roo");
	}

	@Test
	public void Test_chunk_lastfail() {
		StringBuilder sb = new StringBuilder();
		sb.append("2L\t1\tcom\t. 10 r 2\n");
		sb.append("2L\t2\tcom\t. 10 r 3\n");
		sb.append("2L\t3\tcom\t. 10 r 1\n");
		sb.append("2L\t4\tcom\t. 10 r 1\n");
		sb.append("2L\t6\tcom\t. 10 r 3\n");


		PpileupDebugReader dr = new PpileupDebugReader(sb.toString());
		PpileupChunkReader cr = new PpileupChunkReader(dr, 2, ws, 10, LogFactory.getNullLogger());
		PpileupChunk c = cr.next();
		Chunk2SignatureParser c2p = new Chunk2SignatureParser(c, ws, 2, DataTestSupport.getTETranslator_iniFull2Short()); // p, r, in, 4a
		ArrayList<InsertionSignature> a = c2p.getSignatures();


		assertEquals(a.size(), 1);
		assertEquals(a.get(0).getSignatureDirection(), SignatureDirection.Reverse);
		assertEquals(a.get(0).getTEStrand(), TEStrand.Unknown);
		assertEquals(a.get(0).getStart(), 1);
		assertEquals(a.get(0).getEnd(), 2);
		assertEquals(a.get(0).getTefamily(), "roo");
	}





}
