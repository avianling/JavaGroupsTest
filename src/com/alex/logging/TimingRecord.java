package com.alex.logging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TimingRecord {

	public static Map<String,TimingRecord> records;
	
	public TimingRecord( String msg ) {
				
		if ( records == null ) {
			records = new HashMap<String,TimingRecord>();
		}
		
		records.put(msg, this);
	}
	
	
	// The times we are going to record
};
