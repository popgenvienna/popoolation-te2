package corete.io.Parser;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by robertkofler on 8/17/15.
 */
public class CigarParser {
	private static final Pattern cigarparser = Pattern.compile("(\\d+)([SMHDI])");

	private final int start;
	private final int end;
	private final int start_withs;
	private final int end_withs;
	private final String cigar;
	public CigarParser(String cigar, int start)
	{
		this.cigar=cigar;
		this.start=start;
		// Parsing the cigar  and update the positions
		int preStart=0;
		int postEnd=0;
		int alignmentLeng=0;

		ArrayList<CigarEntry> parseCigar = this.parseCigar(cigar);
		if(parseCigar.get(0).key.equals("S"))
		{
			preStart=parseCigar.get(0).count;
		}
		if(parseCigar.get(parseCigar.size()-1).key.equals("S"))
		{
			postEnd=parseCigar.get(parseCigar.size()-1).count;
		}
		for(CigarEntry ci: parseCigar)
		{
			if(ci.key.equals("M") || ci.key.equals("D") || ci.key.equals("N"))
			{
				alignmentLeng+=ci.count;
			}
		}
		this.end=this.start+alignmentLeng-1;
		this.end_withs=this.end+postEnd;
		this.start_withs=this.start-preStart;
	}

	public int getStart()
	{
		return this.start;
	}
	public int  getEnd()
	{
		return this.end;
	}
	public int getStart_withs()
	{
		return this.start_withs;
	}
	public int getEnd_withs()
	{
		return this.end_withs;
	}



	private  class CigarEntry
	{
		public CigarEntry(int count, String key)
		{
			this.count=count;
			this.key=key;
		}
		public int count;
		public String key;
	}


	private  ArrayList<CigarEntry> parseCigar(String cigar)
	{
		//60S41M
		Matcher m= cigarparser.matcher(cigar);
		ArrayList<CigarEntry> ci=new ArrayList<CigarEntry>();
		while(m.find())
		{
			int count = Integer.parseInt(m.group(1));
			String key = m.group(2);
			ci.add(new CigarEntry(count,key));
		}
		return ci;

	}

}
