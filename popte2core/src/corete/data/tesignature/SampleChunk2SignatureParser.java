package corete.data.tesignature;

import corete.data.SignatureDirection;
import corete.data.TEFamilyShortcutTranslator;
import corete.data.TEStrand;
import corete.data.ppileup.PpileupSampleSummary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by robertkofler on 12/30/15.
 */
public class SampleChunk2SignatureParser {

	private final HashMap<Integer, PpileupSampleSummary> hsms;
	private final int start;
	private final int end;
	private final String chromosome;
	private final TEFamilyShortcutTranslator translator;

	public SampleChunk2SignatureParser(HashMap<Integer, PpileupSampleSummary> hsms, String chromosome, int start, int end, TEFamilyShortcutTranslator translator) {
		this.hsms = new HashMap<Integer, PpileupSampleSummary>(hsms);
		this.start = start;
		this.end = end;
		this.translator = translator;
		this.chromosome = chromosome;
	}


	/**
	 * Get the range signatures for one specific TE family and one specific sample id (popindex)
	 *
	 * @param teshortcut
	 * @param popindex
	 * @return
	 */
	public ArrayList<SignatureRangeInfo> getRanges(String teshortcut, int popindex, int windowsize, int valleysize, double mincount) {
		return getTEspecificSampleSignatures(hsms, teshortcut, popindex, windowsize, valleysize, mincount);
	}


	/**
	 * For one TE in a given sample
	 *
	 * @param sample
	 * @return
	 */
	private ArrayList<SignatureRangeInfo> getTEspecificSampleSignatures(HashMap<Integer, PpileupSampleSummary> sample, String teshortcut, int popindex, int windowsize, int valleysize, double mincount) {
		ArrayList<SignatureRangeInfo> toret = new ArrayList<SignatureRangeInfo>();

		LinkedList<ScoreHelper> window = new LinkedList<ScoreHelper>();

		int lastMaxStart = -1;
		int lastMaxPosition = -1;
		double lastMaxSupport = 0.0;
		double runningsum = 0.0;
		int minstretchcount = 0;

		int rangeStart = -1;
		int rangeEnd = -1;
		double winStartScore = 0.0;
		double winEndScore = 0.0;

		for (int i = start; i <= end; i++) {

			int tecount = sample.get(i).getTEcount(teshortcut);
			runningsum += tecount;
			window.add(new ScoreHelper(i, tecount));
			if (window.size() > windowsize) {
				ScoreHelper pop = window.remove(0);
				runningsum -= pop.score;
			}
			if (window.size() != windowsize) continue;

			double av = runningsum / (double) windowsize;
			if (av >= mincount) {
				if (rangeStart == -1) {
					rangeStart = window.getFirst().position;
				}
				rangeEnd = window.getLast().position;

				// larger than the mincount
				if (av >= lastMaxSupport) {
					// larger than the previous maximum count

					lastMaxStart = window.getFirst().position;
					lastMaxSupport = av;
					lastMaxPosition = i;
					winStartScore = window.getFirst().score;
					winEndScore = window.getLast().score;

				}
				minstretchcount = 0;

			} else {
				// lower than the mincount
				minstretchcount++;


				if (lastMaxPosition != -1 && minstretchcount >= (valleysize + windowsize - 1)) {   // we had a previous highscore => signify (create signature)


					SignatureRangeInfo sri = signify(lastMaxStart, lastMaxPosition, lastMaxSupport, teshortcut, popindex, rangeStart, rangeEnd, winStartScore, winEndScore,lastMaxSupport);
					toret.add(sri);
					lastMaxSupport = 0.0;
					lastMaxPosition = -1;
					lastMaxStart = -1;
				}
				if (minstretchcount >= (valleysize + windowsize - 1)) {
					rangeStart = -1;
					rangeEnd = -1;
				}


			}


		}
		// also process the last  // if a last exists
		if (lastMaxPosition != -1) {


			SignatureRangeInfo sri = signify(lastMaxStart, lastMaxPosition, lastMaxSupport, teshortcut, popindex, rangeStart, rangeEnd, winStartScore, winEndScore,lastMaxSupport);
			toret.add(sri);
		}
		return toret;

	}

	private SignatureRangeInfo signify(int start, int end, double support, String shortcut, int popindex, int rangeStart, int rangeEnd, double winStartScore, double winEndScore, double averageWinScore) {

		PopulationID popid = new PopulationID(new ArrayList<Integer>(Arrays.asList(popindex)));
		// forward = uppercase is end
		// reverse = lowercase is start
		SignatureDirection sigDir = translator.getSignatureDirection(shortcut);
		String tefamily = translator.getFamilyname(shortcut);


		InsertionSignature sig = new InsertionSignature(popid, this.chromosome, sigDir, start, end, tefamily, TEStrand.Unknown);
		SignatureRangeInfo sri = new SignatureRangeInfo(sig, rangeStart, rangeEnd, winStartScore, winEndScore,averageWinScore);
		return sri;

	}


	private class ScoreHelper {
		public int position;
		public double score;

		ScoreHelper(int position, double score) {
			this.position = position;
			this.score = score;
		}


	}
}
