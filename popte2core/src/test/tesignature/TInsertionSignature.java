package test.tesignature;

import corete.data.SignatureDirection;
import corete.data.TEStrand;
import corete.data.tesignature.InsertionSignature;
import org.junit.Test;
import test.TestSupport.DataTestSupport;
import test.TestSupport.PairupSupport;
import test.TestSupport.PpileupDebugReader;
import test.TestSupport.PpileupTestSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
/**
 * Created by robertkofler on 12/4/15.
 */
public class TInsertionSignature {

	@Test
	public void Test_directionawareposition(){


		InsertionSignature sig=PairupSupport.getSignaturue("1\tX\t100\t200\tF\tpele\t.\t0.1");
		assertEquals(sig.getDirectionAwarePosition(), 150);
		assertEquals(sig.getStart(),100);
		assertEquals(sig.getTefamily(),"pele");
		assertEquals(sig.getEnd(),200);
		assertEquals(sig.getChromosome(),"X");
		assertEquals(sig.getSignatureDirection(), SignatureDirection.Forward);
		assertEquals(sig.getTEStrand(), TEStrand.Unknown);
	}

	@Test
	public void Test_directionawareposition2(){


		InsertionSignature sig=PairupSupport.getSignaturue("1\tX\t200\t300\tR\tpele\t+\t0.1\n");
		assertEquals(sig.getDirectionAwarePosition(), 250);
		assertEquals(sig.getStart(),200);
		assertEquals(sig.getTefamily(),"pele");
		assertEquals(sig.getEnd(),300);
		assertEquals(sig.getChromosome(),"X");
		assertEquals(sig.getSignatureDirection(), SignatureDirection.Reverse);
		assertEquals(sig.getTEStrand(), TEStrand.Plus);
	}
}
