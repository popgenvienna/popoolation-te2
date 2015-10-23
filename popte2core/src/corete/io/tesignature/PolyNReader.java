package corete.io.tesignature;

import corete.data.FastaRecord;
import corete.data.polyn.PolyNRecord;
import corete.data.polyn.PolyNRecordCollection;
import corete.io.FastaReader;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 10/23/15.
 */
public class PolyNReader {
	public FastaReader fastaReader;
	public Logger logger;
	public PolyNReader(FastaReader fastaReader, Logger logger)
	{
		this.fastaReader=fastaReader;
		this.logger=logger;
	}

	public PolyNRecordCollection getPolyNRecords()
	{
		this.logger.info("Start identification of poly-N tracts in "+fastaReader.getInputFile());
		ArrayList<PolyNRecord> toret=new ArrayList<PolyNRecord>();
		FastaRecord fr=null;
		while((fr=fastaReader.next())!=null)
		{
			ArrayList<PolyNRecord> recs=getPolyNRecords(fr);
			toret.addAll(recs);
		}

		PolyNRecordCollection tr=new PolyNRecordCollection(toret);

		this.logger.info("Finished identification of poly-N tracts");
		this.logger.info("found " + tr.countRecords() + " tracts on "+tr.countContigs()+" contigs; total size " + tr.countNbases() + "bp");
		return tr;
	}





	private ArrayList<PolyNRecord> getPolyNRecords(FastaRecord fr)
	{
		// sam is 1-based
		String chr=fr.getHeader();
		String seq=fr.getSequence();

		int laststart=-1;

		ArrayList<PolyNRecord> toret=new ArrayList<PolyNRecord>();

		for(int i=0;i<seq.length(); i++)
		{
			if(seq.charAt(i)=='N'|| seq.charAt(i)=='n')
			{
				if(laststart==-1) laststart=i;
			}
			else
			{
				if(laststart!=-1)
				{

					// add 1 since index is 0-based while sam is 1-based
				    toret.add(new PolyNRecord(chr,laststart+1,i));      // i is already +1
					laststart=-1;
				}
			}
		}

		// Also treat last
		if(laststart!=-1) toret.add(new PolyNRecord(chr,laststart+1,seq.length())); // length would be -1 +1

		return toret;

	}



}
