package com.alex.logging;

import java.util.ArrayList;

/**
 * A toolkit used to log events which occur.
 * @author Alex
 *
 */
public class Logger {
	
	public static boolean LogToConsole = true;
	public static boolean LogTimingsToConsole = false;
	
	public static void Log( String message ) {
		if ( LogToConsole ) {
			System.out.println(message);
		}
	}
	
	
	
	/**
	 * This is a specalist log call used to record the performance & timing of particular functions.
	 */
	/*public static void LogTiming( String message)  {
		if ( LogTimingsToConsole ) {
			TimingRecord r = new TimingRecord(message);
		}
	}*/
}
