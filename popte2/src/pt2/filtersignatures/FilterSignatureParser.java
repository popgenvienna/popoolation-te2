package pt2.filtersignatures;

import pt2.CommandFormater;

import java.util.LinkedList;
import java.util.logging.Logger;


/**
 * Created by robertkofler on 6/29/15.
 */
public class FilterSignatureParser {


		public static void parseCommandline(LinkedList<String> args)
		{


			String signature="";
			String outputFile="";

			double mincoverage=0;
			Double maxcoverage=null;

			double minsupport=0.0;
			Double maxotherte=null;
			Double maxstructvar=null;

			double minsupportfraction=0.0;
			double maxothertefraction=1.0;
			double maxstructvarfraction=1.0;

			boolean detailedLog=false;


			while(args.size()>0)
			{
				String cu=args.remove(0);
				if(cu.equals("--input"))
				{
					signature= args.remove(0);
				}
				else if(cu.equals("--output"))
				{
					outputFile=args.remove(0);
				}
				else if(cu.equals("--min-coverage"))
				{
					mincoverage=Double.parseDouble(args.remove(0));
				}
				else if(cu.equals("--max-coverage"))
				{
					maxcoverage=Double.parseDouble(args.remove(0));
				}
				else if(cu.equals("--min-count"))
				{
					minsupport=Double.parseDouble(args.remove(0));
				}
				else if(cu.equals("--max-otherte-count"))
				{
					maxotherte=Double.parseDouble(args.remove(0));
				}
				else if(cu.equals("--max-structvar-count"))
				{
					maxstructvar=Double.parseDouble(args.remove(0));
				}
				else if(cu.equals("--min-fraction"))
				{
					minsupportfraction=Double.parseDouble(args.remove(0));
				}
				else if(cu.equals("--max-otherte-fraction"))
				{
					maxothertefraction=Double.parseDouble(args.remove(0));
				}
				else if(cu.equals("--max-structvar-fraction"))
				{
					maxstructvarfraction=Double.parseDouble(args.remove(0));
				}
				else if(cu.equals("--help"))
				{
					printHelp();
					System.exit(0);
				}
				else if(cu.equals("--detailed-log"))
				{
					detailedLog=true;
				}
				else
				{
					System.out.println("Do not recognize command line option "+cu);
					printHelp();
					System.exit(1);

				}
			}
			if(signature.equals("") || outputFile.equals("") )
			{
				printHelp();
				System.exit(1);
			}



			Logger logger=corete.misc.LogFactory.getLogger(detailedLog);
			FilterSignatureFramework isf =new FilterSignatureFramework(signature ,outputFile, mincoverage,
			 maxcoverage, minsupport, maxotherte, maxstructvar,  minsupportfraction,
			 maxothertefraction,  maxstructvarfraction, detailedLog,logger);
			isf.run();
		}





		private static void printHelp()
		{
			/*
				else if(cu.equals("--min-coverage"))
				else if(cu.equals("--max-coverage"))
				else if(cu.equals("--min-count"))
				else if(cu.equals("--max-otherte-count"))
				else if(cu.equals("--max-structvar-count"))
				else if(cu.equals("--min-fraction"))
				else if(cu.equals("--max-otherte-fraction"))
				else if(cu.equals("--max-structvar-fraction"))
			 */
			StringBuilder sb=new StringBuilder();
			sb.append("filter signatures of TE insertions; criteria apply to every sample\n\n");
			sb.append("== Main parameters ==\n");
			sb.append(CommandFormater.format("--input", "the signatures of TE insertions",true));
			sb.append(CommandFormater.format("--output","TE insertion signatures",true));
			sb.append(CommandFormater.format("--min-coverage","the minimum coverage","0.0"));
			sb.append(CommandFormater.format("--max-coverage","the maximum coverage","+infinite"));
			sb.append(CommandFormater.format("--min-count","minimum required support for a TE","0.0"));
			sb.append(CommandFormater.format("--max-otherte-count","maximum allowed support for other TEs","+infinite"));
			sb.append(CommandFormater.format("--max-structvar-count","maximum allowed support for structural variants","+infinite"));
			sb.append(CommandFormater.format("--min-fraction","minimum required frequency for a TE","0.0"));
			sb.append(CommandFormater.format("--max-otherte-fraction","maximum allowed frequency for other TEs","1.0"));
			sb.append(CommandFormater.format("--max-structvar-fraction","maximum allowed frequency for structural variants","1.0"));
			sb.append(CommandFormater.format("--help","show help",null));
			sb.append("\n");
			sb.append("== Parameters for fine tuning =="+"\n");
			sb.append(CommandFormater.format("--detailed-log","show a detailed event log",null));
			sb.append("\nSee the online manual for detailed description of the parameters\n");
			System.out.print(sb.toString());

		}
}
