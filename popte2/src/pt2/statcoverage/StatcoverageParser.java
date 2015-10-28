package pt2.statcoverage;

import pt2.CommandFormater;

import java.util.LinkedList;
import java.util.logging.Logger;


/**
 * Created by robertkofler on 6/29/15.
 */
public class StatcoverageParser {


		public static void parseCommandline(LinkedList<String> args)
		{

			String inputFile="";
			boolean detailedLog=false;
			String outputFile="";

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
			if(inputFile.equals("") || outputFile.equals(""))
			{
				printHelp();
				System.exit(1);
			}


			// Create a logger
			Logger logger=corete.misc.LogFactory.getLogger(detailedLog);
			StatcoverageFramework sf =new StatcoverageFramework(inputFile,outputFile,logger);
			sf.run();
		}

		public static void printHelp()
		{
			StringBuilder sb=new StringBuilder();
			sb.append("Create coverage statistics for a mpileup file; May help to set a target coverage for subsampling\n\n");
			sb.append("== Main parameters ==\n");
			sb.append(CommandFormater.format("--ppileup", "input ppileup file",true));
			sb.append(CommandFormater.format("--output","output ppileup file",true));
			sb.append(CommandFormater.format("--help","show help",null));
			sb.append("\n");
			sb.append("== Parameters for fine tuning =="+"\n");
			sb.append(CommandFormater.format("--detailed-log","show a detailed event log",true));
			sb.append("\nSee the online manual for detailed description of the parameters\n");
			System.out.print(sb.toString());

		}
}
