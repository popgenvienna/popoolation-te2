package pt2.se2pe;

import corete.data.FastaRecord;
import corete.data.SamRecord;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMFileWriter;
import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.fastq.FastqRecord;

import java.util.logging.Logger;

/**
 * Created by robertkofler on 10/29/15.
 */
public class Pop2FileWriter {
	private SAMFileHeader header;
	private SAMFileWriter writer;
	private Logger logger;
	public Pop2FileWriter(SAMFileHeader header, SAMFileWriter writer, Logger logger)
	{
		this.header=header;
		this.writer=writer;
		this.logger=logger;
	}

	public void write(FastqRecord fastq1, FastqRecord fastq2, SamRecord sam1, SamRecord sam2, String readname)
	{
		SAMRecord sr1;
		SAMRecord sr2;
		if(sam1==null) sr1=recordFromFastq(fastq1,readname,header);
		else sr1=recordFromSam(sam1,readname,header);
		if(sam2==null) sr2=recordFromFastq(fastq2,readname,header);
		else sr2=recordFromSam(sam2,readname,header);

		sr1.setReadPairedFlag(true);
		sr2.setReadPairedFlag(true);
		sr1.setFirstOfPairFlag(true);
		sr2.setSecondOfPairFlag(true);



		if(sr1.getReadUnmappedFlag() && sr2.getReadUnmappedFlag())
		{
			   	sr1.setMateUnmappedFlag(true);
				sr2.setMateUnmappedFlag(true);
		}
		else if(sr1.getReadUnmappedFlag())
		{
			sr2.setMateUnmappedFlag(true);
		}
		else if(sr2.getReadUnmappedFlag())
		{
			  sr1.setMateUnmappedFlag(true);
		}
		else
		{
			sr1.setMateNegativeStrandFlag(sr2.getReadNegativeStrandFlag()); // negative strand mapping
			sr2.setMateNegativeStrandFlag(sr1.getReadNegativeStrandFlag());
			sr1.setMateReferenceName(sr2.getReferenceName());  // reference contig
			sr2.setMateReferenceName(sr1.getReferenceName());
			sr1.setMateAlignmentStart(sr2.getAlignmentStart());  // alignment start
			sr2.setMateAlignmentStart(sr1.getAlignmentStart());
		}


		writer.addAlignment(sr1);
		writer.addAlignment(sr2);
	}


	public void close()
	{
		this.writer.close();
	}


	/**
	 * Convert a fastqRecord to a unmmaped SAMRecord
	 *
	 * @param record	the record to convert
	 * @param header	the header for initialize the record
	 * @return			the transformed record
	 */
	private SAMRecord recordFromFastq(FastqRecord record, String readname, SAMFileHeader header) {
		// generate record that already have everything set to undefined (flag is 0, so it is important to change this)
		SAMRecord toReturn = new SAMRecord(header);
		// set the name for the record
		toReturn.setReadName(readname);
		// set the bases
		toReturn.setReadString(record.getReadString());
		// set the qualities
		toReturn.setBaseQualityString(record.getBaseQualityString());
		// set unmapped flag
		toReturn.setReadUnmappedFlag(true);
		return toReturn;
	}

	/**
	 * Convert a fastqRecord to a unmmaped SAMRecord
	 *
	 * @param record	the record to convert
	 * @param header	the header for initialize the record
	 * @return			the transformed record
	 */
	private SAMRecord recordFromSam(SamRecord record, String readname, SAMFileHeader header) {
		// generate record that already have everything set to undefined (flag is 0, so it is important to change this)
		SAMRecord toReturn = new SAMRecord(header);
		// set the name for the record
		toReturn.setReadName(readname);
		toReturn.setFlags(record.getFlag());
		toReturn.setReferenceName(record.getRefchr());
		toReturn.setAlignmentStart(record.getStart());
		toReturn.setMappingQuality(record.getMapq());
		toReturn.setCigarString(record.getCigar());
		toReturn.setReadString(record.getSequence());
		toReturn.setBaseQualityString(record.getQual());
		return toReturn;
	}




}
