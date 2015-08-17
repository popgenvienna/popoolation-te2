package corete.misc;

/**
 * Created by robertkofler on 6/29/15.
 */


/**
 * Log formater for MimicrEE
 * @author robertkofler
 */
public class LogFormatter extends java.util.logging.Formatter
{
	public LogFormatter()
	{}

	@Override
	public String format(java.util.logging.LogRecord record)
	{
		String msg=String.format("%tD %<tT: %s\n",new java.util.Date(record.getMillis()),record.getMessage());
		return msg;
	}

}
