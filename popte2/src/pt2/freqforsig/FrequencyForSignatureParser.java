package pt2.freqforsig;

import java.util.LinkedList;
import java.util.logging.Logger;


/**
 * Created by robertkofler on 6/29/15.
 */
public class FrequencyForSignatureParser {


		public static void parseCommandline(LinkedList<String> args)
		{

			String ppileupe="";
			String signature="";
			String outputFile="";
			int mincount=2;
			Integer fixedinsertsize=null;
			int chunkdistance=5;
			//int refinedistance=2;
			boolean detailedLog=false;



			while(args.size()>0)
			{
				String cu=args.remove(0);

				if(cu.equals("--ppileup"))
				{
					ppileupe=args.remove(0);
				}
				else if(cu.equals("--signature"))
				{
					signature= args.remove(0);
				}
				else if(cu.equals("--output"))
				{
					outputFile=args.remove(0);
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
			if(ppileupe.equals("") ||signature.equals("") || outputFile.equals("") )
			{
				printHelp();
				System.exit(1);
			}


			Logger logger=corete.misc.LogFactory.getLogger(detailedLog);
			FrequencyForSignatureFramework isf =new FrequencyForSignatureFramework(inputFile,outputFile,mode,mincount,
					fixedinsertsize,chunkdistance,detailedLog,logger);
			isf.run();
		}





		private static void printHelp()
		{
			StringBuilder sb=new StringBuilder();
			sb.append("identify signatures of TE insertions\n");
			sb.append("== Main parameters ==\n");
			sb.append(String.format("%-22s%s","--ppileup","input ppileup file; Mandatory\n"));
			sb.append(String.format("%-22s%s","--signature","the signatures of TE insertions; Mandatory\n"));
			sb.append(String.format("%-22s%s","--output","TE insertion signatures; Mandatory\n"));
			sb.append(String.format("%-22s%s","--help","show help\n"));
			sb.append("\n");
			sb.append("== Parameters for fine tuning =="+"\n");
			sb.append(String.format("%-22s%s","--detailed-log","show a detailed event log\n"));
			sb.append("See the online manual for detailed description of the parameters\n");
			System.out.print(sb.toString());

		}
}
