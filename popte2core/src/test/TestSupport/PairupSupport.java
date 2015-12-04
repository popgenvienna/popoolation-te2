package test.TestSupport;

import corete.data.SignatureDirection;
import corete.data.TEStrand;
import corete.data.hier.HierarchyEntry;
import corete.data.hier.TEHierarchy;
import corete.data.polyn.ContigwisePolynRepresentation;
import corete.data.polyn.PolyNRecord;
import corete.data.polyn.PolyNRecordCollection;
import corete.data.teinsertion.Signature2InsertionFactory;
import corete.data.teinsertion.SignaturePairupFramework;
import corete.data.tesignature.FrequencySampleSummary;
import corete.data.tesignature.InsertionSignature;
import corete.data.tesignature.PopulationID;
import corete.io.tesignature.PopolutionIDParser;
import corete.misc.LogFactory;

import java.util.ArrayList;

/**
 * Created by robertkofler on 12/4/15.
 */
public class PairupSupport {

	public static TEHierarchy getTEHierarchy()
	{

		ArrayList<HierarchyEntry> hes=new ArrayList<HierarchyEntry>();
		hes.add(new HierarchyEntry("p1","pele","DNA"));
		TEHierarchy hier=new TEHierarchy(hes);
		return hier;
	}



	public static PolyNRecordCollection getEmpty()
	{
		return new PolyNRecordCollection(new ArrayList<PolyNRecord>());
	}

	public static  PolyNRecordCollection getCollection(String chr, int start,int end) {
		ArrayList<PolyNRecord> polyns = new ArrayList<PolyNRecord>();
		polyns.add(new PolyNRecord(chr, start, end));
		return new PolyNRecordCollection(polyns);
	}


	public static ContigwisePolynRepresentation getemptypolyn_chrX()
	{
		return new ContigwisePolynRepresentation(new ArrayList<PolyNRecord>(),"X");
	}

	public static  ContigwisePolynRepresentation getpolyn_chrX(int start,int end)
	{
		ArrayList<PolyNRecord> polyns=new ArrayList<PolyNRecord>();
		polyns.add(new PolyNRecord("X",start,end));
		return new ContigwisePolynRepresentation(polyns,"X");
	}


	/**
	 * a string containing multiple signature in the form:
	 *
	 * popid chr start end direction family strand popfreq
	 * 1\tX\t100\t200\tF\tpele\t-\t0.1
	 * 1\tX\t200\t300\tR\tpele\t-\t0.3
	 * @param signaturedefinitions
	 * @return
	 */
	public static ArrayList<InsertionSignature> signatureFactory(String signaturedefinitions)
	{
		String[] sp= signaturedefinitions.split("\\n");


		ArrayList<InsertionSignature> signatures=new ArrayList<InsertionSignature>();
		for(String s: sp)
		{
			signatures.add(getSignaturue(s));
		}

		return signatures;
	}

	public static InsertionSignature getSignaturue(String signatureDefinition)
	{
		// 0 1 2   3   4  5   6  7
		// 1 X 100 200 F pele - 0.1
		String[] a=signatureDefinition.split("\\t");
		PopulationID pid= PopolutionIDParser.getPopulationID(a[0]);

		SignatureDirection sd=null;
		if(a[4].equals("F")) sd=SignatureDirection.Forward;
		else if(a[4].equals("R")) sd=SignatureDirection.Reverse;
		else throw new IllegalArgumentException("Unknown signature direction");

		TEStrand str=null;
		if(a[6].equals("-")) str=TEStrand.Minus;
		else if(a[6].equals("+"))str=TEStrand.Plus;
		else if(a[6].equals("."))str=TEStrand.Unknown;
		else throw new IllegalArgumentException("Unknown signature direction");

		Double freq=Double.parseDouble(a[7]);

		ArrayList<FrequencySampleSummary> fss=new ArrayList<FrequencySampleSummary>();
		fss.add(new FrequencySampleSummary(1,1000.0,freq*1000.0,0.0,0.0));


		return new InsertionSignature(pid,a[1],sd,Integer.parseInt(a[2]),Integer.parseInt(a[3]),a[5],str,fss);
	}




	public static SignaturePairupFramework getFramework(String definition,PolyNRecordCollection polyns,double maxdif,int mindistance, int maxdistance)
	{

		TEHierarchy hier = PairupSupport.getTEHierarchy();
		return new SignaturePairupFramework(PairupSupport.signatureFactory(definition),new Signature2InsertionFactory(hier),polyns,maxdif,mindistance,maxdistance,
				LogFactory.getNullLogger());
	}




}
