package corete.misc;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 6/29/15.
 */
public class LogFactory {

	/*
	private constructor; not instances
	 */
	private LogFactory(){}


	public static Logger getLogger(boolean detailedLog) {
		java.util.logging.Logger logger = java.util.logging.Logger.getLogger("TE Logger");
		java.util.logging.ConsoleHandler tehandler = new java.util.logging.ConsoleHandler();
		tehandler.setLevel(Level.INFO);
		if (detailedLog) tehandler.setLevel(Level.FINEST);
		tehandler.setFormatter(new LogFormatter());
		logger.addHandler(tehandler);
		logger.setUseParentHandlers(false);
		logger.setLevel(Level.ALL);
		return logger;
	}

}
