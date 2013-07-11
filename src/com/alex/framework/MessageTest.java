package com.alex.framework;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gson.Gson;

public class MessageTest {

	@Test
	public void test() {
		Message M = new Message();
		M.Headers.put("test", "ing");
		M.Headers.put("alex", "borboom");
		M.Payload = "Bitches love payloads";
		
		System.out.println(M.Serialize());
		
		Gson parser = new Gson();
		String data = parser.toJson(M);
		System.out.println(data);
		
		Message m2 = parser.fromJson(data, Message.class);
		System.out.println(M.Serialize());
	}

}
