package test.data;

import corete.data.SignatureDirection;
import corete.data.TEStrand;
import corete.data.ppileup.PpileupChunk;
import corete.data.tesignature.Chunk2SignatureParser;
import corete.data.tesignature.InsertionSignature;
import corete.data.tesignature.SignatureFrequencyEstimationFramework;
import corete.io.ppileup.PpileupChunkReader;
import corete.misc.LogFactory;
import org.junit.Test;
import test.TestSupport.DataTestSupport;
import test.TestSupport.PpileupDebugReader;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by robertkofler on 11/12/15.
 */
public class TSignatureFrequencyEstimationFramework {

	@Test
	public void Test_chunk_simplefind() {
		StringBuilder sb=new StringBuilder();
		sb.append("@SC\tr\troo\n");
		sb.append("2L\t1\tcom\t. 10 r 2\n");
		sb.append("2L\t2\tcom\t. 10 r 2\n");

		PpileupDebugReader dr=new PpileupDebugReader(sb.toString());
		PpileupChunkReader cr=new PpileupChunkReader(dr,2,2,10, LogFactory.getNullLogger());
		PpileupChunk c =cr.next();
		Chunk2SignatureParser c2p=new Chunk2SignatureParser(c,2,2, DataTestSupport.getTETranslator_iniFull2Short()); // p, r, in, 4a
		ArrayList<InsertionSignature> a= c2p.getSignatures();
		SignatureFrequencyEstimationFramework sef=new SignatureFrequencyEstimationFramework(new PpileupDebugReader(sb.toString()),a,LogFactory.getNullLogger());

		ArrayList<InsertionSignature> f= sef.getSignaturesWithFrequencies();
		assertEquals(f.size(),a.size());
		assertEquals(f.get(0).getFrequencies().get(0).getCoverage(),12,0.000001);
		assertEquals(f.get(0).getFrequencies().get(0).getGivenTEInsertion(),2,0.000001);
		assertEquals(f.get(0).getFrequencies().get(0).getOtherTEinsertions() ,0,0.000001);
		assertEquals(f.get(0).getFrequencies().get(0).getStructuralRearrangements() ,0,0.000001);
	}


	@Test
	public void Test_chunk_multisample() {
		StringBuilder sb=new StringBuilder();
		sb.append("@SC\tr\troo\n");
		sb.append("@SC\tin\tIne-1\n");
		sb.append("2L\t1\tcom\t. 10 r 2\t. 9 r 3 > 4 in 5\n");
		sb.append("2L\t2\tcom\t. 10 r 2\t. 12 r 2 > 3 in 5\n");

		PpileupDebugReader dr=new PpileupDebugReader(sb.toString());
		PpileupChunkReader cr=new PpileupChunkReader(dr,2,2,10, LogFactory.getNullLogger());
		PpileupChunk c =cr.next();
		Chunk2SignatureParser c2p=new Chunk2SignatureParser(c,2,2, DataTestSupport.getTETranslator_iniFull2Short()); // p, r, in, 4a
		ArrayList<InsertionSignature> a= c2p.getSignatures();
		SignatureFrequencyEstimationFramework sef=new SignatureFrequencyEstimationFramework(new PpileupDebugReader(sb.toString()),a,LogFactory.getNullLogger());

		ArrayList<InsertionSignature> f= sef.getSignaturesWithFrequencies();
		assertEquals(f.size(),a.size());
		// roo first population
		assertEquals(f.get(0).getTefamily(),"roo");
		assertEquals(f.get(0).getFrequencies().get(0).getCoverage(),12,0.000001);
		assertEquals(f.get(0).getFrequencies().get(0).getGivenTEInsertion(),2,0.000001);
		assertEquals(f.get(0).getFrequencies().get(0).getOtherTEinsertions() ,0,0.000001);
		assertEquals(f.get(0).getFrequencies().get(0).getStructuralRearrangements() ,0,0.000001);


		// roo second population
		assertEquals(f.get(1).getTefamily(),"roo");
		assertEquals(f.get(1).getFrequencies().get(0).getCoverage(),21.5,0.000001);
		assertEquals(f.get(1).getFrequencies().get(0).getGivenTEInsertion(),2.5,0.000001);
		assertEquals(f.get(1).getFrequencies().get(0).getOtherTEinsertions(),5,0.000001);
		assertEquals(f.get(1).getFrequencies().get(0).getStructuralRearrangements() ,3.5,0.000001);

		// Ine-1
		assertEquals(f.get(2).getTefamily(),"Ine-1");
		assertEquals(f.get(2).getFrequencies().get(0).getCoverage(),21.5,0.000001);
		assertEquals(f.get(2).getFrequencies().get(0).getGivenTEInsertion(),5.0,0.000001);
		assertEquals(f.get(2).getFrequencies().get(0).getOtherTEinsertions(),2.5,0.000001);
		assertEquals(f.get(2).getFrequencies().get(0).getStructuralRearrangements() ,3.5,0.000001);

	}


}
