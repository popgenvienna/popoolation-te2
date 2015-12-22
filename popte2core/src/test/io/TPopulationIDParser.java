package test.io;

import corete.data.tesignature.PopulationID;
import corete.io.tesignature.PopolutionIDParser;
import org.junit.Test;
import test.TestSupport.IoTestSupport;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;



/**
 * Created by robertkofler on 12/22/15.
 */
public class TPopulationIDParser {


	@Test
	public void Test_SimpleToString()
	{


	  	String p = PopolutionIDParser.translateToString(new PopulationID(new ArrayList<Integer>(Arrays.asList(0))));
		assertEquals(p,"1");

		p = PopolutionIDParser.translateToString(new PopulationID(new ArrayList<Integer>(Arrays.asList(2))));
		assertEquals(p,"3");

		p = PopolutionIDParser.translateToString(new PopulationID(new ArrayList<Integer>(Arrays.asList(3))));
		assertEquals(p,"4");

	}

	@Test
	public void Test_TwoToString() {


		String p = PopolutionIDParser.translateToString(new PopulationID(new ArrayList<Integer>(Arrays.asList(0, 1))));
		assertEquals(p, "1,2");

		p = PopolutionIDParser.translateToString(new PopulationID(new ArrayList<Integer>(Arrays.asList(3, 4))));
		assertEquals(p, "4,5");

	}



	@Test
	public void Test_MultiString() {


		String p = PopolutionIDParser.translateToString(new PopulationID(new ArrayList<Integer>(Arrays.asList(0, 1,4))));
		assertEquals(p, "1,2,5");

		p = PopolutionIDParser.translateToString(new PopulationID(new ArrayList<Integer>(Arrays.asList(3, 4,5,10))));
		assertEquals(p, "4,5,6,11");

	}


	@Test
	public void Test_continuous() {


		String p = PopolutionIDParser.translateToString(new PopulationID(new ArrayList<Integer>(Arrays.asList(0, 1,2))));
		assertEquals(p, "1-3");

		p = PopolutionIDParser.translateToString(new PopulationID(new ArrayList<Integer>(Arrays.asList(2,3,4,5,6,7))));
		assertEquals(p, "3-8");

	}


	@Test
	public void Test_SimpleToPopID()
	{


		PopulationID p = PopolutionIDParser.getPopulationID("1");
		assertTrue(p.equals(new PopulationID(new ArrayList<Integer>(Arrays.asList(0))) ));


		p = PopolutionIDParser.getPopulationID("3");
		assertTrue(p.equals(new PopulationID(new ArrayList<Integer>(Arrays.asList(2))) ));

		p = PopolutionIDParser.getPopulationID("14");
		assertTrue(p.equals(new PopulationID(new ArrayList<Integer>(Arrays.asList(13))) ));


	}


	@Test
	public void Test_Two()
	{


		PopulationID p = PopolutionIDParser.getPopulationID("1,2");
		assertTrue(p.equals(new PopulationID(new ArrayList<Integer>(Arrays.asList(0,1))) ));


		p = PopolutionIDParser.getPopulationID("3,4");
		assertTrue(p.equals(new PopulationID(new ArrayList<Integer>(Arrays.asList(2,3))) ));

		p = PopolutionIDParser.getPopulationID("14,15");
		assertTrue(p.equals(new PopulationID(new ArrayList<Integer>(Arrays.asList(13,14))) ));


	}


	@Test
	public void Test_Range()
	{


		PopulationID p = PopolutionIDParser.getPopulationID("1-3");
		assertTrue(p.equals(new PopulationID(new ArrayList<Integer>(Arrays.asList(0,1,2))) ));


		p = PopolutionIDParser.getPopulationID("3-7");
		assertTrue(p.equals(new PopulationID(new ArrayList<Integer>(Arrays.asList(2,3,4,5,6))) ));

		p = PopolutionIDParser.getPopulationID("14-16");
		assertTrue(p.equals(new PopulationID(new ArrayList<Integer>(Arrays.asList(13,14,15))) ));

	}


	@Test
	public void Test_multichaos() {


		PopulationID p = PopolutionIDParser.getPopulationID("1-3,5,7-9");
		assertTrue(p.equals(new PopulationID(new ArrayList<Integer>(Arrays.asList(0, 1, 2,4,6,7,8)))));

	}








	}
