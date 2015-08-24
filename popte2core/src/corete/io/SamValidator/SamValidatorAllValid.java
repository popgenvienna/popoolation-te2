package corete.io.SamValidator;

import corete.data.SamRecord;

/**
 * Created by robertkofler on 8/17/15.
 */
public class SamValidatorAllValid implements ISamValidator {

	public SamValidatorAllValid()
	{}


	@Override
	public boolean isValid(SamRecord record) {
		return true;
	}

	@Override
	public String errorMessage(SamRecord record)
	{
		throw new IllegalArgumentException("Should never happen");
	}

}
