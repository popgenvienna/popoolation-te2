package test.tesignature;


import corete.data.SignatureDirection;
import corete.data.TEStrand;
import corete.data.teinsertion.SignaturePairupFramework;
import corete.data.teinsertion.TEinsertion;
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
public class TSignaturePairupFramework {



	@Test
	public void Test_simple_pairup(){
		StringBuilder sb=new StringBuilder();
		sb.append("1\tX\t100\t200\tF\tpele\t.\t0.1\n");
		sb.append("1\tX\t200\t300\tR\tpele\t.\t0.1\n");
		SignaturePairupFramework spf=PairupSupport.getFramework(sb.toString(),PairupSupport.getEmpty(),0.5,-1,101);
		ArrayList<TEinsertion> tes= spf.getTEinsertions();
		assertEquals(tes.size(),1);
		assertEquals(tes.get(0).getPosition(),200);
		assertEquals(tes.get(0).getChromosome(),"X");
		assertEquals(tes.get(0).getStrand(), TEStrand.Unknown);
		assertEquals(tes.get(0).getSignature(),SignatureDirection.Both);
		assertEquals(tes.get(0).getFamily(),"pele");
		assertEquals(tes.get(0).getOrder(),"DNA");
		assertEquals(tes.get(0).getComment(),"");
		assertEquals(tes.get(0).getPopulationfrequencies().get(0),0.1,0.0001);
	}


	@Test
	public void Test_simple_failedpairdistancetolarge(){
		StringBuilder sb=new StringBuilder();
		sb.append("1\tX\t100\t200\tF\tpele\t.\t0.1\n");
		sb.append("1\tX\t200\t300\tR\tpele\t.\t0.1\n");
		SignaturePairupFramework spf=PairupSupport.getFramework(sb.toString(),PairupSupport.getEmpty(),0.5,-1,100);
		ArrayList<TEinsertion> tes= spf.getTEinsertions();
		assertEquals(tes.size(),2);
		assertEquals(tes.get(0).getPosition(),150);
		assertEquals(tes.get(1).getPosition(),250);
	}


	@Test
	public void Test_simple_failedpairdistancetosmall(){
		StringBuilder sb=new StringBuilder();
		sb.append("1\tX\t100\t200\tF\tpele\t.\t0.1\n");
		sb.append("1\tX\t110\t210\tR\tpele\t.\t0.1\n");
		SignaturePairupFramework spf=PairupSupport.getFramework(sb.toString(),PairupSupport.getEmpty(),0.5,12,100);
		ArrayList<TEinsertion> tes= spf.getTEinsertions();
		assertEquals(tes.size(),2);
		assertEquals(tes.get(0).getPosition(),150);
		assertEquals(tes.get(1).getPosition(),160);
	}



	@Test
	public void Test_simple_failedpairfreqdif(){
		StringBuilder sb=new StringBuilder();
		sb.append("1\tX\t100\t200\tF\tpele\t.\t0.1\n");
		sb.append("1\tX\t200\t300\tR\tpele\t.\t0.3\n");
		SignaturePairupFramework spf=PairupSupport.getFramework(sb.toString(),PairupSupport.getEmpty(),0.1,-1,101);
		ArrayList<TEinsertion> tes= spf.getTEinsertions();
		assertEquals(tes.size(),2);
		assertEquals(tes.get(0).getPosition(),150);
		assertEquals(tes.get(1).getPosition(),250);

	}

	@Test
	public void Test_simple_failedpairpopid(){
		StringBuilder sb=new StringBuilder();
		sb.append("1\tX\t100\t200\tF\tpele\t.\t0.1\n");
		sb.append("2\tX\t200\t300\tR\tpele\t.\t0.1\n");
		SignaturePairupFramework spf=PairupSupport.getFramework(sb.toString(),PairupSupport.getEmpty(),0.5,-1,101);
		ArrayList<TEinsertion> tes= spf.getTEinsertions();
		assertEquals(tes.size(),2);
		assertEquals(tes.get(0).getPosition(),150);
		assertEquals(tes.get(1).getPosition(),250);

	}

	@Test
	public void Test_simple_failedpairfamiliy(){
		StringBuilder sb=new StringBuilder();
		sb.append("1\tX\t100\t200\tF\tpele\t.\t0.1\n");
		sb.append("1\tX\t200\t300\tR\tpele2\t.\t0.1\n");
		SignaturePairupFramework spf=PairupSupport.getFramework(sb.toString(),PairupSupport.getEmpty(),0.5,-1,101);
		ArrayList<TEinsertion> tes= spf.getTEinsertions();
		assertEquals(tes.size(),2);
		assertEquals(tes.get(0).getPosition(),150);
		assertEquals(tes.get(1).getPosition(),250);

	}

	@Test
	public void Test_simple_failedpairchromosome(){
		StringBuilder sb=new StringBuilder();
		sb.append("1\tX\t100\t200\tF\tpele\t.\t0.1\n");
		sb.append("1\tX2\t200\t300\tR\tpele\t.\t0.1\n");
		SignaturePairupFramework spf=PairupSupport.getFramework(sb.toString(),PairupSupport.getEmpty(),0.5,-1,101);
		ArrayList<TEinsertion> tes= spf.getTEinsertions();
		assertEquals(tes.size(),2);
		assertEquals(tes.get(0).getPosition(),150);
		assertEquals(tes.get(1).getPosition(),250);

	}


	@Test
	public void Test_simple_failedpairsignaturedirection(){
		StringBuilder sb=new StringBuilder();
		sb.append("1\tX\t100\t200\tF\tpele\t.\t0.1\n");
		sb.append("1\tX\t200\t300\tF\tpele\t.\t0.1\n");
		SignaturePairupFramework spf=PairupSupport.getFramework(sb.toString(),PairupSupport.getEmpty(),0.5,-1,101);
		ArrayList<TEinsertion> tes= spf.getTEinsertions();
		assertEquals(tes.size(),2);
		assertEquals(tes.get(0).getPosition(),150);
		assertEquals(tes.get(1).getPosition(),250);

	}

	@Test
	public void Test_simple_failedpairtestrand(){
		StringBuilder sb=new StringBuilder();
		sb.append("1\tX\t100\t200\tF\tpele\t.\t0.1\n");
		sb.append("1\tX\t200\t300\tR\tpele\t+\t0.1\n");
		SignaturePairupFramework spf=PairupSupport.getFramework(sb.toString(),PairupSupport.getEmpty(),0.5,-1,101);
		ArrayList<TEinsertion> tes= spf.getTEinsertions();
		assertEquals(tes.size(),2);
		assertEquals(tes.get(0).getPosition(),150);
		assertEquals(tes.get(1).getPosition(),250);
	}

	@Test
	public void Test_complex_firstoftwo(){
		StringBuilder sb=new StringBuilder();
		sb.append("1\tX\t100\t200\tF\tpele\t.\t0.1\n");
		sb.append("1\tX\t200\t300\tR\tpele\t.\t0.1\n");
		sb.append("1\tX\t150\t250\tR\tpele\t.\t0.1\n");
		SignaturePairupFramework spf=PairupSupport.getFramework(sb.toString(),PairupSupport.getEmpty(),0.5,-1,101);
		ArrayList<TEinsertion> tes= spf.getTEinsertions();
		assertEquals(tes.size(),2);
		assertEquals(tes.get(0).getPosition(),175);
		assertEquals(tes.get(1).getPosition(),250);
	}


	@Test
	public void Test_complex_firstoftwoforward(){
		StringBuilder sb=new StringBuilder();
		sb.append("1\tX\t150\t250\tF\tpele\t.\t0.1\n");
		sb.append("1\tX\t100\t200\tF\tpele\t.\t0.1\n");
		sb.append("1\tX\t200\t300\tR\tpele\t.\t0.1\n");

		SignaturePairupFramework spf=PairupSupport.getFramework(sb.toString(),PairupSupport.getEmpty(),0.5,-1,101);
		ArrayList<TEinsertion> tes= spf.getTEinsertions();
		assertEquals(tes.size(),2);
		assertEquals(tes.get(0).getPosition(),225);
		assertEquals(tes.get(1).getPosition(),150);
	}



	@Test
	public void Test_complex_doubletrouble(){
		StringBuilder sb=new StringBuilder();
		sb.append("1\tX\t155\t157\tF\tpele\t.\t0.1\n");
		sb.append("1\tX\t160\t162\tF\tpele\t.\t0.1\n");
		sb.append("1\tX\t210\t212\tR\tpele\t.\t0.1\n");
		sb.append("1\tX\t200\t202\tR\tpele\t.\t0.1\n");


		SignaturePairupFramework spf=PairupSupport.getFramework(sb.toString(),PairupSupport.getEmpty(),0.5,-1,101);
		ArrayList<TEinsertion> tes= spf.getTEinsertions();
		assertEquals(tes.size(),2);
		assertEquals(tes.get(0).getPosition(),181);  // best pair should be the one with the lowest distance
		assertEquals(tes.get(1).getPosition(),183);  // than the second best pair
	}



	// chromosome, and signature direction, TEStrand
	// take the first of two pele
}
