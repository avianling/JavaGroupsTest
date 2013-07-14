package com.alex.framework;

public class MessageFactory {
	public static Message makeRegisterMessage() {
		Message msg = new Message();
		msg.Headers.put(MessageConstants.FIELD_CODE,MessageConstants.CODE_REGISTRATION);
		return msg;
	}
	
	public static Message makeJoinGroupMessage( String idToken, String groupName ) {
		Message msg = new Message();
		
		msg.setHeader(MessageConstants.FIELD_CODE, MessageConstants.CODE_JOIN_GROUP);
		msg.setHeader(MessageConstants.FIELD_GROUP_NAME, groupName);
		msg.setHeader(MessageConstants.FIELD_IDTOKEN, idToken);
		
		return msg;
	}
	
	public static Message makeStringMessage( String idToken, String groupName, String postContent ) {
		Message msg = new Message();
		
		msg.setHeader(MessageConstants.FIELD_CODE, MessageConstants.CODE_MESSAGE_STRING);
		msg.setHeader(MessageConstants.FIELD_GROUP_NAME, groupName);
		msg.setHeader(MessageConstants.FIELD_IDTOKEN, idToken);
		
		msg.Payload = postContent;
		
		return msg;
	}
}
