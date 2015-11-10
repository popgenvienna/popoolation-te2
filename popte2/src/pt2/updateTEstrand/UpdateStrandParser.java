package pt2.updateTEstrand;

import pt2.CommandFormater;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Logger;


/**
 * Created by robertkofler on 6/29/15.
 */
public class UpdateStrandParser {


		public static void parseCommandline(LinkedList<String> args)
		{

			ArrayList<String> inputFiles= new ArrayList<String>();
			String signature="";
			String outputFile="";
			String hierFile="";
			int srmd=10000;
			int mapQual=15;
			float idof=0.01F;
			double maxDisagreementFraction=0.1;



			boolean detailedLog=false;



			while(args.size()>0)
			{
				String cu=args.remove(0);

				if(cu.equals("--bam"))
				{
					inputFiles.add(args.remove(0));
				}
				else if(cu.equals("--signature"))
				{
					signature= args.remove(0);
				}
				else if(cu.equals("--output"))
				{
					outputFile=args.remove(0);
				}
				else if(cu.equals("--hier"))
				{
					hierFile=args.remove(0);
				}
				else if(cu.equals("--max-disagreement"))
				{
					maxDisagreementFraction=Double.parseDouble(args.remove(0));
				}
				else if(cu.equals("--map-qual"))
				{
					mapQual = Integer.parseInt(args.remove(0));
				}
				else if(cu.equals("--sr-mindist"))
				{
					srmd=Integer.parseInt(args.remove(0));
				}
				else if(cu.equals("--id-up-quant"))
				{
					idof=  Float.parseFloat(args.remove(0));
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
			if(inputFiles.size()<1 ||signature.equals("") || outputFile.equals("") || hierFile.equals(""))
			{
				printHelp();
				System.exit(1);
			}


			Logger logger=corete.misc.LogFactory.getLogger(detailedLog);
			UpdateStrandFramework isf =new UpdateStrandFramework(inputFiles,signature,outputFile,hierFile,maxDisagreementFraction, mapQual,srmd,idof,detailedLog,logger);
			isf.run();
		}





		private static void printHelp()
		{
			StringBuilder sb=new StringBuilder();
			sb.append("estimate the strand of TEs for signatures of TE insertions\n\n");
			sb.append("== Main parameters ==\n");
			sb.append(CommandFormater.format("--bam", "input files; must be the same files and in the same order as for creating the original ppileup",true));
			sb.append(CommandFormater.format("--signature","the signatures of TE insertions",true));
			sb.append(CommandFormater.format("--output","TE insertion signatures",true));
			sb.append(CommandFormater.format("--hier","TE hierarchy file",true));
			sb.append(CommandFormater.format("--map-qual","minimum mapping quality", "15"));
			sb.append(CommandFormater.format("--max-disagreement","the maximum disagreement for the strand of the TE insertion in fraction of reads",true));
			sb.append(CommandFormater.format("--help","show help",null));
			sb.append("\n");
			sb.append("== Parameters for fine tuning =="+"\n");
			sb.append(CommandFormater.format("--sr-mindist","minimum inner distance for structural rearrangements","10000"));
			sb.append(CommandFormater.format("--id-up-quant","paired end fragments with an insert size in the upper quantile will be ignored [fraction]","0.01"));
			sb.append(CommandFormater.format("--detailed-log","show a detailed event log",null));
			sb.append("\nSee the online manual for detailed description of the parameters\n");
			System.out.print(sb.toString());

		}
}
