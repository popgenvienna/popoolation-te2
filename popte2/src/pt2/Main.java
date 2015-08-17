package pt2;

import pt2.ppileup.PpileupParser;

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
		if(subtask.equals("ppileup"))
		{
			PpileupParser.parseCommandline(rawarguments);
		}
		else if(subtask.equals("identifySignatures"))
		{

		}
		else if(subtask.equals("pairupSignatures"))
		{

		}
		else if(subtask.equals("estimateFrequency"))
		{

		}
		else if(subtask.equals("subsamplePPileup"))
		{

		}
		else if(subtask.equals("statistics"))
		{

		}
		else if(subtask.equals("filterInsertions"))
		{

		}
		else
		{
			System.out.println("Unknown sub-task %s".format(subtask));
			System.out.print(getgeneralHelp());
			System.exit(1);
		}

    }

	public static String getgeneralHelp()
	{
		StringBuilder sb=new StringBuilder();
		sb.append("Usage: java -Xmx4g -jar popte2 [subtask] [parameters of subtask]\n\n");
		sb.append("Possible subtasks:\n");
		sb.append(String.format("%-22s%s","ppileup","generate a multi physical pileup file\n"));
		sb.append(String.format("%-22s%s", "identifySignatures", "identify signatures of TE insertions;\n"));
		sb.append(String.format("%-22s%s","pairupSignatures","pair up suitable signatures of TE insertions, resulting in TE insertions\n"));
		sb.append(String.format("%-22s%s","estimateFrequency","estimate population frequencies of TE insertions\n"));
		sb.append(String.format("%-22s%s","subsamplePPileup","subsample the ppileup files to uniform coverage\n"));
		sb.append(String.format("%-22s%s","statistics","estimate various statistics\n"));
		sb.append(String.format("%-22s%s","filter","filter TE insertions\n"));
		return sb.toString();
	}

}
