package corete.data;

/**
 * A PE sam record, optimized for identification of TE insertions
 * It is guaranteed that the first read has a lower start position than the second read
 * the second read may be empty (eg for structural rearangements or TE insertions)
 * Created by robertkofler on 8/17/15.
 */
public class SamPair {
	private final SamPairType spt;
	private final SamRecord firstRead;
	private final SamRecord secondRead;
	private final String family;
	private final String order;


	public SamPair(SamRecord firstRead, SamRecord secondRead, SamPairType spt)
	{
		this(firstRead,secondRead,spt,null,null);
	}



	public SamPair(SamRecord firstRead, SamRecord secondRead, SamPairType spt,String family, String order)
	{
		if(secondRead!=null)
		{
			if(firstRead.getStart()>secondRead.getStart()) throw new IllegalArgumentException("First read of pair must have a lower starting position than second read");
		}
		this.firstRead=firstRead;
		this.secondRead=secondRead;
		this.spt=spt;
		this.family=family;
		this.order=order;
	}

	/**
	 * First read of a PE
	 * Guaranted not null and guaranted with smaller start position than second-read
	 * @return
	 */
	public SamRecord getFirstRead(){return this.firstRead;}

	/**
	 * The second read of a PE
	 * may be null (not present), for structural rearangements and TE-fragments
	 * If present, guaranteed to have higher start position than the first read
	 * @return
	 */
	public SamRecord getSecondRead(){return this.secondRead;}
	public SamPairType getSamPairType(){return this.spt;}

	/**
	 * Proper pair defined
	 * both reads mapped
	 * both reads have sufficient mapping quality
	 * reads are on identical reference chromosomes
	 * reads point towards each other
	 *  reads are not not overlapping (not considering softclipping)
	 * @param minMapQual
	 * @return
	 */
	public boolean isProperPair(int minMapQual)
	{
		if(firstRead==null ||secondRead==null) return false;
		if(firstRead.getMapq()<minMapQual || secondRead.getMapq()<minMapQual) return false;
		if(!firstRead.getRefchr().equals(secondRead.getRefchr())) return false;
		if((!firstRead.isForwardStrand()) || secondRead.isForwardStrand()) return false;
		if(firstRead.getEnd()<=secondRead.getStart()) return true;
		else return false;
	}


	/**
	 * get the INNER DISTANCE
	 * only compute this for proper pairs
	 * does not make sense to compute for anything else
	 * eg.:
	 * 012345678901234567890
	 * AAAA-----TTTTT
	 *    3.....9
	 * r1.end = 3; r2.start = 9
	 * distance = r2.start - r1.end - 1
	 *
	 * @return
	 */
	public int getInnerDistance()
	{
		if(firstRead==null ||secondRead==null) throw new IllegalArgumentException("Can not compute distance for unpaired");
		int  dist=this.firstRead.getEnd();

		int distance=secondRead.getStart() - firstRead.getEnd() - 1;
		return distance;
	}


	public String getFamily() { return this.family;}

	public String getOrder() {return this.order;}





}
