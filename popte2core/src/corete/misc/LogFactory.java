package corete.misc;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 6/29/15.
 */
public class LogFactory {

	private static Logger nullLogger=null;
	private static Logger popte2Logger=null;
	/*
	private constructor; not instances
	 */
	private LogFactory(){}


	/**
	 * Default logger for PoPooaltion TE2
	 * @param detailedLog
	 * @return
	 */
	public static Logger getLogger(boolean detailedLog) {
		if(popte2Logger==null) {
			java.util.logging.Logger logger = java.util.logging.Logger.getLogger("TE Logger");
			java.util.logging.ConsoleHandler tehandler = new java.util.logging.ConsoleHandler();
			tehandler.setLevel(Level.INFO);
			if (detailedLog) tehandler.setLevel(Level.FINEST);
			tehandler.setFormatter(new LogFormatter());
			logger.addHandler(tehandler);
			logger.setUseParentHandlers(false);
			logger.setLevel(Level.ALL);
			popte2Logger=logger;
		}
		return popte2Logger;
	}

	/**
	 * Return a logger which is not producing output
	 * Useful for testing purposes
	 * @return
	 */
	public static Logger getNullLogger()
	{
		if(nullLogger ==null)
		{
			java.util.logging.Logger logger = java.util.logging.Logger.getLogger("TE Logger");
			java.util.logging.ConsoleHandler tehandler = new java.util.logging.ConsoleHandler();
			tehandler.setLevel(Level.OFF);
			tehandler.setFormatter(new LogFormatter());
			logger.addHandler(tehandler);
			logger.setUseParentHandlers(false);
			logger.setLevel(Level.OFF);
			nullLogger=logger;
		}

		return nullLogger;
	}


}
