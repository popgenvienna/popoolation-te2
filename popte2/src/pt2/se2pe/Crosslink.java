package pt2.se2pe;

import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMFileWriter;
import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.fastq.FastqRecord;
import htsjdk.samtools.SamPairUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Class to perform the crosslink between read-pairs and include mate information.
 * It also adds some tags from the multiple alignments
 * TODO: unit tests
 *
 * @author Daniel Gómez-Sánchez
 */
public class Crosslink {

	// Proper oriented pairs are the ones where they are forward-reverse or reverse-forward
	private static List<SamPairUtil.PairOrientation> properOriented = new ArrayList<SamPairUtil.PairOrientation>(){{
		add(SamPairUtil.PairOrientation.FR);
		add(SamPairUtil.PairOrientation.RF);
	}};

	// the first record (crosslinked)
	private SAMRecord crosslinkedRecord1;
	// the second record (crosslinked)
	private SAMRecord crosslinkedRecord2;

	/**
	 * Construct an instance with the crosslinked records
	 *
	 * @param record1			the fastq record for the first pair
	 * @param record2			the fastq record for the second pair
	 * @param alignmentList1	the list with the alignments in the BAM file for the first pair
	 * @param alignmentList2	the list with the alignments in the BAM file for the second pair
	 * @param header			the header for the BAM file (to create a unmapped read if no alignment is in the list)
	 * @param maxAlignmentsSA	the maximum number of alignments to include in the SA tag
	 */
	public Crosslink(FastqRecord record1, FastqRecord record2, LinkedList<SAMRecord> alignmentList1, LinkedList<SAMRecord> alignmentList2, SAMFileHeader header, int maxAlignmentsSA) {
		// get the record to crosslink
		crosslinkedRecord1 = getToCrosslink(record1, alignmentList1, header, maxAlignmentsSA);
		crosslinkedRecord2 = getToCrosslink(record2, alignmentList2, header, maxAlignmentsSA);
		// set the first and second in pair flags
		crosslinkedRecord1.setFirstOfPairFlag(true);
		crosslinkedRecord2.setSecondOfPairFlag(true);
		// set that the records are paired
		crosslinkedRecord1.setReadPairedFlag(true);
		crosslinkedRecord2.setReadPairedFlag(true);
		// perform the crosslink between the two pairs (using the htsjdk library) -> it also computes the insert size
		SamPairUtil.setMateInfo(crosslinkedRecord1, crosslinkedRecord2, false);
		// add proper pair information based on the proper orientation
		SamPairUtil.setProperPairFlags(crosslinkedRecord1, crosslinkedRecord2, properOriented);
	}

	/**
	 * Get the first pair in this crosslink
	 *
	 * @return	a copy of the pair
	 * @throws CloneNotSupportedException
	 */
	public SAMRecord getFirstPair() throws CloneNotSupportedException {
		return (SAMRecord) crosslinkedRecord1.clone();
	}

	/**
	 * Get the second pair in this crosslink
	 *
	 * @return	a copy of the pair
	 * @throws CloneNotSupportedException
	 */
	public SAMRecord getSecondPair() throws CloneNotSupportedException {
		return (SAMRecord) crosslinkedRecord2.clone();
	}

	public void writeCrosslinkTo(SAMFileWriter writer) {
		writer.addAlignment(this.crosslinkedRecord1);
		writer.addAlignment(this.crosslinkedRecord2);
	}

	/**
	 * Obtain the record to crosslink from the alignments:
	 * 		If there are not alignment, obtain an unmapped read from the fastq entry
	 * 		If there is only one alignment, modify the record to obtain an uniquely mapped record
	 * 		If there are multiple alignments, add the multiple alignment tags to the best alignment
	 *
	 * @param record			the fastq record before mapping
	 * @param alignmentList		the list of alignments for this fastq record
	 * @param header			the header from the BAM file (for no alignments)
	 * @return					a copy of the record with all the new tags inside
	 */
	private SAMRecord getToCrosslink(FastqRecord record, LinkedList<SAMRecord> alignmentList, SAMFileHeader header, int maxAlignmentsSA) {
		switch(alignmentList.size()) {
			// Create an unmapped read if the arraylist is empty
			case 0: return recordFromFastq(record, header);
			// if only one record is in the list, return that record with the proper tags
			case 1:	return uniqueAlignment(alignmentList.getFirst());
			// if there are more, get the best alignment
			default: return BestAlignmentUtils.getBestAlignment(alignmentList, maxAlignmentsSA);
		}
	}

	/**
	 * Return the record with the X0 tag set to 1
	 *
	 * @param record	the record to add the tags
	 * @return			a copy of the record with the tags added
	 */
	private SAMRecord uniqueAlignment(SAMRecord record) {
		SAMRecord toReturn;
		// this should not throw the error, but it is safer
		try {
			// copy the record
			toReturn = (SAMRecord)record.clone();
		} catch(Exception e) {
			throw new RuntimeException("Unexpected exception when obtaining unique alignment from "+record);
		}
		toReturn.setAttribute("X0", 1);
		return toReturn;
	}

	/**
	 * Convert a fastqRecord to a unmmaped SAMRecord
	 *
	 * @param record	the record to convert
	 * @param header	the header for initialize the record
	 * @return			the transformed record
	 */
	private SAMRecord recordFromFastq(FastqRecord record, SAMFileHeader header) {
		// generate record that already have everything set to undefined (flag is 0, so it is important to change this)
		SAMRecord toReturn = new SAMRecord(header);
		// set the name for the record
		toReturn.setReadName(record.getReadHeader().replaceAll("/[12]", ""));
		// set the bases
		toReturn.setReadString(record.getReadString());
		// set the qualities
		toReturn.setBaseQualityString(record.getBaseQualityString());
		// set unmapped flag
		toReturn.setReadUnmappedFlag(true);
		return toReturn;
	}

}
