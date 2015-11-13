package corete.io.stat;


import corete.data.SamRecord;
import corete.data.stat.MapStatReads;
import corete.io.ISamBamReader;

import corete.data.hier.TEHierarchy;


import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 8/17/15.
 */
public class MapStatReadsReader {
	private ISamBamReader sbr;
	private TEHierarchy hier;
	private final int minMapQual;
	private Logger logger;


	public MapStatReadsReader(ISamBamReader sbr, TEHierarchy hier, int minMapQual, Logger logger){
		this.sbr=sbr;
		this.hier=hier;
		this.minMapQual=minMapQual;
		this.logger=logger;


	}



	public MapStatReads readMapStat()
	{
		this.logger.info("Creating statistics for mapped reads");
		int readsInFile=0;
		int readsMapped=0;
		int readsWithMq=0;
		int readsTe=0;
		int readsTeWithMq=0;
		HashMap<String,Integer> mappedRefChr =new HashMap<String,Integer>();
		HashMap<String,Integer> mappedTE= new HashMap<String,Integer>();

		               while(sbr.hasNext())
					   {
						   SamRecord sr=sbr.next();
						   readsInFile++;
						   if(sr.isUnmapped()) continue;
						   readsMapped++;
						   if(hier.containsRefid(sr.getRefchr())) readsTe++;

						   // filter mapqual
						   if(sr.getMapq()<minMapQual)continue;
						   readsWithMq++;
						   if(hier.containsRefid(sr.getRefchr()))
						   {
							   readsTeWithMq++;
							   String famname=hier.getFamily(sr.getRefchr());
							   mappedTE.putIfAbsent(famname,0);
							   mappedTE.put(famname,mappedTE.get(famname)+1);

						   }
						   else
						   {
							   mappedRefChr.putIfAbsent(sr.getRefchr(),0);
							   mappedRefChr.put(sr.getRefchr(),mappedRefChr.get(sr.getRefchr())+1);
						   }

					   }

		return new MapStatReads(readsInFile,readsMapped,readsWithMq,readsTe,readsTeWithMq,mappedRefChr,mappedTE);




	}
}
