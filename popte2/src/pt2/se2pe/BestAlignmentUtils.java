package pt2.se2pe;

import htsjdk.samtools.SAMRecord;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Class with static methods for compute the best alignment.
 * Deprecated methods are not in use
 * TODO: unit tests
 *
 *
 * @author Daniel Gómez-Sánchez
 */
public class BestAlignmentUtils {

	/**
	 * Default best alignment algorithm
	 * Current implementation use a multi feature approach to determine the best alignment (see class MultifeatureBestAlignment)
	 *
	 * @param alignmentList		the list of alignments for this fastq record
	 * @param maxAlignmentsSA	the maximum number of alignments to include in the SA tag
	 * @return					a copy of the best alignment updated
	 */
	public static SAMRecord getBestAlignment(LinkedList<SAMRecord> alignmentList, int maxAlignmentsSA) {
		return getBestAlignment(alignmentList, maxAlignmentsSA, new MultifeatureBestAlignment());
	}

	/**
	 * Use the alignment in the list that is the best and update the tags with the others
	 *
	 * @param alignmentList		the list of alignments for this fastq record
	 * @param maxAlignmentsSA	the maximum number of alignments to include in the SA tag
	 * @param checker			use this alignment checker to test which alignment is the best
	 * @return					a copy of the best alignment updated
	 */
	private static SAMRecord getBestAlignment(LinkedList<SAMRecord> alignmentList, int maxAlignmentsSA, BestAlignmentChecker checker) {
		// iterate over the rest of alingments to check the mapping quality
		Iterator<SAMRecord> it = alignmentList.iterator();
		// get the first index as the best
		int bestIndex = 0;
		SAMRecord bestAlignment = it.next();
		for(int i = 1; it.hasNext(); i++) {
			if(checker.check(it.next(), bestAlignment)){
				bestIndex = i;
			}
		}
		// If the best index is the first, it is already stored
		if(bestIndex == 0) {
			// remove it from the list
			alignmentList.pop();
		} else {
			// get the best alignment and remove it
			bestAlignment = alignmentList.remove(bestIndex);
		}
		// return the best alignment with the tags added
		return updateBestAligment(bestAlignment, alignmentList, maxAlignmentsSA);
	}

	/**
	 * Use the first alignment from the list as the best alignment, and update the tags with the others
	 *
	 * @param alignmentList         the list with all the alignments
	 * @param maxAlignmentsSA		the maximum number of alignments to include in the SA tag
	 * @return						a copy of the best alignment updated
	 */
	@Deprecated
	public static SAMRecord firstAlignmentBest(LinkedList<SAMRecord> alignmentList, int maxAlignmentsSA) {
		// Usually the first record is the best alignment
		SAMRecord bestAlignment = alignmentList.pop();
		// return the best alignment with the tags added
		return updateBestAligment(bestAlignment, alignmentList, maxAlignmentsSA);
	}

	/**
	 * Return a copy of the best alignment updated with the rest of the aligments information: includes the X0 tag and SA if there are less than the maximum number of alignments
	 *
	 * @param bestAlignment			the best alignment
	 * @param restOfAlignments		the rest of the alignments
	 * @param maxAlignmentsSA		the maximum number of alignments to include in the SA tag
	 * @return						an upadated copy of the alignment
	 */
	private static SAMRecord updateBestAligment(SAMRecord bestAlignment, LinkedList<SAMRecord> restOfAlignments, int maxAlignmentsSA) {
		// get the number of alignments
		int numberOfAlignments = restOfAlignments.size();
		// this should not throw the error, but it is safer
		SAMRecord toRet;
		try {
			// copy the record
			toRet = (SAMRecord) bestAlignment.clone();
		} catch(Exception e) {
			throw new RuntimeException("Unexpected exception when updatinf best alignment from "+bestAlignment);
		}
		// Update the X0:i tag
		toRet.setAttribute("X0", numberOfAlignments);
		// Only update the SA tag if there are less alternative alignments than the expected
		if(numberOfAlignments-1 <= maxAlignmentsSA)  {
			String tagSA = generateTagSA(restOfAlignments);
			toRet.setAttribute("SA", tagSA);
		}
		return toRet;
	}

	/**
	 * Generate the SA tag from the set of alignments. The format is a semicolon delimited list, with the following information: rname,pos,strand,CIGAR,mapQ,NM
	 *
	 * @param alignmentList		the set of non-best hit
	 * @return					the formed tag
	 */
	private static String generateTagSA(LinkedList<SAMRecord> alignmentList) {
		StringBuilder tagBuilder = new StringBuilder();
		for(SAMRecord record: alignmentList) {
			// add the reference name
			tagBuilder.append(record.getReferenceName()); tagBuilder.append(",");
			// add the position
			tagBuilder.append(record.getUnclippedStart());tagBuilder.append(",");
			// add the strand
			if(record.getReadNegativeStrandFlag()) {
				tagBuilder.append("-");
			} else {
				tagBuilder.append("+");
			}
			tagBuilder.append(",");
			// add the CIGAR
			tagBuilder.append(record.getCigarString());tagBuilder.append(",");
			// add the mapping quality
			tagBuilder.append(record.getMappingQuality());tagBuilder.append(",");
			// add the NM tag
			Integer tagNM = record.getIntegerAttribute("NM");
			if(tagNM == null) {
				tagBuilder.append("NA");
			} else {
				tagBuilder.append(tagNM);
			}
			tagBuilder.append(";");
		}
		return tagBuilder.toString();
	}

	/**
	 * Interface to check if the alignment is the best
	 */
	private static interface BestAlignmentChecker {

		/**
		 * Test if the test record is best than the best record
		 *
		 * @param test	the testing record
		 * @param best	the current best record
		 * @return		true if the record test record is best than the best record, false otherwise
		 */
		public abstract boolean check(SAMRecord test, SAMRecord best);
	}

	/**
	 * Use the next features in order, and if there is a draw use the next one to test if the alignment is the best
	 * 	1. Alignment score
	 * 	2. Mapping quality
	 * 	3. Number of mismatches
	 */
	private static class MultifeatureBestAlignment implements BestAlignmentChecker {
		@Override
		public boolean check(SAMRecord test, SAMRecord best) {
			if(best.getIntegerAttribute("AS") < test.getIntegerAttribute("AS")) {
				return true;
			} else if(best.getIntegerAttribute("AS") == test.getIntegerAttribute("AS")) {
				if(best.getMappingQuality() < test.getMappingQuality()) {
					return true;
				}
			}
			return (best.getMappingQuality() == test.getMappingQuality()) && best.getIntegerAttribute("NM") < test.getIntegerAttribute("NM");
		}
	}

	/**
	 * Use only the mapping quality as the best alignment
	 */
	@Deprecated
	private static class MappingQualityBestAlignment implements BestAlignmentChecker {
		@Override
		public boolean check(SAMRecord test, SAMRecord best) {
			return best.getMappingQuality() < test.getMappingQuality();
		}
	}

	/**
	 * Use only the alignment score
	 */
	@Deprecated
	private static class AlignmentScoreBestAlignment implements BestAlignmentChecker {
		@Override
		public boolean check(SAMRecord test, SAMRecord best) {
			return best.getIntegerAttribute("AS") < test.getIntegerAttribute("AS");
		}
	}

}
