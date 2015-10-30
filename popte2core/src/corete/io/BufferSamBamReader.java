package corete.io;

import corete.data.SamRecord;
import corete.io.ISamBamReader;

import java.util.ArrayList;

/**
 * Created by robertkofler on 8/17/15.
 */
public class BufferSamBamReader {
	private ISamBamReader sbr;
	private SamRecord buffer;




	public BufferSamBamReader(ISamBamReader reader)
	{
	  	 	this.sbr=reader;
	}




	public void buffer(SamRecord toBuffer)
	{
		if(this.buffer!=null) throw new IllegalArgumentException("Error buffer already full");
		this.buffer=toBuffer;
	}



	public SamRecord next() {

		if(buffer!=null)
		{
			SamRecord toret=buffer;
			buffer=null;
			return buffer;
		}
		if(sbr.hasNext()) return sbr.next();
		else return null;

	}

}
