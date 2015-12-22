package corete.data.tesignature;

import corete.data.SignatureDirection;
import corete.data.TEFamilyShortcutTranslator;
import corete.data.ppileup.PpileupSite;
import corete.io.ppileup.IPpileupReader;
import corete.io.ppileup.PpileupReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 10/21/15.
 */
public class SignatureFrequencyEstimationFramework {
	private final IPpileupReader pr;
	private final ArrayList<InsertionSignature> signatures;
	private final TEFamilyShortcutTranslator translator;
	private Logger logger;
	public SignatureFrequencyEstimationFramework(IPpileupReader pr,ArrayList<InsertionSignature> signatures,Logger logger)
	{

		this.pr=pr;
		this.translator=pr.getTEFamilyShortcutTranslator();
		this.signatures=new ArrayList<InsertionSignature>(signatures);
		this.logger =logger;

	}




	public ArrayList<InsertionSignature> getSignaturesWithFrequencies()
	{

		ArrayList<InsertionSignature> frequpdatedsigs=new ArrayList<InsertionSignature>();
		HashSet<String> pastChromosome=new HashSet<String>(); // a set of already parsed chromosomes

		// Chromosome specific activa
		String activeChr=null;
		ArrayList<SignatureFrequencyBuilder> chrspecBuilders=new ArrayList<SignatureFrequencyBuilder>();
		HashMap<Integer,ArrayList<SignatureFrequencyBuilder>> chrspecBuilderHash=new HashMap<Integer,ArrayList<SignatureFrequencyBuilder>>();

		PpileupSite site=null;
		while((site=pr.next())!=null)
		{
			// First the ACTION OF CHANGING A CHROMOSOME
			if(activeChr==null || (!activeChr.equals(site.getChromosome())))
			{

				if(chrspecBuilders.size()>0)
				{
					// Time to harvest// don't forget to harvest in the end
					logger.info("Upated "+chrspecBuilders.size() + " signatures of TE insertions with frequency estimates ");
					frequpdatedsigs.addAll(harvest(chrspecBuilders));
					chrspecBuilders=new ArrayList<SignatureFrequencyBuilder>();
					chrspecBuilderHash= new HashMap<Integer,ArrayList<SignatureFrequencyBuilder>>();
				}

				// new active chromosome; logging, checking, new Builder and new chromosome array map
				logger.info("Start parsing ppileup for chromosome "+site.getChromosome());
				activeChr=site.getChromosome();
				if(pastChromosome.contains(activeChr))throw new IllegalArgumentException("invalid ppileup file, make sure ppileup is filtered by chromosome; following entry occurs disjointly "+activeChr);
				pastChromosome.add(activeChr);
				chrspecBuilders=getChromosomeSpecificBuilders(activeChr);
				chrspecBuilderHash=getChrspecificBuilderHash(chrspecBuilders);
			}
			// handle the chromosome specific position
			int position=site.getPosition();
			if(chrspecBuilderHash.containsKey(position))
			{
				ArrayList<SignatureFrequencyBuilder> sitebuilders=chrspecBuilderHash.get(position);
				for(SignatureFrequencyBuilder sfb:sitebuilders) sfb.addSite(site);
			}

		}

		if(chrspecBuilders.size()>0)
		{
			logger.info("Upated "+chrspecBuilders.size() + " signatures of TE insertions with frequency estimates ");
			frequpdatedsigs.addAll(harvest(chrspecBuilders));
		}
		return frequpdatedsigs;



	}


	/**
	 * Once done with some builder (eg at chromosome boundary get the updated list)
	 * @param builders
	 * @return
	 */
	private ArrayList<InsertionSignature> harvest(ArrayList<SignatureFrequencyBuilder> builders)
	{
		ArrayList<InsertionSignature> signatures=new ArrayList<InsertionSignature>();
		for(SignatureFrequencyBuilder builder:builders)
		{
			signatures.add(builder.getUpdatedSignature());
		}

		return signatures;
	}

	/**
	 * Get the builders for one chromsome
	 * @param chromosome
	 * @return
	 */
	private ArrayList<SignatureFrequencyBuilder> getChromosomeSpecificBuilders(String chromosome)
	{
		ArrayList<SignatureFrequencyBuilder> chrspec=new ArrayList<SignatureFrequencyBuilder>();
		for(InsertionSignature sig: this.signatures)
		{
			if(sig.getChromosome().equals(chromosome)) {
				String shortcut;
				String antishortcut;
				if (sig.getSignatureDirection() == SignatureDirection.Forward) {
					shortcut = translator.getShortcutFwd(sig.getTefamily());
					antishortcut = translator.getShortcutRev(sig.getTefamily());
				} else {
					antishortcut = translator.getShortcutFwd(sig.getTefamily());
					shortcut = translator.getShortcutRev(sig.getTefamily());
				}

				chrspec.add(new SignatureFrequencyBuilder(sig, shortcut, antishortcut));
			}
		}
		return chrspec;
	}


	private HashMap<Integer,ArrayList<SignatureFrequencyBuilder>> getChrspecificBuilderHash(ArrayList<SignatureFrequencyBuilder> builders)
	{
		HashMap<Integer,ArrayList<SignatureFrequencyBuilder>> toret=new HashMap<Integer,ArrayList<SignatureFrequencyBuilder>>();
		for(SignatureFrequencyBuilder sfb:builders)
		{
			for(int i=sfb.getStart(); i<=sfb.getEnd(); i++)
			{
				toret.putIfAbsent(i,new ArrayList<SignatureFrequencyBuilder>());
				toret.get(i).add(sfb);
			}
		}
		return toret;
	}






}
