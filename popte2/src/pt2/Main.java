package pt2;

import pt2.filtersignatures.FilterSignatureFramework;
import pt2.filtersignatures.FilterSignatureParser;
import pt2.freqforsig.FrequencyForSignatureParser;
import pt2.identifysignatures.IdentifySignatureParser;
import pt2.pairupsignatures.PairupSignatureFramework;
import pt2.pairupsignatures.PairupSignatureParser;
import pt2.ppileup.PpileupParser;
import pt2.se2pe.Se2PeParser;
import pt2.se2pe.SingleEndToPairEnd;
import pt2.statcoverage.StatcoverageFramework;
import pt2.statcoverage.StatcoverageParser;
import pt2.statpairs.StatPairsFramework;
import pt2.statpairs.StatPairsParser;
import pt2.statreads.StatReadsParser;
import pt2.subsample.SubsampleParser;
import pt2.updateTEstrand.UpdateStrandParser;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.logging.Level;

public class Main {

    public static void main(String[] args) {
		LinkedList<String> rawarguments=new LinkedList<String>(Arrays.asList(args));
		if(rawarguments.size()<1) {
			System.out.println("Not enough arguments");
			System.out.print(getgeneralHelp());
			System.exit(1);
		}



		String subtask=rawarguments.remove(0);
		if(subtask.toLowerCase().equals("ppileup"))
		{
			PpileupParser.parseCommandline(rawarguments);
		}
		else if(subtask.toLowerCase().equals("subsampleppileup"))
		{
			SubsampleParser.parseCommandline(rawarguments);
		}
		else if(subtask.toLowerCase().equals("identifysignatures"))
		{
			IdentifySignatureParser.parseCommandline(rawarguments);
		}
		else if(subtask.toLowerCase().equals("frequency"))
		{
			FrequencyForSignatureParser.parseCommandline(rawarguments);
		}
		else if(subtask.toLowerCase().equals("filtersignatures"))
		{
			FilterSignatureParser.parseCommandline(rawarguments);
		}
		else if(subtask.toLowerCase().equals("pairupsignatures"))
		{
			PairupSignatureParser.parseCommandline(rawarguments);
		}
		else if(subtask.toLowerCase().equals("se2pe")) {
			Se2PeParser.parseCommandLine(rawarguments);

		}
		else if(subtask.toLowerCase().equals("updatestrand")) {
			UpdateStrandParser.parseCommandline(rawarguments);
		}
		else if(subtask.toLowerCase().equals("stat-coverage"))
		{
			StatcoverageParser.parseCommandline(rawarguments);
		}
		else if(subtask.toLowerCase().equals("stat-reads"))
		{
			StatReadsParser.parseCommandline(rawarguments);
		}
		else if(subtask.toLowerCase().equals("stat-pairs"))
		{
			StatPairsParser.parseCommandline(rawarguments);
		}
		else if(subtask.toLowerCase().equals("version"))
		{
			System.out.println(getVersionNumber());
		}
		else
		{
			System.out.println("Unknown sub-task %s".format(subtask));
			System.out.print(getgeneralHelp());
			System.exit(1);
		}

		System.exit(0);

    }

	public static String getgeneralHelp()
	{
		StringBuilder sb=new StringBuilder();
		sb.append("Usage: java -Xmx4g -jar popte2 [subtask] [parameters of subtask]\n\n");
		sb.append("== Main tasks ==\n");
		sb.append(CommandFormater.format("ppileup","generate a multi physical pileup file",null));
		sb.append(CommandFormater.format("subsamplePpileup","subsample ppileup files to an uniform coverage",null));
		sb.append(CommandFormater.format("identifySignatures", "identify signatures of TE insertions",null));
		sb.append(CommandFormater.format("frequency","estimate population frequencies for signatures of TE insertions",null));
		sb.append(CommandFormater.format("filterSignatures","filter signatures of TE insertions",null));
		sb.append(CommandFormater.format("pairupSignatures","pair up signatures of TE insertions to obtain TE insertions",null));


		sb.append("\n== Secondary tasks ==\n");
		sb.append(CommandFormater.format("se2pe","obtain a paired-end bam-file for individually mapped (e.g. bwasw) output files",null));
		sb.append(CommandFormater.format("updatestrand","estimate strand of signatures of TE insertions",null));
		sb.append(CommandFormater.format("stat-coverage","calculate physical coverage statistics; helps to decide optimal target coverage for subsampling",null));
		sb.append(CommandFormater.format("stat-reads","compute the mapping statistics; reads mapping to different reference chromosomes and TEs",null));
		sb.append(CommandFormater.format("stat-pairs","compute the paired-end statistics; reads supporting a TE insertion",null));
		sb.append(CommandFormater.format("version","print the version number",null));


		//sb.append(String.format("%-22s%s","filter","filter TE insertions\n"));
		return sb.toString();
	}


	public static String getVersionNumber()
	{
		return "v1.06.15";
	}


}
