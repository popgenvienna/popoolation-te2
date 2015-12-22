package corete.data.teinsertion;

import corete.data.SignatureDirection;
import corete.data.polyn.ContigwisePolynRepresentation;
import corete.data.polyn.PolyNRecordCollection;
import corete.data.tesignature.FrequencySampleSummary;
import corete.data.tesignature.InsertionSignature;

import java.util.*;
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
			this.logger.fine("Processing family "+tefamily+"; with insertion numbers "+famspec.size());
			toret.addAll(getInsertionsForChrFamSpec(famspec,chrpolyn));

		}
		return toret;
	}


	private ArrayList<TEinsertion> getInsertionsForChrFamSpec(ArrayList<InsertionSignature> chrfamspec,ContigwisePolynRepresentation chrpolyn)
	{
		// Oki that's a pretty algorithm...took me some time
		ArrayList<InsertionSignature> fwd=new ArrayList<InsertionSignature>();
		ArrayList<InsertionSignature> rev=new ArrayList<InsertionSignature>();
		for(InsertionSignature s:chrfamspec)
		{
			if(s.getSignatureDirection()== SignatureDirection.Forward) fwd.add(s);
			else if(s.getSignatureDirection()==SignatureDirection.Reverse) rev.add(s);
			else throw new IllegalArgumentException("unknown signature direction");
		}


		// Identify the possible pairs
		ArrayList<PossiblePair> pps=new ArrayList<PossiblePair>();
		for(InsertionSignature f: fwd)
		{
			for(InsertionSignature r:rev)
			{
				// Are already chr and fam specific
				assert(f.getTefamily().equals(r.getTefamily()));
				assert(f.getChromosome().equals(r.getChromosome()));

				if(!f.getPopid().equals(r.getPopid())) continue;
				if(f.getTEStrand() !=r.getTEStrand()) continue;
				if(maxPopFreqDif(f,r)>this.maxDif) continue;
				// equal popid, chr, strand, family, maxfreqdif < maxallowed

				// distance from forward start
				if(!chrpolyn.distanceIsOutsideBoundary(f.getDirectionAwarePosition(),r.getDirectionAwarePosition(),minDistance,maxDistance))
				{
					int dist= chrpolyn.getDistance(f.getDirectionAwarePosition(),r.getDirectionAwarePosition());
					pps.add(new PossiblePair(f,r,dist));
				}
			}
		}

		// Sort the possible pairs most promising first, ie. shortest distance first
		Collections.sort(pps,new Comparator<PossiblePair>() {
			@Override
			public int compare(PossiblePair o1, PossiblePair o2) {
				if(o1.distance<o2.distance) return -1;
				if(o1.distance>o2.distance) return 1;
				return 0;
			}
		});

		// Add the possible pairs, starting by the one with the smallest distance ending with the largest distance;
		// Only use possible pairs where both Insertion signatures are not yet present in the used hash;
		ArrayList<TEinsertion> toret=new ArrayList<>();
		HashSet<InsertionSignature> used=new HashSet<InsertionSignature>();
		for(PossiblePair pp:pps)
		{
			if((!used.contains(pp.forward)) && (!used.contains(pp.reverse)))
			{
				toret.add(s2if.fromTwo(pp.forward,pp.reverse));
				used.add(pp.forward); used.add(pp.reverse);
			}
		}

		// Process all remaining ones
		for(InsertionSignature s: chrfamspec) {
			if(!used.contains(s)) {
				toret.add(s2if.fromOne(s));
				used.add(s);
			}
		}
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

class PossiblePair
{
	public final InsertionSignature forward;
	public final InsertionSignature reverse;
	public final int distance;
	public PossiblePair(InsertionSignature forward, InsertionSignature reverse,int distance)
	{
		this.forward=forward;
		this.reverse=reverse;
		this.distance=distance;
	}
}

