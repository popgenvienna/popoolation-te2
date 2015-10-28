package pt2.freqforsig;

import pt2.CommandFormater;

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
			FrequencyForSignatureFramework isf =new FrequencyForSignatureFramework(ppileupe,signature ,outputFile,detailedLog,logger);
			isf.run();
		}





		private static void printHelp()
		{
			StringBuilder sb=new StringBuilder();
			sb.append("estimate frequencies for signatures of TE insertions\n\n");
			sb.append("== Main parameters ==\n");
			sb.append(CommandFormater.format("--ppileup", "input ppileup file",true));
			sb.append(CommandFormater.format("--signature","the signatures of TE insertions",true));
			sb.append(CommandFormater.format("--output","TE insertion signatures",true));
			sb.append(CommandFormater.format("--help","show help",null));
			sb.append("\n");
			sb.append("== Parameters for fine tuning =="+"\n");
			sb.append(CommandFormater.format("--detailed-log","show a detailed event log",null));
			sb.append("\nSee the online manual for detailed description of the parameters\n");
			System.out.print(sb.toString());

		}
}
