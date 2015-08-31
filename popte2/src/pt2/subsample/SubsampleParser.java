package pt2.subsample;

import pt2.ppileup.PpileupFramework;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Logger;


/**
 * Created by robertkofler on 6/29/15.
 */
public class SubsampleParser {


		public static void parseCommandline(LinkedList<String> args)
		{

			String inputFile="";
			boolean detailedLog=false;
			String outputFile="";
			int targetCoverage=0;
			boolean zippedOutput=true;

			while(args.size()>0)
			{
				String cu=args.remove(0);

				if(cu.equals("--ppileup"))
				{
					inputFile=args.remove(0);
				}
				else if(cu.equals("--output"))
				{
					outputFile=args.remove(0);
				}
				else if(cu.equals("--target-coverage"))
				{
					targetCoverage=Integer.parseInt(args.remove(0));
				}
				else if(cu.equals("--detailed-log"))
				{
					detailedLog=true;
				}
				else if(cu.equals("--disable-zipped"))
				{
					zippedOutput=false;
				}
				else if(cu.equals("--help"))
				{
					printHelp();
					System.exit(0);
				}
				else
				{
					System.out.println("Do not recognize command line option "+cu);
					printHelp();
					System.exit(1);

				}
			}
			if(inputFile.equals("") || outputFile.equals("") || targetCoverage==0)
			{
				printHelp();
				System.exit(1);
			}


			// Create a logger
			Logger logger=corete.misc.LogFactory.getLogger(detailedLog);
			SubsampleFramework sf =new SubsampleFramework(inputFile,outputFile,targetCoverage,zippedOutput,logger);
			sf.run();
		}

		public static void printHelp()
		{
			StringBuilder sb=new StringBuilder();
			sb.append("create a physical pileup file from one or multiple bam files\n\n");
			sb.append("== Main parameters ==\n");
			sb.append(String.format("%-22s%s","--ppileup","input ppileup file\n"));
			sb.append(String.format("%-22s%s","--output","output ppileup file\n"));
			sb.append(String.format("%-22s%s","--target-coverage","the target coverage of the output file\n"));
			sb.append(String.format("%-22s%s","--help","show help\n"));
			sb.append("\n");
			sb.append("== Parameters for fine tuning =="+"\n");
			sb.append(String.format("%-22s%s","--disable-zipped","flag; disable zipped output\n"));;
			sb.append(String.format("%-22s%s","--detailed-log","show a detailed event log\n"));
			sb.append("See the online manual for detailed description of the parameters\n");
			System.out.print(sb.toString());

		}
}
