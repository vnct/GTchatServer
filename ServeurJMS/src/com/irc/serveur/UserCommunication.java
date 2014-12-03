package com.irc.serveur;

import java.io.IOException;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.TextMessage;






import com.component.jms.JMSMessageProducer;
import com.irc.socket.SocketCommunication;
import com.irc.socket.SocketMessage;
import com.irc.socket.SocketMessageType;



public class UserCommunication {

	private JMSMessageProducer messageProducer;
	private SocketCommunication socketCommunication;
	private String nickname;
	private String adminstatus;
	public UserCommunication(JMSMessageProducer messageProducer, String nickname,
			String adminstatus) {
		super();
		this.messageProducer = messageProducer;
		this.socketCommunication = new SocketCommunication();
		this.nickname = nickname;
		this.adminstatus = adminstatus;
	}
	public JMSMessageProducer getMessageProducer() {
		return messageProducer;
	}
	public void setMessageProducer(JMSMessageProducer messageProducer) {
		this.messageProducer = messageProducer;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getAdminstatus() {
		return adminstatus;
	}
	public void setAdminstatus(String adminstatus) {
		this.adminstatus = adminstatus;
	}
	// fonction qui informe les utilisateurs que qq s'est déconnecté ou connecté; 
	public void updateUserDisconnect(UserCommunication user) throws IOException, JMSException
	{
		
		SocketMessage socketmessage = new SocketMessage(false,nickname, user.getNickname(), "SERVEUR",SocketMessageType.USER_DISCONNECT);
		String text = socketCommunication.convertSocketMessagetoString(socketmessage);
		TextMessage message = messageProducer.getSession().createTextMessage(text);
		messageProducer.sendMessage(message);
	}
	public void updateUserConnect(UserCommunication user) throws IOException, JMSException
	{
		SocketMessage socketmessage = new SocketMessage(false,nickname, user.getNickname(), "SERVEUR",SocketMessageType.USER_CONNECT);
		String text = socketCommunication.convertSocketMessagetoString(socketmessage);
		TextMessage message = messageProducer.getSession().createTextMessage(text);
		messageProducer.sendMessage(message);

	}
	
	// fonction qui recoie les messages clients et les mets dans la liste
	
	
	public void sendConnectedUser(List<UserCommunication> userlist) throws IOException, JMSException
	{
		for(UserCommunication user : userlist)
		{
			SocketMessage socketmessage = new SocketMessage(false,nickname, user.getNickname(), "SERVEUR",SocketMessageType.USER_NAME);
			String text = socketCommunication.convertSocketMessagetoString(socketmessage);
			TextMessage message = messageProducer.getSession().createTextMessage(text);
			messageProducer.sendMessage(message);
		}
	}
	public void sendMsg(TextMessage message) throws JMSException
	{
		messageProducer.sendMessage(message);
	}
	

	public void sendMsg(String text) throws IOException, JMSException
	{
		sendMsg(new SocketMessage(false,nickname, text, "Serveur", SocketMessageType.MESSAGE_TEXT));
		
	}
	public void sendMsg(SocketMessage socketMessage) throws IOException, JMSException
	{
		String text = socketCommunication.convertSocketMessagetoString(socketMessage);
		TextMessage message = messageProducer.getSession().createTextMessage(text);
		sendMsg(message);
		
	}

	
}
