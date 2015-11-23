package test.TestSupport;

import corete.data.SignatureDirection;
import corete.data.hier.HierarchyEntry;
import corete.data.hier.TEHierarchy;
import corete.data.ppileup.*;
import corete.io.SamPairReader;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by robertkofler on 8/25/15.
 */
public class PpileupTestSupport {

	public static TEHierarchy get_ppb_tehier()
	{

		ArrayList<HierarchyEntry> hes=new ArrayList<HierarchyEntry>();
		hes.add(new HierarchyEntry("P-element","P-element","DNA"));
		hes.add(new HierarchyEntry("Ine-1","Ine-1","RNA"));
		hes.add(new HierarchyEntry("roo","roo","RNA"));

		TEHierarchy hier=new TEHierarchy(hes);
		return hier;
	}



	public static PpileupBuilder get_simplePpBuilder() {

		StringBuilder sb = new StringBuilder();
		// proper pair
		sb.append("r3\t99\t2L\t1\t20\t100M\t=\t200\t10\tGTG\t999\trc1\n");
		sb.append("r3\t147\t2L\t200\t20\t100M\t=\t1\t-10\tTAA\t899\trc3\n");
		SamPairReader spr = new SamPairReader(new BufferedReader(new StringReader(sb.toString())), DataTestSupport.getTEHierarchy(), 1000);
		PpileupBuilder pb = new PpileupBuilder(10, 100, spr, DataTestSupport.getTETranslator_iniFull2Short());
		return pb;
	}


	public static PpileupBuilder get_simplePpBuildermq(int mapq)
	{

			StringBuilder sb = new StringBuilder();
			// proper pair
			sb.append("r3\t99\t2L\t1\t20\t100M\t=\t200\t10\tGTG\t999\trc1\n");
			sb.append("r3\t147\t2L\t200\t20\t100M\t=\t1\t-10\tTAA\t899\trc3\n");
			sb.append("r3\t99\t2L\t5\t19\t100M\t=\t205\t10\tGTG\t999\trc1\n");
			sb.append("r3\t147\t2L\t205\t19\t100M\t=\t5\t-10\tTAA\t899\trc3\n");
			sb.append("r3\t99\t2L\t10\t21\t100M\t=\t210\t10\tGTG\t999\trc1\n");
			sb.append("r3\t147\t2L\t210\t21\t100M\t=\t10\t-10\tTAA\t899\trc3\n");
			SamPairReader spr=new SamPairReader(new BufferedReader(new StringReader(sb.toString())),DataTestSupport.getTEHierarchy(),1000);
			PpileupBuilder pb=new PpileupBuilder(mapq,100,spr,DataTestSupport.getTETranslator_iniFull2Short());
			return pb;

	}



		public static PpileupBuilder get_PpB_split() {

			StringBuilder sb = new StringBuilder();
			// proper pair
			sb.append("r3\t99\t2L\t1\t20\t100M\t=\t400\t10\tGTG\t999\trc1\n");
			sb.append("r3\t147\t2L\t400\t20\t100M\t=\t1\t-10\tTAA\t899\trc3\n");
			SamPairReader spr = new SamPairReader(new BufferedReader(new StringReader(sb.toString())), DataTestSupport.getTEHierarchy(), 1000);
			PpileupBuilder pb = new PpileupBuilder(10, 100, spr, DataTestSupport.getTETranslator_iniFull2Short());
			return pb;
		}

		public static PpileupBuilder get_PpB_tepele() {

		StringBuilder sb = new StringBuilder();
		// proper pair
		sb.append("r3\t99\t2L\t1\t20\t100M\tP-element\t400\t10\tGTG\t999\trc1\n");
		sb.append("r4\t147\t2L\t400\t20\t100M\tP-element\t1\t-10\tTAA\t899\trc3\n");
		SamPairReader spr = new SamPairReader(new BufferedReader(new StringReader(sb.toString())), PpileupTestSupport.get_ppb_tehier(), 1000);
		PpileupBuilder pb = new PpileupBuilder(10, 100, spr, DataTestSupport.getTETranslator_iniFull2Short());
		return pb;
	}
	public static PpileupBuilder get_PpB_teine() {

		StringBuilder sb = new StringBuilder();
		// proper pair
		sb.append("r3\t99\t2L\t1\t20\t100M\tIne-1\t400\t10\tGTG\t999\trc1\n");
		sb.append("r4\t147\t2L\t400\t20\t100M\tIne-1\t1\t-10\tTAA\t899\trc3\n");
		SamPairReader spr = new SamPairReader(new BufferedReader(new StringReader(sb.toString())), PpileupTestSupport.get_ppb_tehier(), 1000);
		PpileupBuilder pb = new PpileupBuilder(10, 100, spr, DataTestSupport.getTETranslator_iniFull2Short());
		return pb;
	}

	public static PpileupBuilder get_PpB_te_peleine()
	{

		StringBuilder sb = new StringBuilder();
		// proper pair
		sb.append("r3\t99\t2L\t1\t20\t100M\tP-element\t400\t10\tGTG\t999\trc1\n");
		sb.append("r4\t147\t2L\t400\t20\t100M\tP-element\t1\t-10\tTAA\t899\trc3\n");
		sb.append("r3\t99\t2L\t1\t20\t100M\tIne-1\t400\t10\tGTG\t999\trc1\n");
		sb.append("r4\t147\t2L\t400\t20\t100M\tIne-1\t1\t-10\tTAA\t899\trc3\n");
		SamPairReader spr = new SamPairReader(new BufferedReader(new StringReader(sb.toString())), PpileupTestSupport.get_ppb_tehier(), 1000);
		PpileupBuilder pb = new PpileupBuilder(10, 100, spr, DataTestSupport.getTETranslator_iniFull2Short());
		return pb;
	}


	public static PpileupBuilder get_PpB_SV() {

		StringBuilder sb = new StringBuilder();
		// proper pair
		sb.append("r3\t99\t2L\t1\t20\t100M\t2R\t400\t10\tGTG\t999\trc1\n");
		sb.append("r4\t147\t2L\t400\t20\t100M\t2R\t1\t-10\tTAA\t899\trc3\n");
		SamPairReader spr = new SamPairReader(new BufferedReader(new StringReader(sb.toString())), PpileupTestSupport.get_ppb_tehier(), 1000);
		PpileupBuilder pb = new PpileupBuilder(10, 100, spr, DataTestSupport.getTETranslator_iniFull2Short());
		return pb;
	}


	public static PpileupBuilder get_PpB_chrswitch() {

		StringBuilder sb = new StringBuilder();
		// proper pair
		sb.append("r3\t99\t2L\t1\t20\t100M\t2R\t400\t10\tGTG\t999\trc1\n");
		sb.append("r4\t99\t2R\t1\t20\t100M\t2L\t1\t-10\tTAA\t899\trc3\n");
		SamPairReader spr = new SamPairReader(new BufferedReader(new StringReader(sb.toString())), PpileupTestSupport.get_ppb_tehier(), 1000);
		PpileupBuilder pb = new PpileupBuilder(10, 100, spr, DataTestSupport.getTETranslator_iniFull2Short());
		return pb;
	}


	/**
	 * provides a quick way of obtaining a ppileup sample summary
	 * by just providing a string of the form
	 * "> 10 < 20 i 10 I 50 . 40"
	 * @param ppsample
	 * @return
	 */
	public static PpileupSampleSummary ppileupSampleSummaryFactory(String ppsample)
	{
		String[] spl= ppsample.split(" ");
		LinkedList<String> tmp=new LinkedList<>();
		for(String s:spl){tmp.add(s);}

		int countabsfwd=0;
		int countabsrev=0;
		int count_srfwd=0;
		int count_srrev=0;
		HashMap<String,Integer>  tecountfwd =new HashMap<String,Integer>();
		HashMap<String,Integer>  tecountrev =new HashMap<String,Integer>();

		while(tmp.size()>0)
		{
			String symbol =tmp.remove(0);
			int count=Integer.parseInt(tmp.remove(0));

			switch(symbol)
			{
				case PpileupSymbols.ABS:
					countabsfwd+=count;
					countabsrev+=count;
					break;
				case PpileupSymbols.ABSFWD:
					countabsfwd+=count;
					break;
				case PpileupSymbols.ABSREV:
					countabsrev+=count;
					break;
				case PpileupSymbols.SvFWD:
					count_srfwd=count; break;
				case PpileupSymbols.SvRev:
					count_srrev=count; break;
				default:
					if(symbol.toLowerCase().equals(symbol))  tecountrev.put(symbol,count);
					else if(symbol.toUpperCase().equals(symbol)) tecountfwd.put(symbol,count);


					break;
			}
		}
		PpileupDirectionalSampleSummary fw=new PpileupDirectionalSampleSummary(SignatureDirection.Forward,countabsfwd,count_srfwd,tecountfwd);
		PpileupDirectionalSampleSummary re=new PpileupDirectionalSampleSummary(SignatureDirection.Reverse,countabsrev,count_srrev,tecountrev);
		return new PpileupSampleSummary(fw,re);
	}

	/**
	 * Return a Ppileup site from a simpel formating string
	 * eg.: "2L\t10\tcom\t. 10 r 10\t. 15 i 10\t . 0 t 30"
	 *
	 * @param ppsample
	 * @return
	 */
	public static PpileupSite ppileupSiteFactory(String ppsample) {
		String[] spl= ppsample.split("\\t");
		LinkedList<String> tmp=new LinkedList<>();
		for(String s:spl){tmp.add(s);}

		String chr=tmp.remove(0);
		int pos=Integer.parseInt(tmp.remove(0));
		String comment=tmp.remove(0);

		ArrayList<PpileupSampleSummary> summaries = new ArrayList<PpileupSampleSummary>();
		while(tmp.size()>0) {
			summaries.add(ppileupSampleSummaryFactory(tmp.remove(0)));
		}
		return new PpileupSite(chr,pos,comment,summaries);

	}








}
