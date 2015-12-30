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
import static org.junit.Assert.assertFalse;
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

	@Test
		 public void Test_equals(){


		InsertionSignature sig1=PairupSupport.getSignaturue("1\tX\t200\t300\tR\tpele\t+\t0.1\n");
		InsertionSignature sig2=PairupSupport.getSignaturue("1\tX\t200\t300\tR\tpele\t+\t0.1\n");


		assertTrue(sig1.equals(sig2));

		 sig1=PairupSupport.getSignaturue("1\tX\t200\t300\tR\tpele\t+\t0.2\n");
		 sig2=PairupSupport.getSignaturue("1\tX\t200\t300\tR\tpele\t+\t0.1\n");


		assertTrue(sig1.equals(sig2));

	}

	@Test
	public void Test_ne(){


		InsertionSignature sig1=PairupSupport.getSignaturue("1\tX\t200\t300\tR\tpele\t+\t0.1\n");
		InsertionSignature sig2=PairupSupport.getSignaturue("2\tX\t200\t300\tR\tpele\t+\t0.1\n");
		assertFalse (sig1.equals(sig2));

		sig1=PairupSupport.getSignaturue("1\tX\t200\t300\tR\tpele\t+\t0.1\n");
		sig2=PairupSupport.getSignaturue("1\tX\t201\t300\tR\tpele\t+\t0.1\n");
		assertFalse (sig1.equals(sig2));

		sig1=PairupSupport.getSignaturue("1\tX\t200\t300\tR\tpele\t+\t0.1\n");
		sig2=PairupSupport.getSignaturue("1\tX\t200\t301\tR\tpele\t+\t0.1\n");
		assertFalse (sig1.equals(sig2));

		sig1=PairupSupport.getSignaturue("1\tX\t200\t300\tF\tpele\t+\t0.1\n");
		sig2=PairupSupport.getSignaturue("1\tX\t200\t300\tR\tpele\t+\t0.1\n");
		assertFalse (sig1.equals(sig2));

		sig1=PairupSupport.getSignaturue("1\tX\t200\t300\tF\tpele\t+\t0.1\n");
		sig2=PairupSupport.getSignaturue("1\tX\t200\t300\tF\troo\t+\t0.1\n");
		assertFalse (sig1.equals(sig2));

		sig1=PairupSupport.getSignaturue("1\tX\t200\t300\tF\tpele\t-\t0.1\n");
		sig2=PairupSupport.getSignaturue("1\tX\t200\t300\tF\tpele\t+\t0.1\n");
		assertFalse (sig1.equals(sig2));

	}



}
