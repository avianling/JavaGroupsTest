package com.alex.logging;

/**
 * A toolkit used to log events which occur.
 * @author Alex
 *
 */
public class Logger {
	
	public static boolean LogToConsole = true;
	
	public static void Log( String message ) {
		if ( LogToConsole ) {
			System.out.println(message);
		}
	}
}
