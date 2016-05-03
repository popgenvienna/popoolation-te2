package pt2.ppileup;

import pt2.CommandFormater;

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
			boolean homogenizeReads=false;
			boolean extendclipped=false;

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
				else if(cu.equals("--hier"))
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
				else if(cu.equals("--extend-clipped"))
				{
					extendclipped=true;
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
				else if(cu.equals("--homogenize-pairs"))
				{
					homogenizeReads=true;
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
			if(inputFiles.size()<1 || outputFile.equals("") || hierFile.equals(""))
			{
				printHelp();
				System.exit(1);
			}


			// Create a logger
			Logger logger=corete.misc.LogFactory.getLogger(detailedLog);
			PpileupFramework pfp =new PpileupFramework(inputFiles,outputFile,hierFile,mapQual,srmd,idof,shortcuts,homogenizeReads,extendclipped, zippedOutput, logger);
			pfp.run();
		}

		public static void printHelp()
		{
			StringBuilder sb=new StringBuilder();
			sb.append("create a physical pileup file from one or multiple bam files\n\n");
			sb.append("== Main parameters ==\n");
			sb.append(CommandFormater.format("--bam", "a bam file; multiple files may be specified",true));
			sb.append(CommandFormater.format("--map-qual","minimum mapping quality", "15"));
			sb.append(CommandFormater.format("--hier","TE hierarchy file",true));
			sb.append(CommandFormater.format("--output","the output file",true));
			sb.append(CommandFormater.format("--help","show help",null));
			sb.append("\n");
			sb.append("== Parameters for fine tuning =="+"\n");

			sb.append(CommandFormater.format("--homogenize-pairs","subsample number of read pairs to equal levels among bam files",false));
			sb.append(CommandFormater.format("--te-shortcuts","use a predefined list of TE family shortcuts [file-path]",null));
			sb.append(CommandFormater.format("--disable-zipped","disable zipped output",null));
			sb.append(CommandFormater.format("--sr-mindist","minimum inner distance for structural rearrangements","10000"));
			sb.append(CommandFormater.format("--id-up-quant","paired end fragments with an insert size in the upper quantile will be ignored [fraction]","0.01"));
			sb.append(CommandFormater.format("--detailed-log","show a detailed event log",null));
			sb.append("\nSee the online manual for detailed description of the parameters\n");
			System.out.print(sb.toString());

		}
}
