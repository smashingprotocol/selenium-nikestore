package com.pcm.utility;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;


public class Log {

	
	
	private static final Logger log = Logger.getLogger(Log.class.getName());
	
	
	public static void executeTestCase(String strTestCase)
	{
		BasicConfigurator.configure();
		log.info("Starting TestCase: " + strTestCase + "...");
		
	}
	
	public static void info(String message)
	{
		BasicConfigurator.configure();
		Log.info(message);
	}
	
}
