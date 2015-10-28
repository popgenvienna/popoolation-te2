package pt2;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by robertkofler on 10/27/15.
 */
public class CommandFormater {

	private static int charleng=50;


	public static String format(String option, String explanation, String defaultValue)
	{
		//String.format("%-22s%s","--ppileup","input ppileup file\n")
		//"%-22s%s","--signature","the signatures of TE insertions; Mandatory\n"

		if(defaultValue!=null) explanation+="; default="+defaultValue;

		String[] tmp=explanation.split(" ");
		LinkedList<String> split=new LinkedList<String>(Arrays.asList(tmp));

		StringBuilder sb=new StringBuilder();
		sb.append(String.format("%-22s%s\n",option,pull(split)));
		while(split.size()>0)
		{
			sb.append(String.format("%-22s%s\n"," ",pull(split)));
		}
		return sb.toString();

	}




	public static String format(String option, String explanation, boolean mandatory)
	{
		if(mandatory)  explanation+="; mandatory";
		return format(option,explanation,null);
	}


	private static String pull(LinkedList<String> topull)
	{
		String toret=topull.remove(0);
		while(topull.size()>0 && toret.length()<charleng)
		{
			toret+=" "+topull.remove(0);
		}
		return toret;

	}

}
