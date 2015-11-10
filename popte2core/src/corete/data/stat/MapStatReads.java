package corete.data.stat;

import java.util.HashMap;

/**
 * Created by robertkofler on 11/10/15.
 */
public class MapStatReads
{
	private final int readsInFile;
	private final int readsMapped;
	private final int readsWithMq;
	private final int readsTe;
	private final int readsTeWithMq;
	private final HashMap<String,Integer> mappedRefChr;
	private final HashMap<String,Integer> mappedTE;
	public MapStatReads(int readsInFile, int readsMapped, int readsWithMq, int readsTe, int readsTeWithMq,HashMap<String,Integer> mappedRefChr, HashMap<String,Integer> mappedTE)
	{
		this.readsInFile=readsInFile;
		this.readsMapped=readsMapped;
		this.readsWithMq=readsWithMq;
		this.readsTe=readsTe;
		this.readsTeWithMq=readsTeWithMq;
		this.mappedRefChr=new HashMap<String,Integer>(mappedRefChr);
		this.mappedTE=new HashMap<String,Integer>(mappedTE);
	}
	public int getReadsInFile(){return this.readsInFile;}
	public int getReadsMapped(){return this.readsMapped;}
	public int getReadsWithMq(){return this.readsWithMq;}
	public int getReadsTe(){return this.readsTe;}
	public int getReadsTeWithMq(){return this.readsTeWithMq;}
	public HashMap<String,Integer> getMappedRefChr(){return new HashMap<String,Integer>(this.mappedRefChr);}
	public HashMap<String,Integer> getMappedTE(){return new HashMap<String,Integer>(this.mappedTE);}

}
