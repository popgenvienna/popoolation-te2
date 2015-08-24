package corete.io.SamValidator;

import corete.data.SamRecord;

/**
 * Created by robertkofler on 8/17/15.
 */
public interface ISamValidator {
	public abstract boolean isValid(SamRecord record);
	public abstract String errorMessage(SamRecord record);
}
