package pt2.se2pe;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.logging.Logger;

/**
 * Progress logger for the crosslink tool
 * Modified from htsjdk.samtools.util.ProgressLogger but with the logging system from java.util.logging (used in this software) and to accept Crosslink objects instead of SAMRecords
 * TODO: unit tests
 *
 * @author Daniel Gómez-Sánchez
 */

public class CrosslinkProgressLogger {

	private final Logger log;
	private final int n;

	private final long startTime = System.currentTimeMillis();

	private final NumberFormat fmt = new DecimalFormat("#,###");
	private final NumberFormat timeFmt = new DecimalFormat("00");

	private long processed = 0;

	// Set to -1 until the first record is added
	private long lastStartTime = -1;

	/**
	 * Construct a progress logger.
	 * @param log the Log object to write outputs to
	 * @param n the frequency with which to output (i.e. every N records)
	 */
	public CrosslinkProgressLogger(final Logger log, final int n) {
		this.log = log;
		this.n = n;
	}

	/**
	 * Construct a progress logger with the desired log, with a period of 1m records.
	 *
	 * @param log the Log object to write outputs to
	 */
	public CrosslinkProgressLogger(final Logger log) { this(log, 1000000); }

	/**
	 * Logs the record
	 *
	 * @param rec the crosslink record
	 * @return	true if logger was triggered; false otherwise
	 */
	public synchronized boolean record(final Crosslink rec) throws CloneNotSupportedException {
		if (this.lastStartTime == -1) this.lastStartTime = System.currentTimeMillis();
		if (++this.processed % this.n == 0) {
			final long now = System.currentTimeMillis();
			final long lastPeriodSeconds = (now - this.lastStartTime) / 1000;
			this.lastStartTime = now;

			final long seconds = (System.currentTimeMillis() - startTime) / 1000;
			final String elapsed   = formatElapseTime(seconds);
			final String period    = pad(fmt.format(lastPeriodSeconds), 4);
			final String processed = pad(fmt.format(this.processed), 13);

			final String firstPairStr = String.format("%s:%s", rec.getFirstPair().getReferenceName(), fmt.format(rec.getFirstPair().getAlignmentStart()));
			final String secondPairStr = String.format("%s:%s", rec.getSecondPair().getReferenceName(), fmt.format(rec.getSecondPair().getAlignmentStart()));

			log.info(String.format("Crosslinked %s read pairs.\tElapsed time: %ss.\tTime for last %s: %ss.\tLast pair positions: %s-%s",
									processed, elapsed, fmt.format(this.n), period, firstPairStr, secondPairStr
			));
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Output the finishing log
	 */
	public void finishLog() {
		if (this.lastStartTime == -1) this.lastStartTime = System.currentTimeMillis();
		final long now = System.currentTimeMillis();
		final long lastPeriodSeconds = (now - this.lastStartTime) / 1000;
		this.lastStartTime = now;

		final long seconds = (System.currentTimeMillis() - startTime) / 1000;
		final String elapsed   = formatElapseTime(seconds);
		final String period    = pad(fmt.format(lastPeriodSeconds), 4);
		final String processed = pad(fmt.format(this.processed), 13);

		log.info(String.format("Crosslinked %s read pairs.\tElapsed time: %ss.\tFinished.",
			processed, elapsed
		));
	}

	/** Returns the count of records processed. */
	public long getCount() { return this.processed; }

	/** Returns the number of seconds since progress tracking began. */
	public long getElapsedSeconds() { return (System.currentTimeMillis() - this.startTime) / 1000; }

	/** Left pads a string until it is at least the given length. */
	private String pad (String in, final int length) {
		while (in.length() < length) {
			in = " " + in;
		}

		return in;
	}

	/** Formats a number of seconds into hours:minutes:seconds. */
	private String formatElapseTime(final long seconds) {
		final long s = seconds % 60;
		final long allMinutes = seconds / 60;
		final long m = allMinutes % 60;
		final long h = allMinutes / 60;

		return timeFmt.format(h) + ":" + timeFmt.format(m) + ":" + timeFmt.format(s);
	}
}
