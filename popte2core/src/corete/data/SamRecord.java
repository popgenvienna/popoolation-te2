package corete.data;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by robertkofler on 6/30/15.
 */
public class SamRecord {
	private final String readname;
	private final int flag;
	private final String refChr;
	private final int start;
	private final int end;
	private final int start_withs;
	private final int end_withs;
	private final int mapq;
	private final String cigar;
	private final String refChrMate;
	private final int posMate;
	private final int len;
	private final String seq;
	private final String qual;
	private final String comment;
	//\*|([0-9]+[MIDNSHPX=])+ CIGAR string
	private static final Pattern cigarparser = Pattern.compile("(\\d+)([SMHDI])");



	public SamRecord(String readname, int flag, String refChr, int pos, int mapq, String cigar, String refChrMate,
					   int posMate, int len, String seq, String qual, String comment)
	{
		this.readname=readname;
		this.flag=flag;
		this.refChr=refChr;
		this.start=pos;
		this.mapq=mapq;
		this.cigar=cigar;
		this.refChrMate=refChrMate;
		this.posMate=posMate;
		this.len=len;
		this.seq=seq;
		this.qual=qual;
		this.comment=comment;

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

	public String getReadname()
	{
		return this.readname;
	}
	public int getFlag()
	{
		return flag;
	}
	public int getMapq()
	{
		return this.mapq;
	}
	public String getCigar()
	{
		return this.cigar;
	}
	public String getRefchr()
	{
		return this.refChr;
	}
	public String getRefchrMate()
	{
		return this.refChrMate;
	}
	public int getStartMate()
	{
		return this.posMate;
	}
	public int getDistance()
	{
		return len;
	}
	public String getSequence()
	{
		return this.seq;
	}
	public String getQual()
	{
		return this.qual;
	}
	public int getStart()
	{
		return this.start;
	}

	/**
	 * End position of alignment (excluding soft/hard clipping);
	 * Last position that matches (start+len-1)
	 * @return
	 */
	public int getEnd()
	{
		 return this.end;
	}

	/**
	 * Start position minus softclip, ie the start position if the alignment would start with the soft-clipped region
	 * @return
	 */
	public int getStartWithS()
	{
		return this.start_withs;
	}

	/**
	 * End position plus softclip, i.e. the end position if the alignment would end after the soft-clipped region
	 * @return
	 */
	public int getEndWithS()
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
			int count = Integer.getInteger(m.group(1));
			String key = m.group(2);
			ci.add(new CigarEntry(count,key));
		}
		return ci;

	}




}
