package pt2.ppileup;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Logger;


/**
 * Created by robertkofler on 6/29/15.
 */
public class PpileupParser {


		public static void parseCommandline(LinkedList<String> args)
		{

			ArrayList<String> inputFiles= new ArrayList<String>();
			boolean detailedLog=false;
			String outputFile="";
			int mapQual=15;
			String hierFile="";

			while(args.size()>0)
			{
				String cu=args.remove(0);

				if(cu.equals("--bam"))
				{
					inputFiles.add(args.remove(0));
				}
				else if(cu.equals("--output"))
				{
					outputFile=args.remove(0);
				}
				else if(cu.equals("--hier-file"))
				{
					hierFile=args.remove(0);
				}
				else if(cu.equals("--map-qual"))
				{
					     mapQual = Integer.getInteger(args.remove(0));
				}
				else if(cu.equals("--detailed-log"))
				{
					detailedLog=true;
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
			if(inputFiles.size()<1 || outputFile=="" || hierFile=="")
			{
				printHelp();
				System.exit(1);
			}


			// Create a logger
			Logger logger=corete.misc.LogFactory.getLogger(detailedLog);
			PpileupFramework pfp =new PpileupFramework(inputFiles,outputFile,hierFile,mapQual,logger);
			pfp.run();
		}

		public static void printHelp()
		{
			StringBuilder sb=new StringBuilder();
			sb.append("create a physical pileup file from one or multiple bam files\n\n");
			sb.append(String.format("%-22s%s","--bam","bam file\n"));
			sb.append(String.format("%-22s%s","--map-qual","minimum mapping quality; default=15\n"));
			sb.append(String.format("%-22s%s","--hier","TE hierarchy file\n"));
			sb.append(String.format("%-22s%s","--output","the output file\n"));
			sb.append(String.format("%-22s%s","--detailed-log","show a detailed event log\n"));
			sb.append(String.format("%-22s%s","--help","show the help\n"));
			System.out.print(sb.toString());

		}
}
