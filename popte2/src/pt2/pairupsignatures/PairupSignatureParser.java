package pt2.pairupsignatures;

import java.util.LinkedList;
import java.util.logging.Logger;


/**
 * Created by robertkofler on 6/29/15.
 */
public class PairupSignatureParser {


		public static void parseCommandline(LinkedList<String> args)
		{

			String refgenome="";
			String hierfile="";
			String signature="";
			String outputFile="";

			int maxDistance=500;
			int minDistance=-100;
			double maxfreqdiff=0.5;



			boolean detailedLog=false;



			while(args.size()>0)
			{
				String cu=args.remove(0);

				if(cu.equals("--signature"))
				{
					signature=args.remove(0);
				}
				else if(cu.equals("--ref-genome"))
				{
					refgenome= args.remove(0);
				}
				else if(cu.equals("--hier-file"))
				{
					hierfile=args.remove(0);
				}
				else if(cu.equals("--output"))
				{
					outputFile=args.remove(0);
				}
				else if(cu.equals("--min-distance"))
				{
					minDistance=Integer.parseInt(args.remove(0));
				}
				else if(cu.equals("--max-distance"))
				{
					maxDistance=Integer.parseInt(args.remove(0));
				}
				else if(cu.equals("--max-freq-diff"))
				{
					  maxfreqdiff=Double.parseDouble(args.remove(0));
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
			if(refgenome.equals("") ||signature.equals("") || outputFile.equals("") ||hierfile.equals(""))
			{
				printHelp();
				System.exit(1);
			}


			Logger logger=corete.misc.LogFactory.getLogger(detailedLog);
			PairupSignatureFramework isf =new PairupSignatureFramework(signature, refgenome, hierfile, outputFile,
					minDistance,maxDistance,maxfreqdiff,detailedLog,logger);
			isf.run();
		}





		private static void printHelp()
		{


			StringBuilder sb=new StringBuilder();
			sb.append("pairs up signatures of TE insertions and yields TE insertions\n");
			sb.append("== Main parameters ==\n");
			sb.append(String.format("%-22s%s","--signature","the signatures of TE insertions; Mandatory\n"));
			sb.append(String.format("%-22s%s","--ref-genome","the RepeatMasked reference genome; Mandatory\n"));
			sb.append(String.format("%-22s%s","--hier-file","the TE hierarchy; Mandatory\n"));
			sb.append(String.format("%-22s%s","--output","TE insertion signatures; Mandatory\n"));
			sb.append(String.format("%-22s%s","--min-distance","the minimum distance between signatures; default=-100\n"));
			sb.append(String.format("%-22s%s","--max-distance","the maximum distance between signatures; default=500\n"));
			sb.append(String.format("%-22s%s","--max-freq-diff","the maximum frequency difference between signatures; default=0.5\n"));
			sb.append(String.format("%-22s%s","--help","show help\n"));
			sb.append("\n");
			sb.append("== Parameters for fine tuning =="+"\n");
			sb.append(String.format("%-22s%s","--detailed-log","show a detailed event log\n"));
			sb.append("See the online manual for detailed description of the parameters\n");
			System.out.print(sb.toString());

		}
}
