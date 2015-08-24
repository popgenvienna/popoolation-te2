package corete.io;

import corete.data.SamPair;
import corete.data.SamPairType;
import corete.data.SamRecord;
import corete.data.hier.TEHierarchy;
import corete.io.SamValidator.SamValidatorIsSorted;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 8/17/15.
 */
public class SamPairReader {
	private final int srmd;
	private final Logger logger;
	private final ISamBamReader sbr;
	private final String inputFile;
	private final TEHierarchy tehier;
	private SamPair next=null;

	// debug variables
	private int countPair=0;
	private int countTEspan=0;
	private int countUnmapped=0;
	private int countTEpair=0;
	private int countBrokenpair=0;

	public SamPairReader(String inputFile, TEHierarchy tehier, int srmd, Logger logger)
	{
		this.logger=logger;
		this.tehier=tehier;
		this.inputFile=inputFile;
		this.srmd=srmd;
		this.sbr=new AutoDetectSamBamReader(inputFile,logger,new SamValidatorIsSorted());

		this.next=getNext();
	}


	public boolean hasNext()
	{
		if(this.next==null){return false;}
		else{return true;}
	}

	public SamPair next()
	{
		if(!this.hasNext()){throw new InvalidParameterException("there is no further SamPair; use hasNext() first"); }
		SamPair toret=this.next;
		this.next=getNext();
		return toret;
	}



	private SamPair getNext()
	{

		HashMap<String,SamRecord> paarship=new HashMap<String,SamRecord>();
		// read from file
		while(sbr.hasNext())
		{
			SamRecord sr=sbr.next();
			// kick out unmapped or unmapped mates
			if(sr.isUnmapped() || sr.isUnmappedMate()){countUnmapped++; continue;}
			// kick out TE reads
			if(tehier.containsRefid(sr.getRefchr())){countTEpair++; continue;}

			// Hmm, it's probably best not to treat MQ at this step as both reads of a pair may have different mq, and so the mate
			// may get lost, clogging the hash
			// kick out reads with insufficient mapping Quality
			//if(sr.getMapq()<this.mapQual){continue;}

			// here all reads are a.) mapped b.) not TE
			// RETURN TE read
			if(tehier.containsRefid(sr.getRefchrMate())){countTEspan++;
				String fam=tehier.getFamily(sr.getRefchrMate());
				String ord=tehier.getOrder(sr.getRefchrMate());
				return new SamPair(sr,null, SamPairType.TEInsert,fam,ord);}
			// broken pair on different chromosomes
			if(!sr.getRefchr().equals(sr.getRefchrMate())){countBrokenpair++; return new SamPair(sr,null, SamPairType.BrokenPair);}

			// here all are already on same reference chromosome, ie not on TE
			if(Math.abs(sr.getStart()-sr.getStartMate()) > this.srmd){countBrokenpair++; return new SamPair(sr,null,SamPairType.BrokenPair);}

			// ok same chromosome, not te, no large distance
			if(paarship.containsKey(sr.getReadname()))
			{
				SamPair sp=new SamPair(paarship.get(sr.getReadname()),sr,SamPairType.Pair);
				paarship.remove(sr.getReadname());
				countPair++;
				return sp;
			}
			else
			{
				paarship.put(sr.getReadname(),sr);
			}
		}

		if(!paarship.isEmpty()){this.logger.info("WARNING - sam/bam file may be compromised could not find mate for "+paarship.size()+ " reads" );}
		this.logger.fine("Unmapped reads "+countUnmapped);
		this.logger.fine("Mapping to TE "+countTEpair);
		this.logger.fine("Broken pairs "+countBrokenpair);
		this.logger.fine("Genomic pairs "+countPair);
		this.logger.fine("PE spanning TE insertions "+countTEspan);

		return null;

	}




}
