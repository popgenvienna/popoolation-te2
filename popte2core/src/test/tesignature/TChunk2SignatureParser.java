package test.tesignature;

import corete.data.SignatureDirection;
import corete.data.TEFamilyShortcutTranslator;
import corete.data.TEStrand;
import corete.data.ppileup.PpileupChunk;
import corete.data.ppileup.PpileupSampleSummary;
import corete.data.ppileup.PpileupSite;
import corete.data.tesignature.*;
import corete.io.ppileup.PpileupChunkReader;
import corete.misc.LogFactory;
import org.junit.Test;
import sun.rmi.runtime.Log;
import test.TestSupport.DataTestSupport;
import test.TestSupport.PpileupDebugReader;
import test.TestSupport.PpileupTestSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
		Chunk2SignatureParser c2p=new Chunk2SignatureParser(c,ws,ws,2, DataTestSupport.getTETranslator_iniFull2Short()); // p, r, in, 4a
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
		Chunk2SignatureParser c2p=new Chunk2SignatureParser(c,ws,ws,2, DataTestSupport.getTETranslator_iniFull2Short()); // p, r, in, 4a
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
	public void Test_chunk_findtwo_notanymore() {
		StringBuilder sb=new StringBuilder();
		sb.append("2L\t1\tcom\t. 10 r 2\n");
		sb.append("2L\t2\tcom\t. 10 r 2\n");
		sb.append("2L\t3\tcom\t. 10 r 1\n");
		sb.append("2L\t4\tcom\t. 10 r 1\n");
		sb.append("2L\t5\tcom\t. 10 r 2\n");
		sb.append("2L\t6\tcom\t. 10 r 2\n");


		ArrayList<Integer> valleys=new ArrayList<Integer>(
				Arrays.asList(3, 3, 3));
		PpileupDebugReader dr=new PpileupDebugReader(sb.toString());
		PpileupChunkReader cr=new PpileupChunkReader(dr,2,ws,10, LogFactory.getNullLogger());
		PpileupChunk c =cr.next();
		Chunk2SignatureParser c2p=new Chunk2SignatureParser(c,ws,valleys,2, DataTestSupport.getTETranslator_iniFull2Short()); // p, r, in, 4a
		ArrayList<InsertionSignature> a= c2p.getSignatures();


		assertEquals(a.size(),1);
		assertEquals(a.get(0).getSignatureDirection(), SignatureDirection.Reverse);
		assertEquals(a.get(0).getTEStrand(), TEStrand.Unknown);
		assertEquals(a.get(0).getStart() ,5);
		assertEquals(a.get(0).getEnd() ,6);
		assertEquals(a.get(0).getTefamily() ,"roo");



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
		Chunk2SignatureParser c2p = new Chunk2SignatureParser(c, ws, ws,2, DataTestSupport.getTETranslator_iniFull2Short()); // p, r, in, 4a
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
		Chunk2SignatureParser c2p = new Chunk2SignatureParser(c, ws,ws, 2, DataTestSupport.getTETranslator_iniFull2Short()); // p, r, in, 4a
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
		Chunk2SignatureParser c2p=new Chunk2SignatureParser(c,ws,ws,2, DataTestSupport.getTETranslator_iniFull2Short()); // p, r, in, 4a
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
		Chunk2SignatureParser c2p = new Chunk2SignatureParser(c, ws,ws, 2, DataTestSupport.getTETranslator_iniFull2Short()); // p, r, in, 4a
		ArrayList<InsertionSignature> a = c2p.getSignatures();


		assertEquals(a.size(), 0);
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
		Chunk2SignatureParser c2p = new Chunk2SignatureParser(c, ws,ws, 2, DataTestSupport.getTETranslator_iniFull2Short()); // p, r, in, 4a
		ArrayList<InsertionSignature> a = c2p.getSignatures();


		assertEquals(a.size(), 1);
		assertEquals(a.get(0).getSignatureDirection(), SignatureDirection.Reverse);
		assertEquals(a.get(0).getTEStrand(), TEStrand.Unknown);
		assertEquals(a.get(0).getStart(), 1);
		assertEquals(a.get(0).getEnd(), 2);
		assertEquals(a.get(0).getTefamily(), "roo");
	}


	@Test
	public void Test_chunk_simplerange() {
		StringBuilder sb=new StringBuilder();
		sb.append("2L\t1\tcom\t. 10 r 2\n");
		sb.append("2L\t2\tcom\t. 10 r 2\n");

		PpileupDebugReader dr=new PpileupDebugReader(sb.toString());
		PpileupChunkReader cr=new PpileupChunkReader(dr,2,ws,10, LogFactory.getNullLogger());
		PpileupChunk c =cr.next();
		Chunk2SignatureParser c2p=new Chunk2SignatureParser(c,ws,ws,2, DataTestSupport.getTETranslator_iniFull2Short()); // p, r, in, 4a
		ArrayList<SignatureRangeInfo> a= c2p.getRangeSignatures();

		assertEquals(a.size(),1);
		assertEquals(a.get(0).getRangeStart(),1);
		assertEquals(a.get(0).getRangeEnd(),2);
		assertEquals(a.get(0).getWinStartScore(),2.0,0.0001);
		assertEquals(a.get(0).getWinEndScore(),2.0,0.0001);

	}


	@Test
	public void Test_chunk_pyramidrange_extended() {
		StringBuilder sb=new StringBuilder();
		sb.append("2L\t1\tcom\t. 10 r 2\n");
		sb.append("2L\t2\tcom\t. 10 r 3\n");
		sb.append("2L\t3\tcom\t. 10 r 4\n");
		sb.append("2L\t4\tcom\t. 10 r 4\n");
		sb.append("2L\t5\tcom\t. 10 r 3\n");
		sb.append("2L\t6\tcom\t. 10 r 2\n");
		sb.append("2L\t7\tcom\t. 10 r 1\n");
		sb.append("2L\t8\tcom\t. 10 r 1\n");


		PpileupDebugReader dr=new PpileupDebugReader(sb.toString());
		PpileupChunkReader cr=new PpileupChunkReader(dr,2,ws,10, LogFactory.getNullLogger());
		PpileupChunk c =cr.next();
		Chunk2SignatureParser c2p=new Chunk2SignatureParser(c,ws,ws,2, DataTestSupport.getTETranslator_iniFull2Short()); // p, r, in, 4a
		ArrayList<SignatureRangeInfo> a= c2p.getRangeSignatures();

		assertEquals(a.size(),1);
		assertEquals(a.get(0).getRangeStart (),1);
		assertEquals(a.get(0).getRangeEnd (),6);
		assertEquals(a.get(0).getWinStartScore (),4.0,0.0001);
		assertEquals(a.get(0).getWinEndScore (),4.0,0.0001);

	}


	@Test
	public void Test_chunk_pyramidrange_abrupt() {
		StringBuilder sb=new StringBuilder();
		sb.append("2L\t1\tcom\t. 10 r 2\n");
		sb.append("2L\t2\tcom\t. 10 r 3\n");
		sb.append("2L\t3\tcom\t. 10 r 4\n");
		sb.append("2L\t4\tcom\t. 10 r 4\n");
		sb.append("2L\t5\tcom\t. 10 r 3\n");
		sb.append("2L\t6\tcom\t. 10 r 2\n");



		PpileupDebugReader dr=new PpileupDebugReader(sb.toString());
		PpileupChunkReader cr=new PpileupChunkReader(dr,2,ws,10, LogFactory.getNullLogger());
		PpileupChunk c =cr.next();
		Chunk2SignatureParser c2p=new Chunk2SignatureParser(c,ws,ws,2, DataTestSupport.getTETranslator_iniFull2Short()); // p, r, in, 4a
		ArrayList<SignatureRangeInfo> a= c2p.getRangeSignatures();

		assertEquals(a.size(),1);
		assertEquals(a.get(0).getRangeStart (),1);
		assertEquals(a.get(0).getRangeEnd (),6);
		assertEquals(a.get(0).getWinStartScore (),4.0,0.0001);
		assertEquals(a.get(0).getWinEndScore (),4.0,0.0001);

	}



	@Test
	public void Test_chunk_pyramidrange_complex() {
		StringBuilder sb=new StringBuilder();
		sb.append("2L\t1\tcom\t. 10 r 2\n");
		sb.append("2L\t2\tcom\t. 10 r 3\n");
		sb.append("2L\t3\tcom\t. 10 r 4\n");
		sb.append("2L\t4\tcom\t. 10 r 4\n");
		sb.append("2L\t5\tcom\t. 10 r 3\n");
		sb.append("2L\t6\tcom\t. 10 r 2\n");
		sb.append("2L\t7\tcom\t. 10 r 1\n");
		sb.append("2L\t8\tcom\t. 10 r 1\n");
		sb.append("2L\t9\tcom\t. 10 r 2\n");
		sb.append("2L\t10\tcom\t. 10 r 3\n");
		sb.append("2L\t11\tcom\t. 10 r 4\n");
		sb.append("2L\t12\tcom\t. 10 r 4\n");
		sb.append("2L\t13\tcom\t. 10 r 3\n");
		sb.append("2L\t14\tcom\t. 10 r 2\n");



		PpileupDebugReader dr=new PpileupDebugReader(sb.toString());
		PpileupChunkReader cr=new PpileupChunkReader(dr,2,ws,10, LogFactory.getNullLogger());
		PpileupChunk c =cr.next();
		Chunk2SignatureParser c2p=new Chunk2SignatureParser(c,ws,ws,2, DataTestSupport.getTETranslator_iniFull2Short()); // p, r, in, 4a
		ArrayList<SignatureRangeInfo> a= c2p.getRangeSignatures();

		assertEquals(a.size(),2);
		assertEquals(a.get(0).getRangeStart (),1);
		assertEquals(a.get(0).getRangeEnd (),6);
		assertEquals(a.get(0).getWinStartScore (),4.0,0.0001);
		assertEquals(a.get(0).getWinEndScore (),4.0,0.0001);
		assertEquals(a.get(0).getSignature().getStart(),3);
		assertEquals(a.get(0).getSignature().getEnd(),4);

		assertEquals(a.get(1).getRangeStart (),9);
		assertEquals(a.get(1).getRangeEnd (),14);
		assertEquals(a.get(1).getWinStartScore (),4.0,0.0001);
		assertEquals(a.get(1).getWinEndScore (),4.0,0.0001);
		assertEquals(a.get(1).getSignature().getStart(),11);
		assertEquals(a.get(1).getSignature().getEnd(),12);


	}


	@Test
		 public void Test_chunk_joint_unaltered() {
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
		Chunk2SignatureParser c2p=new Chunk2SignatureParser(c,ws,ws,2, DataTestSupport.getTETranslator_iniFull2Short()); // p, r, in, 4a
		ArrayList<SignatureRangeInfo> sris= c2p.getRangeSignatures();
		SampleChunk2SignatureParser sc2p=new SampleChunk2SignatureParser(c.getPooledTrack(),c.getChromosome(),c.getStartPosition(),c.getEndPosition(),DataTestSupport.getTETranslator_iniFull2Short());
		RefinementChunk2SignatureParser rcp=new RefinementChunk2SignatureParser(sc2p,sris,c.getChromosome(),c.getStartPosition(),c.getEndPosition(),ws.get(0),ws.get(0),DataTestSupport.getTETranslator_iniFull2Short());
		ArrayList<InsertionSignature> a=rcp.getSignatures();
		Collections.sort(a);

		assertEquals(sris.size(), 2);
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
	public void Test_chunk_joint_altered() {
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
		Chunk2SignatureParser c2p=new Chunk2SignatureParser(c,ws,ws,2, DataTestSupport.getTETranslator_iniFull2Short()); // p, r, in, 4a
		ArrayList<SignatureRangeInfo> sris= c2p.getRangeSignatures();




		StringBuilder sbr=new StringBuilder();
		sbr.append("2L\t1\tcom\t. 10 r 2\n");
		sbr.append("2L\t2\tcom\t. 10 r 3\n");
		sbr.append("2L\t3\tcom\t. 10 r 4\n");
		sbr.append("2L\t4\tcom\t. 10 r 4\n");
		sbr.append("2L\t5\tcom\t. 10 r 3\n");
		sbr.append("2L\t6\tcom\t. 10 r 2\n");


		PpileupDebugReader drr=new PpileupDebugReader(sbr.toString());
		PpileupChunkReader crr=new PpileupChunkReader(drr,2,ws,10, LogFactory.getNullLogger());
		PpileupChunk pcr =crr.next();



		SampleChunk2SignatureParser sc2p=new SampleChunk2SignatureParser(pcr.getPooledTrack(),pcr.getChromosome(),pcr.getStartPosition(),pcr.getEndPosition(),DataTestSupport.getTETranslator_iniFull2Short());
		RefinementChunk2SignatureParser rcp=new RefinementChunk2SignatureParser(sc2p,sris,pcr.getChromosome(),pcr.getStartPosition(),pcr.getEndPosition(),ws.get(0),ws.get(0),DataTestSupport.getTETranslator_iniFull2Short());
		ArrayList<InsertionSignature> a=rcp.getSignatures();

		assertEquals(sris.size(),2);
		assertEquals(a.size(),1);

		assertEquals(a.get(0).getSignatureDirection(), SignatureDirection.Reverse);
		assertEquals(a.get(0).getTEStrand(), TEStrand.Unknown);
		assertEquals(a.get(0).getStart() ,3);
		assertEquals(a.get(0).getEnd() ,4);
		assertEquals(a.get(0).getTefamily() ,"roo");
	}




}
