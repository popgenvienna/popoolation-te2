package test.tesignature;


import corete.data.polyn.ContigwisePolynRepresentation;
import corete.data.polyn.PolyNRecord;
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
 * Created by robertkofler on 12/4/15.
 */
public class TContigwisePolynRepresentation {


	@Test
	public void Test_distance_nopolyn() {
		ArrayList<PolyNRecord> polyns=new ArrayList<PolyNRecord>();
		ContigwisePolynRepresentation pn=new ContigwisePolynRepresentation(polyns,"2L");

		assertEquals(pn.getContig(),"2L");
		assertEquals(pn.getDistance(1,10),10);
		assertEquals(pn.getDistance(10,1),-10);
		assertEquals(pn.getDistance(1,20),20);
		assertEquals(pn.getDistance(20,1),-20);
	}



	@Test
	public void Test_distance_withpolyn() {
		ArrayList<PolyNRecord> polyns=new ArrayList<PolyNRecord>();
		polyns.add(new PolyNRecord("2L",10,20));
		ContigwisePolynRepresentation pn=new ContigwisePolynRepresentation(polyns,"2L");


		assertEquals(pn.getContig(),"2L");
		assertEquals(pn.getDistance(1,10),9);
		assertEquals(pn.getDistance(10,1),-9);
		assertEquals(pn.getDistance(1,20),9);
		assertEquals(pn.getDistance(1,21),10);
		assertEquals(pn.getDistance(20,1),-9);
		assertEquals(pn.getDistance(21,1),-10);
		assertEquals(pn.getDistance(21,31),11);
		assertEquals(pn.getDistance(31,21),-11);


	}

	@Test
	public void Test_distance_withoverlappingpolyn() {
		ArrayList<PolyNRecord> polyns=new ArrayList<PolyNRecord>();
		polyns.add(new PolyNRecord("2L",10,20));
		polyns.add(new PolyNRecord("2L",15,25));
		ContigwisePolynRepresentation pn=new ContigwisePolynRepresentation(polyns,"2L");


		assertEquals(pn.getContig(),"2L");
		assertEquals(pn.getDistance(1,10),9);
		assertEquals(pn.getDistance(10,1),-9);
		assertEquals(pn.getDistance(1,26),10);
	}

	@Test
	public void Test_distance_concat() {
		ArrayList<PolyNRecord> polyns=new ArrayList<PolyNRecord>();
		polyns.add(new PolyNRecord("2L",10,17));
		polyns.add(new PolyNRecord("2L",18,20));
		ContigwisePolynRepresentation pn=new ContigwisePolynRepresentation(polyns,"2L");


		assertEquals(pn.getContig(),"2L");
		assertEquals(pn.getDistance(1,10),9);
		assertEquals(pn.getDistance(10,1),-9);
		assertEquals(pn.getDistance(1,20),9);
		assertEquals(pn.getDistance(1,21),10);
		assertEquals(pn.getDistance(20,1),-9);
		assertEquals(pn.getDistance(21,1),-10);
		assertEquals(pn.getDistance(21,31),11);
		assertEquals(pn.getDistance(31,21),-11);

	}


	@Test
	public void Test_outofboundary() {
		ArrayList<PolyNRecord> polyns=new ArrayList<PolyNRecord>();
		ContigwisePolynRepresentation pn=new ContigwisePolynRepresentation(polyns,"2L");

		assertEquals(pn.getDistance(1,10),10);
		assertEquals(pn.distanceOutsideBoundary(1,10,-9,19),false);
		assertEquals(pn.distanceOutsideBoundary(1,10,10,10),false);
		assertEquals(pn.distanceOutsideBoundary(1,10,9,9),true);
		assertEquals(pn.distanceOutsideBoundary(1,10,-10,9),true);
		assertEquals(pn.distanceOutsideBoundary(1,10,11,12),true);

	}


	@Test
	public void Test_outofboundaryreverse() {
		ArrayList<PolyNRecord> polyns=new ArrayList<PolyNRecord>();
		ContigwisePolynRepresentation pn=new ContigwisePolynRepresentation(polyns,"2L");

		assertEquals(pn.getDistance(10,1),-10);
		assertEquals(pn.distanceOutsideBoundary(10,1,-19,9),false);
		assertEquals(pn.distanceOutsideBoundary(10,1,-10,-10),false);
		assertEquals(pn.distanceOutsideBoundary(10,1,-9,-9),true);
		assertEquals(pn.distanceOutsideBoundary(10,1,-10,9),false);
		assertEquals(pn.distanceOutsideBoundary(10,1,-11,-12),true);

	}


	@Test
	public void Test_outofboundarypolyn() {
		ArrayList<PolyNRecord> polyns=new ArrayList<PolyNRecord>();
		polyns.add(new PolyNRecord("2L",10,20));
		ContigwisePolynRepresentation pn=new ContigwisePolynRepresentation(polyns,"2L");

		assertEquals(pn.getDistance(5,25),10);
		assertEquals(pn.distanceOutsideBoundary(5,25,5,25),false);
		assertEquals(pn.distanceOutsideBoundary(5,25,10,10),false);
		assertEquals(pn.distanceOutsideBoundary(5,25,9,9),true);
		assertEquals(pn.distanceOutsideBoundary(5,25,11,11),true);
		assertEquals(pn.distanceOutsideBoundary(5,25,11,30),true);

	}


	@Test
	public void Test_outofboundarypolynreverse() {
		ArrayList<PolyNRecord> polyns=new ArrayList<PolyNRecord>();
		polyns.add(new PolyNRecord("2L",10,20));
		ContigwisePolynRepresentation pn=new ContigwisePolynRepresentation(polyns,"2L");

		assertEquals(pn.getDistance(25,5),-10);
		assertEquals(pn.distanceOutsideBoundary(25,5,-25,-5),false);
		assertEquals(pn.distanceOutsideBoundary(25,5,-10,-10),false);
		assertEquals(pn.distanceOutsideBoundary(25,5,-9,-9),true);
		assertEquals(pn.distanceOutsideBoundary(25,5,-11,-11),true);
		assertEquals(pn.distanceOutsideBoundary(25,5,-11,-30),true);

	}



}
