package corete.io.stat;


import corete.data.SamPair;
import corete.data.SamPairType;
import corete.data.SamRecord;
import corete.data.hier.TEHierarchy;
import corete.data.stat.MapStatPairs;
import corete.data.stat.MapStatReads;
import corete.io.ISamBamReader;
import corete.io.SamPairReader;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 8/17/15.
 */
public class MapStatPairsReader {
	private SamPairReader spr;

	private final int minMapQual;
	private Logger logger;


	public MapStatPairsReader(SamPairReader spr,  int minMapQual, Logger logger){
		this.spr=spr;

		this.minMapQual=minMapQual;
		this.logger=logger;


	}



	public MapStatPairs pairMapStat()
	{
		this.logger.info("Creating statistics for pair-end fragments");


		int mapped=0;
		int pairs=0;
		int properPairsWithMq=0;
		int structuralVariants=0;
		int structuralVariantsWithMq=0;
		int tePair=0;
		int tePairWithMq=0;
		HashMap<String,Integer> mapProperPair= new HashMap<String,Integer>();
		HashMap<String,Integer> mapStructuralRearrangement= new HashMap<String,Integer>();
		HashMap<String,Integer> mapTE= new HashMap<String,Integer>();
		HashMap<String,Integer> mapTEchr= new HashMap<String,Integer>();



		while(spr.hasNext())
					   {

						   SamPair sp=spr.next();
						   mapped++;
						   if(sp.getSamPairType()== SamPairType.TEInsert) tePair++;
						   else if(sp.getSamPairType()==SamPairType.Pair)pairs++;
						   else if(sp.getSamPairType()==SamPairType.BrokenPair)structuralVariants++;
						   else throw new IllegalArgumentException("invalid Sam pair type "+sp.getSamPairType());

						   // Filter for mapping quality
						   if(sp.getFirstRead().getMapq()<minMapQual)continue;
						   if(sp.getSamPairType()==SamPairType.Pair && sp.getSecondRead().getMapq()<minMapQual) continue;

						   // summary stat
						   if(sp.getSamPairType()== SamPairType.TEInsert) tePairWithMq++;
						   else if(sp.getSamPairType()==SamPairType.Pair && sp.isProperPair(minMapQual)) properPairsWithMq++;
						   else if(sp.getSamPairType()==SamPairType.BrokenPair)structuralVariantsWithMq++;


						   // detailed stat
						   if(sp.getSamPairType()== SamPairType.TEInsert)
						   {
							   mapTE.putIfAbsent(sp.getFamily(),0);
							   mapTE.put(sp.getFamily(),mapTE.get(sp.getFamily())+1);

							   String key=sp.getFirstRead().getRefchr()+"-"+sp.getFamily();
							   mapTEchr.putIfAbsent(key,0);
							   mapTEchr.put(key,mapTEchr.get(key)+1);

						   }
						   else if(sp.getSamPairType()==SamPairType.Pair && sp.isProperPair(minMapQual)) {
							   mapProperPair.putIfAbsent(sp.getFirstRead().getRefchr() ,0);
							   mapProperPair.put(sp.getFirstRead().getRefchr(),mapProperPair.get(sp.getFirstRead().getRefchr())+1);

						   }
						   else if(sp.getSamPairType()==SamPairType.BrokenPair)
						   {
							   String key = getKey(sp.getFirstRead().getRefchr(),sp.getFirstRead().getRefchrMate());
							   mapStructuralRearrangement.putIfAbsent(key,0);
							   mapStructuralRearrangement.put(key,mapStructuralRearrangement.get(key)+1);
						   }





					   }

		return new MapStatPairs(mapped,pairs,properPairsWithMq,structuralVariants,structuralVariantsWithMq,tePair,tePairWithMq,mapProperPair,
				mapStructuralRearrangement,mapTE,mapTEchr);
	}

	private String getKey(String  first, String second)
	{
		if(first.compareTo(second)<0)
		{
			String tmp=first;
			first=second;
			second=tmp;
		}
		return first+"-"+second;

	}

}
