package corete.io;

import corete.data.SamRecord;
import corete.io.SamValidator.ISamValidator;
import corete.io.SamValidator.SamValidatorAllValid;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 8/17/15.
 */
public class BestHitSamBamReader implements ISamBamReader {
	private ISamBamReader sbr;
	private SamRecord buffer;




	public BestHitSamBamReader(ISamBamReader reader)
	{
	  	 this.sbr=reader;
		buffer=this.getNext();
	}


	private SamRecord getNext()
	{
		if(buffer!=null)
		{
			SamRecord toret=buffer;
			buffer=null;
			return toret;
		}
		else if(sbr.hasNext())
		{
			return sbr.next();
		}
		else return null;

	}

	private void buffer(SamRecord toBuffer)
	{
		if(this.buffer!=null) throw new IllegalArgumentException("Error buffer already full");
		this.buffer=toBuffer;
	}



	public boolean hasNext() {
		if(this.buffer==null) return false;
		else return true;
	}


	public SamRecord next() {
		if(!this.hasNext()) throw new IllegalArgumentException();
		ArrayList<SamRecord> equalRecords=new ArrayList<SamRecord>();
		equalRecords.add(this.next());
		String readname=equalRecords.get(0).getReadname();

		while(this.hasNext())
		{
			SamRecord test=this.next();
			if(test.getReadname().equals(readname)) equalRecords.add(test);
			else
			{
				this.buffer(test);
				break;
			}
		}

		SamRecord besthit=equalRecords.get(0);
		int bestscore=besthit.getEnd()-besthit.getStart();
		for(SamRecord s:equalRecords)
		{
			int score=s.getEnd()-s.getStart();
			if(score>bestscore)
			{
				 besthit=s;
				bestscore=score;
			}
		}
		return besthit;
	}
}
