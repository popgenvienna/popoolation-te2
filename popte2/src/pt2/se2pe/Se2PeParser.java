package pt2.se2pe;

import corete.misc.LogFactory;
import pt2.CommandFormater;

import java.io.File;
import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 10/29/15.
 */
public class Se2PeParser {
	private static final String taskName = "se2pe";

	public static void parseCommandLine(LinkedList<String> args) {
		// Parsing the commands
		String fastq1 = null;
		String fastq2 = null;
		String bam1 = null;
		String bam2 = null;
		String output = null;
		boolean detailedLog = false;
		boolean sort = false;
		boolean index = false;
		boolean debug=false;

		for (int i = 0; i < args.size(); i++) {
			switch (args.get(i)) {
				case "--fastq1":
					fastq1 = args.get(++i);
					break;
				case "--fastq2":
					fastq2 = args.get(++i);
					break;
				case "--bam1":
					bam1 = args.get(++i);
					break;
				case "--bam2":
					bam2 = args.get(++i);
					break;
				case "--output":
					output = args.get(++i);
					break;
				case "--detailed-log":
					detailedLog = true;
					break;
				case "--sort":
					sort = true;
					break;
				case "--index":
					index = true;
					break;
				case "--debug":
					debug = true;
					break;
				default:
					System.out.println("Do not recognize command line option " + args.get(i));
					System.exit(1);
			}
		}
		// Create the logger
		Logger logger = LogFactory.getLogger(detailedLog);
		if (debug) {
			logger.warning("Debugging mode on");
		}
		// Check if the required inputs are set
		if (fastq1 == null || fastq2 == null || bam1 == null || bam2 == null || output == null) {
			System.out.println("All main parameters are required\n");
			printHelp();
			System.exit(1);

		}

		Se2PeFramework s2p=new Se2PeFramework(fastq1,fastq2,bam1,bam2,output,sort,index,logger);
		s2p.run();





	}


	public static void printHelp()
	{
		StringBuilder sb=new StringBuilder();
		sb.append("Merge two sam/bam files and crosslink the paired-end information\n\n");

		sb.append("== Main parameters ==\n");
		sb.append(CommandFormater.format("--fastq1", "fastq file with first read pairs (gziped allowed)", true));
		sb.append(CommandFormater.format("--fastq2", "fastq file with second read pairs (gziped allowed)",true));
		sb.append(CommandFormater.format("--bam1", "bam/sam file with first read pairs",true));
		sb.append(CommandFormater.format("--bam2", "bam/sam file with second read pairs",true));
		sb.append(CommandFormater.format("--output", "Output file",true));
		sb.append("\n");
		sb.append("== Parameters for fine tuning ==\n");
		// this three following options comes from the bwa sampe program
		sb.append(CommandFormater.format("--sort", "Sort the output file", false));
		sb.append(CommandFormater.format("--index", "Create an index for the output file (sort will be set by default)", null));
		sb.append(CommandFormater.format("--detailed-log", "Show a detailed event log", false));
		sb.append("\nSee the online manual for detailed description of the parameters\n");
		System.out.println(sb.toString());


	}

}
