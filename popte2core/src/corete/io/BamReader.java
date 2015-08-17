package corete.io;

import corete.data.SamRecord;
import corete.io.Parser.CigarParser;
import htsjdk.samtools.SamReaderFactory;
import htsjdk.samtools.ValidationStringency;
import htsjdk.samtools.SAMRecordIterator;

import java.io.File;
import java.security.InvalidParameterException;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 6/30/15.
 * Wrapper using the htsjdk library.
 * Why? When htsdjk changes all I need to change is the imlementation of this class and not every line of the code
 * where htsdjk is called; also support for Logger and conversion to my SamRecord
 */
public class BamReader implements ISamBamReader {

	private final String bamfile;
	private final Logger logger;
	private final SAMRecordIterator sri;
	private SamRecord next=null;



	public BamReader(String file, Logger logger)
	{
		this.bamfile=file;
		this.logger=logger;
		this.logger.info("Reading bam file "+file);
		SamReaderFactory srf=SamReaderFactory.make();
		srf.validationStringency(ValidationStringency.LENIENT);
		htsjdk.samtools.SamReader samreader = srf.open(new File(this.bamfile));
		sri= samreader.iterator();
		next=this.read_next();        // read the first entry
	}

	/**
	 * is there a next entry?
	 * @return
	 */
	public boolean hasNext()
	{
	 	if(next==null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * Get the next sam record
	 * @return
	 */
	public SamRecord next()
	{
		if(!this.hasNext()) throw new InvalidParameterException("no next record!");
		SamRecord toret=next;
		next=this.read_next();
		return toret;
	}


	/**
	 * actually read the next record
	 * @return
	 */
	private SamRecord read_next()
	{
		if(sri.hasNext())
		{
			SamRecord sr=translate(sri.next());
			return sr;
		}
		else
		{
			sri.close();
			return null;
		}
	}

	private corete.data.SamRecord translate(htsjdk.samtools.SAMRecord sr)
	{

		CigarParser cp=new CigarParser(sr.getCigarString(),sr.getAlignmentStart());

		corete.data.SamRecord toret=new SamRecord(sr.getReadName(),sr.getFlags(),sr.getReferenceName(),cp.getStart(),cp.getEnd(),cp.getStart_withs(),cp.getEnd_withs() ,sr.getMappingQuality(),
				sr.getCigarString(),sr.getMateReferenceName(),sr.getMateAlignmentStart(),sr.getInferredInsertSize(),sr.getReadString(),sr.getBaseQualityString(),"");

		return toret;
	}


}
