package corete.io;

import corete.data.SamPair;

/**
 * Created by robertkofler on 2/7/16.
 */
public interface ISamPairReader {

	abstract public boolean hasNext();
	abstract public SamPair next();
}
