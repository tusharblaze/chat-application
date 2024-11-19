package com.zensar.chat_application.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.zensar.chat_application.dto.ChatMessage;
import com.zensar.chat_application.dto.MessageType;

@Component
public class WebSocketEventListner {

	private final static Logger log = LoggerFactory.getLogger(WebSocketEventListner.class);
	
	private  SimpMessageSendingOperations messageTemplate = new SimpMessagingTemplate((message,timeout)->{
		return false;
	});
	
	@EventListener
	public void handleWebSocketDisconnectListner(SessionDisconnectEvent event){
		
		   StompHeaderAccessor stopmAccessor = StompHeaderAccessor.wrap(event.getMessage());
		   String username = (String)stopmAccessor.getSessionAttributes().get("username");
		
		   if(username != null) {
			   log.info("User Disconnected :{} ",username);
			   var chatMessage = new ChatMessage();
			   chatMessage.setType(MessageType.LEAVE);
			   chatMessage.setSender(username);
			   messageTemplate.convertAndSend("/topic/public", chatMessage);
		   }
		}
}
