package corete.data.teinsertion;

import corete.data.SignatureDirection;
import corete.data.polyn.ContigwisePolynRepresentation;
import corete.data.polyn.PolyNRecordCollection;
import corete.data.tesignature.FrequencySampleSummary;
import corete.data.tesignature.InsertionSignature;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 10/28/15.
 */
public class SignaturePairupFramework {
	private final ArrayList<InsertionSignature> signatures;
	private final HashSet<String> contigids;

	private final Signature2InsertionFactory s2if;
	private final PolyNRecordCollection polyn;
	private final double maxDif;
	private final int minDistance;
	private final int maxDistance;
	private Logger logger;

	public SignaturePairupFramework(ArrayList<InsertionSignature> signatures, Signature2InsertionFactory s2if, PolyNRecordCollection polyn,
									double maxDif, int minDistance, int maxDistance, Logger logger)
	{
		this.signatures=new ArrayList<InsertionSignature>(signatures);
		this.s2if=s2if;
		this.polyn=polyn;
		this.logger=logger;
		this.maxDif=maxDif;
		this.minDistance=minDistance;
		this.maxDistance=maxDistance;

		contigids=new HashSet<String>();
		for(InsertionSignature is: signatures)
		{
			contigids.add(is.getChromosome());
		}
	}


	public ArrayList<TEinsertion> getTEinsertions()
	{
		ArrayList<TEinsertion> toret=new ArrayList<TEinsertion>();
		for(String chrid:this.contigids)
		{
			this.logger.info("Pairing signatures for contig "+chrid);
			toret.addAll(getForChromosome(chrid));
		}
		return toret;
	}


	private ArrayList<TEinsertion> getForChromosome(String chrid)
	{

		// First find all insertions for a chromosome
		// and find all different TE families for a chromosome
		HashSet<String> tefamilies=new HashSet<String>();
		ArrayList<InsertionSignature> chrspecsignatures=new ArrayList<InsertionSignature>();
		for(InsertionSignature sig:this.signatures)
		{
			if(sig.getChromosome().equals(chrid))
			{
				chrspecsignatures.add(sig);
				tefamilies.add(sig.getTefamily());
			}
		}

		ContigwisePolynRepresentation chrpolyn=this.polyn.getContigwisePolynRepresentation(chrid);
		ArrayList<TEinsertion> toret=new ArrayList<TEinsertion>();
		// Process all TE families separately
		for(String tefamily:tefamilies)
		{
			ArrayList<InsertionSignature> famspec=new ArrayList<InsertionSignature>();
			// Get all signatures of the given family
			for(InsertionSignature sig:chrspecsignatures) if(sig.getTefamily().equals(tefamily)) famspec.add(sig);

			// so now get the for a given family;
			toret.addAll(getInsertionsForChrFamSpec(famspec,chrpolyn));

		}
		return toret;
	}


	private ArrayList<TEinsertion> getInsertionsForChrFamSpec(ArrayList<InsertionSignature> chrfamspec,ContigwisePolynRepresentation chrpolyn)
	{
		LinkedList<InsertionSignature> fwd=new LinkedList<InsertionSignature>();
		LinkedList<InsertionSignature> rev=new LinkedList<InsertionSignature>();
		for(InsertionSignature s:chrfamspec)
		{
			if(s.getSignatureDirection()== SignatureDirection.Forward) fwd.add(s);
			else if(s.getSignatureDirection()==SignatureDirection.Reverse) rev.add(s);
			else throw new IllegalArgumentException("unknown signature direction");
		}


		ArrayList<TEinsertion> toret=new ArrayList<>();

		// First go over all the fwd insertions;
		while(fwd.size()>0)
		{
			InsertionSignature cand_fwd=fwd.remove(0);

			// Check if a fitting reverse insertion can be found
			int i=0;
			boolean success =false;
			for(; i<rev.size(); i++)
			{
				InsertionSignature cand_rev=rev.get(i);

				if(!cand_fwd.getPopid().equals(cand_rev.getPopid())) continue;
				if(cand_fwd.getTEStrand() !=cand_rev.getTEStrand()) continue;
				if(maxPopFreqDif(cand_fwd,cand_rev)>this.maxDif) continue;
				// equal popid, chr, strand, family, maxfreqdif < maxallowed

				// distance from forward start
				int distance =chrpolyn.getDistance(cand_fwd.getEnd(),cand_rev.getStart());
				if(distance>=minDistance && distance<=maxDistance)
				{
					success=true;
					break;
				}
			}

			// If a partner was found than pair both up
			if(success)
			{
				toret.add(s2if.fromTwo(cand_fwd,rev.remove(i)));
			}
			else toret.add(s2if.fromOne(cand_fwd));      // if no partner was found, use as stand-alone fwd insertion
		}

		// also treat the remaining reverse insertions; as no fwd insertion is left treat them all as stand-alone rev insertions
		while(rev.size()>0) toret.add(s2if.fromOne(rev.remove(0)));

		return toret;
	}

	private double maxPopFreqDif(InsertionSignature a, InsertionSignature b)
	{
		double maxdif=0;
		for(int i=0; i<a.getFrequencies().size();i++)
		{
			FrequencySampleSummary af=a.getFrequencies().get(i);
			FrequencySampleSummary bf=b.getFrequencies().get(i);
			double fd=Math.abs(af.getPopulationFrequency()-bf.getPopulationFrequency());
			if(fd>maxdif) maxdif=fd;

		}
		return maxdif;
	}





}
