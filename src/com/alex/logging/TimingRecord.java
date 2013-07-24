package com.alex.logging;

import java.util.ArrayList;

public class TimingRecord {
	public String message;
	public long time;
	
	public static ArrayList<TimingRecord> records;
	
	public TimingRecord( String msg ) {
		time = System.nanoTime();
		message = msg;
		
		if ( records == null ) {
			records = new ArrayList<TimingRecord>();
		}
		
		records.add(this);
	}
};
