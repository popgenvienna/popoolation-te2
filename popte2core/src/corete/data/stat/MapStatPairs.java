package corete.data.stat;

import java.util.HashMap;

/**
 * Created by robertkofler on 11/10/15.
 */
public class MapStatPairs
{

	private final int mappedPairs;
	private final int properPairs;
	private final int properPairsWithMq;
	private final int structuralVariants;
	private final int structuralVariantsWithMq;
	private final int tePair;
	private final int tePairWithMq;
	private final HashMap<String,Integer> mapProperPair;
	private final HashMap<String,Integer> mapStructuralRearrangement;
	private final HashMap<String,Integer> mapTE;
	private final HashMap<String,Integer> mapTEchr;


	public MapStatPairs(int mappedPairs, int properPairs, int properPairsWithMq, int structuralVariants, int structuralVariantsWithMq,
						int tePair, int tePairWithMq, HashMap<String,Integer> mapProperPair, HashMap<String,Integer> mapStructuralRearrangement,
						HashMap<String,Integer> mapTE, HashMap<String,Integer> mapTEchr)
	{
		this.mappedPairs=mappedPairs;
		this.properPairs=properPairs;
		this.properPairsWithMq=properPairsWithMq;
		this.structuralVariants=structuralVariants;
		this.structuralVariantsWithMq=structuralVariantsWithMq;
		this.tePair=tePair;
		this.tePairWithMq=tePairWithMq;
		this.mapProperPair = new HashMap<String,Integer>(mapProperPair);
		this.mapStructuralRearrangement=new HashMap<String,Integer>(mapStructuralRearrangement);
		this.mapTE=new HashMap<String,Integer>(mapTE);
		this.mapTEchr = new HashMap<String,Integer>(mapTEchr);

	}


	public int getMapped() {
		return mappedPairs;
	}

	public int getPairs() {
		return properPairs;
	}

	public int getProperPairsWithMq() {
		return properPairsWithMq;
	}

	public int getStructuralVariants() {
		return structuralVariants;
	}

	public int getStructuralVariantsWithMq() {
		return structuralVariantsWithMq;
	}

	public int getTePair() {
		return tePair;
	}

	public int getTePairWithMq() {
		return tePairWithMq;
	}

	public HashMap<String, Integer> getMapProperPair() {
		return new HashMap<String, Integer>(mapProperPair);
	}

	public HashMap<String, Integer> getMapStructuralRearrangement() {
		return new HashMap<String, Integer>(mapStructuralRearrangement);
	}

	public HashMap<String, Integer> getMapTE() {
		return new HashMap<String, Integer>(mapTE);
	}

	public HashMap<String, Integer> getMapTEChr() {
		return new HashMap<String, Integer>(mapTEchr);
	}
}
