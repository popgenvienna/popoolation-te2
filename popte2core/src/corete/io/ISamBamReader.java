package corete.io;

import corete.data.SamRecord;
/**
 * Created by robertkofler on 6/30/15.
 */
public interface ISamBamReader {

	public abstract SamRecord next();
	public abstract boolean hasNext();
}
