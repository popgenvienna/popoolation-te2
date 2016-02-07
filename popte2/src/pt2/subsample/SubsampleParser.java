package pt2.subsample;

import pt2.CommandFormater;
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
			boolean withReplace=false;

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
				else if(cu.equals("--with-replace"))
				{
					withReplace=true;
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
			SubsampleFramework sf =new SubsampleFramework(inputFile,outputFile,targetCoverage,withReplace,zippedOutput,logger);
			sf.run();
		}

		public static void printHelp()
		{
			StringBuilder sb=new StringBuilder();
			sb.append("subsample a ppileup file to uniform coverage\n\n");
			sb.append("== Main parameters ==\n");
			sb.append(CommandFormater.format("--ppileup","input ppileup file",true));
			sb.append(CommandFormater.format("--output","output ppileup file",true));
			sb.append(CommandFormater.format("--target-coverage","the target coverage of the output file [int]",true));
			sb.append(CommandFormater.format("--help","show help",null));
			sb.append("\n");
			sb.append("== Parameters for fine tuning =="+"\n");
			sb.append(CommandFormater.format("--with-replace","use sampling with replacement instead of without replacement",null));
			sb.append(CommandFormater.format("--disable-zipped","disable zipped output",null));
			sb.append(CommandFormater.format("--detailed-log","show a detailed event log",null));
			sb.append("See the online manual for detailed description of the parameters\n");
			System.out.print(sb.toString());

		}
}
