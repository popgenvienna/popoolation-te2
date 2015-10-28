package pt2.pairupsignatures;

import pt2.CommandFormater;

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
			sb.append("pairs up signatures of TE insertions and yields TE insertions\n\n");
			sb.append("== Main parameters ==\n");
			sb.append(CommandFormater.format("--signature", "signatures of TE insertions",true));
			sb.append(CommandFormater.format("--ref-genome","the repeat-masked reference genome",true));
			sb.append(CommandFormater.format("--hier-file","the TE hierarchy",true));
			sb.append(CommandFormater.format("--output","TE insertions",true));
			sb.append(CommandFormater.format("--min-distance","the minimum distance between signatures","-100"));
			sb.append(CommandFormater.format("--max-distance","the maximum distance between signatures", "500"));
			sb.append(CommandFormater.format("--help","show help",null));
			sb.append("\n");
			sb.append("== Parameters for fine tuning =="+"\n");
			sb.append(CommandFormater.format("--max-freq-diff","the maximum frequency difference between signatures","0.5"));
			sb.append(CommandFormater.format("--detailed-log","show a detailed event log",null));
			sb.append("\nSee the online manual for detailed description of the parameters\n");
			System.out.print(sb.toString());

		}
}
