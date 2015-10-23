package pt2.filtersignatures;

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
			sb.append("filter signatures of TE insertions; criteria apply to every sample\n");
			sb.append("== Main parameters ==\n");
			sb.append(String.format("%-22s%s","--input","the signatures of TE insertions; Mandatory\n"));
			sb.append(String.format("%-22s%s","--output","TE insertion signatures; Mandatory\n"));
			sb.append(String.format("%-22s%s","--min-coverage","the minimum coverage; default=0.0\n"));
			sb.append(String.format("%-22s%s","--max-coverage","the maximum coverage; default=null\n"));
			sb.append(String.format("%-22s%s","--min-count","minimum required support for a TE; default=0.0\n"));
			sb.append(String.format("%-22s%s","--max-otherte-count","maximum allowed support for other TEs; default=null\n"));
			sb.append(String.format("%-22s%s","--max-structvar-count","maximum allowed support for structural variants; default=null\n"));
			sb.append(String.format("%-22s%s","--min-fraction","minimum required frequency for a TE; default=0.0\n"));
			sb.append(String.format("%-22s%s","--max-otherte-fraction","maximum allowed frequency for other TEs; default=1.0\n"));
			sb.append(String.format("%-22s%s","--max-structvar-fraction","maximum allowed frequency for structural variants; default=1.0\n"));

			sb.append(String.format("%-22s%s","--help","show help\n"));
			sb.append("\n");
			sb.append("== Parameters for fine tuning =="+"\n");
			sb.append(String.format("%-22s%s","--detailed-log","show a detailed event log\n"));
			sb.append("See the online manual for detailed description of the parameters\n");
			System.out.print(sb.toString());

		}
}
