package week06.util;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * The ATM logger for the ATM project
 * This serves as the root of the logging hierarchy for the
 * ATM application
 * 
 * @author Scott LaChance
 * 
 */
public class AtmLogger
{
	/** 
	 * Returns the singleton instance of the logger
	 * @return The AtmLogger instance
	 */
	static public Logger getAtmLogger()
	{
		return atmLogger;
	}
	
	/**
	 * Initializes the logging system for our purposes
	 * 
	 * @throws IOException on error
	 */
	static public void setup() throws IOException
	{
		// create an get the global logger to configure it
		//Logger logger = Logger.getLogger(ATM_LOGGER);

		// suppress the logging output to the console
		// removes any handlers to the console logger
		Logger rootLogger = Logger.getLogger("");
		Handler[] handlers = rootLogger.getHandlers();
		if(handlers.length > 0)
		{
			if(handlers[0] instanceof ConsoleHandler)
			{
				rootLogger.removeHandler(handlers[0]);
			}
		}

		atmLogger.setLevel(Level.INFO);
		fileTxt = new FileHandler("Logging.txt");
		fileHTML = new FileHandler("Logging.html");

		// create a TXT formatter
		formatterTxt = new SimpleFormatter();
		fileTxt.setFormatter(formatterTxt);
		atmLogger.addHandler(fileTxt);

		// create an HTML formatter
		formatterHTML = new AtmHtmlLoggingFormatter();
		fileHTML.setFormatter(formatterHTML);

		atmLogger.addHandler(fileHTML);
	}

	/**
	 * Adds all the handlers defined by the application to the provided logger.
	 * Thus any locally defined logger added to the AtmLogger, will get 
	 * the same handlers applied uniquely to that logger.
	 * 
	 * @param logger Logger handler instance to add
	 */
	static public void addAtmLoggerHandlers(Logger logger)
	{
		logger.addHandler(fileTxt);
		logger.addHandler(fileHTML);
	}

	static private FileHandler fileTxt;
	static private SimpleFormatter formatterTxt;
	static private FileHandler fileHTML;
	static private Formatter formatterHTML;
	
	static public String ATM_LOGGER = "ATM_LOGGER";
	static Logger atmLogger;
	
	/**
	 * static initializer
	 */
	static 
	{
		atmLogger = Logger.getLogger(ATM_LOGGER);
	}
}
