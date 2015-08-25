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
			int srmd=10000;
			float idof=0.01F;
			String shortcuts="";
			boolean zippedOutput=true;

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
					     mapQual = Integer.parseInt(args.remove(0));
				}
				else if(cu.equals("--detailed-log"))
				{
					detailedLog=true;
				}
				else if(cu.equals("--te-shortcuts"))
				{
					    shortcuts=args.remove(0);
				}
				else if(cu.equals("--sr-mindist"))
				{
					srmd=Integer.parseInt(args.remove(0));
				}
				else if(cu.equals("--id-up-quant"))
				{
					  idof=  Float.parseFloat(args.remove(0));
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
			if(inputFiles.size()<1 || outputFile=="" || hierFile=="")
			{
				printHelp();
				System.exit(1);
			}


			// Create a logger
			Logger logger=corete.misc.LogFactory.getLogger(detailedLog);
			PpileupFramework pfp =new PpileupFramework(inputFiles,outputFile,hierFile,mapQual,srmd,idof,shortcuts,zippedOutput, logger);
			pfp.run();
		}

		public static void printHelp()
		{
			StringBuilder sb=new StringBuilder();
			sb.append("create a physical pileup file from one or multiple bam files\n\n");
			sb.append("== Main parameters ==\n");
			sb.append(String.format("%-22s%s","--bam","bam files; multiple may be specified\n"));
			sb.append(String.format("%-22s%s","--map-qual","minimum mapping quality; default=15\n"));
			sb.append(String.format("%-22s%s","--hier","TE hierarchy file\n"));
			sb.append(String.format("%-22s%s","--output","the output file\n"));
			sb.append(String.format("%-22s%s","--help","show help\n"));
			sb.append("\n");
			sb.append("== Parameters for fine tuning =="+"\n");
			sb.append(String.format("%-22s%s","--te-shortcuts","use a predefined list of TE family shortcuts; default=\"\"\n"));
			sb.append(String.format("%-22s%s","--disable-zipped","flag; disable zipped output\n"));
			sb.append(String.format("%-22s%s","--sr-mindist","structural rearrangement minimum distance; default=10000\n"));
			sb.append(String.format("%-22s%s","--id-up-quant","upper quantile of inner distance will be ignored; [fraction] default=0.01\n"));
			sb.append(String.format("%-22s%s","--detailed-log","show a detailed event log\n"));
			sb.append("See the online manual for detailed description of the parameters\n");
			System.out.print(sb.toString());

		}
}
