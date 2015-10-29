package corete.data;

import corete.io.Parser.SamFlagParser;

import java.security.InvalidParameterException;

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




	public SamRecord(String readname, int flag, String refChr, int start, int end, int start_withs, int end_withs, int mapq, String cigar, String refChrMate,
					   int posMate, int len, String seq, String qual, String comment)
	{
		if(start_withs > start) throw new InvalidParameterException("soft clipped start position has to be smaller or equal to start position");
		if(end_withs < end) throw new InvalidParameterException("soft clipped end position has to be larger or equal to end position");
		if(end < start) {
			throw new InvalidParameterException("start position has to be larger than end position");
		}
		if(end_withs < start_withs) throw new InvalidParameterException("Soft clipped start position has to be larger than soft clipped end position");
		this.readname=readname;
		this.flag=flag;
		this.refChr=refChr;
		this.start=start;
		this.end=end;
		this.mapq=mapq;
		this.cigar=cigar;
		this.refChrMate=refChrMate;
		this.posMate=posMate;
		this.len=len;
		this.seq=seq;
		this.qual=qual;
		this.comment=comment;
		this.start_withs=start_withs;
		this.end_withs=end_withs;
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
	public String getComment(){return this.comment;}

	/**
	 * Does the read map to the forward strand
	 * @return
	 */
	public boolean isForwardStrand()
	{
		return SamFlagParser.isForwardStrand(this.flag);
	}

	public boolean isUnmapped()
	{
		return SamFlagParser.isUnmapped(this.flag);
	}

	public boolean isUnmappedMate()
	{
		return SamFlagParser.isUnmappedMate(this.flag);
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

	public String shortID()
	{
		return "Chr:"+this.refChr+";Pos:"+this.getStart()+";Readname:"+this.getReadname();
	}



}
