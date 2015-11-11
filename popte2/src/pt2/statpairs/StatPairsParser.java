package pt2.statpairs;

import pt2.CommandFormater;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Logger;


/**
 * Created by robertkofler on 6/29/15.
 */
public class StatPairsParser {


		public static void parseCommandline(LinkedList<String> args)
		{

			ArrayList<String> inputFiles= new ArrayList<String>();
			boolean detailedLog=false;
			String outputFile="";
			int mapQual=0;
			String hierFile="";
			int srmd=10000;


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
				else if(cu.equals("--sr-mindist"))
				{
					srmd=Integer.parseInt(args.remove(0));
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
			if(inputFiles.size()<1 || outputFile.equals("") || hierFile.equals(""))
			{
				printHelp();
				System.exit(1);
			}


			// Create a logger
			Logger logger=corete.misc.LogFactory.getLogger(detailedLog);
			StatPairsFramework pfp =new StatPairsFramework(inputFiles,outputFile,hierFile,mapQual,srmd,logger);
			pfp.run();
		}

		public static void printHelp()
		{
			StringBuilder sb=new StringBuilder();
			sb.append("computes mapping statistics; paired-end supporting a TE insertion, proper-pairs and structural rearrangements\n\n");
			sb.append("== Main parameters ==\n");
			sb.append(CommandFormater.format("--bam", "a bam file; multiple files may be specified",true));
			sb.append(CommandFormater.format("--map-qual","minimum mapping quality", "0"));
			sb.append(CommandFormater.format("--hier","TE hierarchy file",true));
			sb.append(CommandFormater.format("--output","the output file",true));
			sb.append(CommandFormater.format("--help","show help",null));
			sb.append("\n");
			sb.append("== Parameters for fine tuning =="+"\n");
			sb.append(CommandFormater.format("--sr-mindist","minimum inner distance for structural rearrangements","10000"));
			sb.append(CommandFormater.format("--detailed-log","show a detailed event log",null));
			sb.append("\nSee the online manual for detailed description of the parameters\n");
			System.out.print(sb.toString());

		}
}
