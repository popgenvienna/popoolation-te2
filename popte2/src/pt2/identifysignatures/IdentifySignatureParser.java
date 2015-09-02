package pt2.identifysignatures;

import java.util.LinkedList;
import java.util.logging.Logger;


/**
 * Created by robertkofler on 6/29/15.
 */
public class IdentifySignatureParser {


		public static void parseCommandline(LinkedList<String> args)
		{

			String inputFile="";
			SignatureIdentificationMode mode=null;
			String outputFile="";
			int mincoverage=10;
			int mincount=2;
			int maxrearangements=2;
			int maxothertes=2;
			Integer fixedinsertsize=null;
			int chunkdistance=2;
			boolean detailedLog=false;



			while(args.size()>0)
			{
				String cu=args.remove(0);

				if(cu.equals("--ppileup"))
				{
					inputFile=args.remove(0);
				}
				else if(cu.equals("--mode"))
				{
					mode= getMode(args.remove(0));
				}
				else if(cu.equals("--output"))
				{
					outputFile=args.remove(0);
				}
				else if(cu.equals("--min-coverage"))
				{
					mincoverage=Integer.parseInt(args.remove(0));
				}
				else if(cu.equals("--min-count"))
				{
					mincount=Integer.parseInt(args.remove(0));
				}
				else if(cu.equals("--max-rearrangement"))
				{
					maxrearangements = Integer.parseInt(args.remove(0));
				}
				else if(cu.equals("--max-othertes"))
				{
					maxothertes=Integer.parseInt(args.remove(0));
				}
				else if(cu.equals("--fixed-insertsize"))
				{
					fixedinsertsize = Integer.parseInt(args.remove(0));
				}
				else if(cu.equals("--chunk-distance"))
				{
					chunkdistance = Integer.parseInt(args.remove(0));
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
			if(inputFile.equals("") || outputFile.equals("") || mode==null)
			{
				printHelp();
				System.exit(1);
			}


			Logger logger=corete.misc.LogFactory.getLogger(detailedLog);
			IdentifySignatureFramework isf =new IdentifySignatureFramework(inputFile,outputFile,mode,mincoverage,mincount,maxrearangements,maxothertes,
					fixedinsertsize,chunkdistance,detailedLog,logger);
			isf.run();
		}

	private static SignatureIdentificationMode getMode(String input)
	{
		if(input.toLowerCase().equals("joint"))
		{
			return SignatureIdentificationMode.Joint;
		}
		else if(input.toLowerCase().equals("separate"))
		{
			return SignatureIdentificationMode.Separate;
		}
		else throw new IllegalArgumentException("Unknown mode "+input);
	}


		private static void printHelp()
		{
			StringBuilder sb=new StringBuilder();
			sb.append("identify signatures of TE insertions\n");
			sb.append("populations may be analysed jointly or separately\n\n");
			sb.append("== Main parameters ==\n");
			sb.append(String.format("%-22s%s","--ppileup","input ppileup file; Mandatory\n"));
			sb.append(String.format("%-22s%s","--mode","joint or separate; Mandatory\n"));
			sb.append(String.format("%-22s%s","--output","TE insertion signatures; Mandatory\n"));
			sb.append(String.format("%-22s%s","--min-coverage","the minimum coverage of the output file; default=10\n"));
			sb.append(String.format("%-22s%s","--min-count","the minimum count of a TE insertion; default=2\n"));
			sb.append(String.format("%-22s%s","--help","show help\n"));
			sb.append("\n");
			sb.append("== Parameters for fine tuning =="+"\n");
			sb.append(String.format("%-22s%s","--max-rearrangement","maximum counts of rearrangements; default=2\n"));
			sb.append(String.format("%-22s%s","--max-othertes","maximum counts of overlapping TE families; default=2\n"));
			sb.append(String.format("%-22s%s","--fixed-insertsize","proceed with a fixed insert size for all populations; default=None\n"));
			sb.append(String.format("%-22s%s","--chunk-distance","minimum distance between chromosomal chunks; default=5\n"));
			sb.append(String.format("%-22s%s","--detailed-log","show a detailed event log\n"));
			sb.append("See the online manual for detailed description of the parameters\n");
			System.out.print(sb.toString());

		}
}
