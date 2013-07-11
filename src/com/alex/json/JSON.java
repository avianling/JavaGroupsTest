package com.alex.json;

import com.google.gson.Gson;

public class JSON {
	public static Gson parser;
	
	public JSON() {
		if ( parser == null ) {
			parser = new Gson();
		}
	}
	
	public static String toJson( Object o )
	{
		return parser.toJson(o);
	}
	
	public static Object fromJson( String json, Class objectType ) {
		return parser.fromJson(json, objectType);
	}
}
