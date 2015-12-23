package pt2.identifysignatures;

import pt2.CommandFormater;

import java.util.LinkedList;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by robertkofler on 6/29/15.
 */
public class IdentifySignatureParser {


		public static void parseCommandline(LinkedList<String> args)
		{

			String inputFile="";
			SignatureIdentificationMode mode=null;
			String outputFile="";
			int mincount=2;
			int chunkdistance=5;
			SignatureWindowMode windowMode= SignatureWindowMode.Median;
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
				else if(cu.equals("--min-count"))
				{
					mincount=Integer.parseInt(args.remove(0));
				}
				else if(cu.equals("--signature-window"))
				{
					windowMode = getSignatureWindowMode(args.remove(0));
				}
				else if(cu.equals("--chunk-distance"))
				{
					chunkdistance = Integer.parseInt(args.remove(0));
				}
				//else if(cu.equals("--refine-distance"))
				//{
				//	refinedistance = Integer.parseInt(args.remove(0));
				//}
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
			if(inputFile.equals("") || outputFile.equals("") || mode==null)
			{
				printHelp();
				System.exit(1);
			}


			Logger logger=corete.misc.LogFactory.getLogger(detailedLog);
			IdentifySignatureFramework isf =new IdentifySignatureFramework(inputFile,outputFile,mode,mincount,
					windowMode,chunkdistance,detailedLog,logger);
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
		//else if(input.toLowerCase().equals("separaterefined"))
		//{
		//	return SignatureIdentificationMode.SeparateRefined;
		//
		//}
		else throw new IllegalArgumentException("Unknown mode "+input);
	}


		private static void printHelp()
		{
			StringBuilder sb=new StringBuilder();
			sb.append("identify signatures of TE insertions\n\n");
			sb.append("== Main parameters ==\n");
			sb.append(CommandFormater.format("--ppileup", "input ppileup file",true));
			sb.append(CommandFormater.format("--mode","joint|separate",true));
			sb.append(CommandFormater.format("--output","TE insertion signatures",true));
			sb.append(CommandFormater.format("--min-count","the minimum count of a TE insertion","2"));
			sb.append(CommandFormater.format("--help","show help",null));
			sb.append("\n");
			sb.append("== Parameters for fine tuning =="+"\n");
			sb.append(CommandFormater.format("--signature-window","the window size of the signatures of TE insertions; [fixNNNN|minimumSampleMedian|maximumSampleMedian|median] ","median"));
			sb.append(CommandFormater.format("--chunk-distance","minimum distance between chromosomal chunks, in multiples of insert size [int]","5"));
			//sb.append(String.format("%-22s%s","--refine-distance","scan-distance for refined positions, in multiples of insert size; default=2\n"));
			sb.append(CommandFormater.format("--detailed-log","show a detailed event log",null));
			sb.append("\nSee the online manual for detailed description of the parameters\n");
			System.out.print(sb.toString());

		}

	public static SignatureWindowMode getSignatureWindowMode(String toParse)
	{
		if(toParse.toLowerCase().equals("minimumsamplemedian"))
		{
			    return SignatureWindowMode.MinimumSampleMedian;
		}
		else if (toParse.toLowerCase().equals("maximumsamplemedian"))
		{
			      return SignatureWindowMode.MaximumSampleMedian;
		}
		else if(toParse.toLowerCase().equals("median"))
		{

			return SignatureWindowMode.Median;
		}
		else if(toParse.toLowerCase().startsWith("fix"))
		{
			Pattern p=Pattern.compile("(?i)fix(\\d+)");
			Matcher m=p.matcher(toParse);
			if(m.find()) {

				int distance = Integer.parseInt(m.group(1));
				SignatureWindowMode toret= SignatureWindowMode.FixedWindow;
				toret.setDistance(distance);
				return toret;
			}
			else throw new IllegalArgumentException("invalid sample mode "+toParse);
		}
		else throw new IllegalArgumentException("Invalid sample mode "+toParse);
	}

}
