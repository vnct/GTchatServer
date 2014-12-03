package com.irc.serveur;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import com.component.jms.JMSMessageProducer;
import com.irc.socket.SocketCommunication;
import com.irc.socket.SocketMessage;
import com.irc.socket.SocketMessageType;

public class Server implements Runnable {
	private Vector<ServerThreadClient> clients = null;
	private ServerSocket server = null;
	private Thread       thread = null;
	private int clientCount = 0;
	private JMSMessageProducer messageProducer = null;
	
	
	public Server(int port, String string) {
		super();
		try {
			server = new ServerSocket(port);
			clients = new Vector<ServerThreadClient>();
			start();
			
		}
		catch (IOException e) {
			e.printStackTrace();
		} 
		try {
			messageProducer = new JMSMessageProducer("tcp://"+string+":61616","TOPIC.TOPIC");
		} catch (JMSException e) {
			System.out.println("Can't create TOPIC. Launch ACTIVEMQ");
		}
	}


	public static void main(String args[])
	{  	try
		{
			Server server = null;
			int port = Integer.parseInt(args[1]);
			server = new Server(port,args[0]);
		}
		catch(Exception exception)
		{
			System.out.println("Args 1 : IP ADRESSE ; Arg 2 : PORT");
		}
		
	}


	@Override
	public void run() {
		while (thread != null)
	      {  try
	         {  System.out.println("Waiting for a client ..."); 
	            addThread(server.accept());
	         }
	         catch(IOException ie)
	         {  
	        	 System.out.println("Acceptance Error: " + ie); 
	       }
	      }
	}
	
	private void addThread(Socket socket) {
		
		Integer index = clients.size();
		HashMap<String, String> hashMap;
		try {
			hashMap = createConnection(socket);
			if(hashMap!=null){
				ServerThreadClient threadClient = new ServerThreadClient(this, socket,index+1,hashMap.get("NICKNAME"));
				clients.add(threadClient);
				Integer id = clients.indexOf(threadClient);
				clients.get(id).openThread();
				clientCount++;
				sendmessageTopic(SocketMessageType.USER_CONNECT,hashMap.get("NICKNAME"));
				sendUpdateList();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error opening thread: " + e);
		}

	}
	

	public synchronized  void disconnectSocket(ServerThreadClient serverThreadClient)
	{
		System.out.println(serverThreadClient.getNickname() + " se deconnecte");
		Integer indexClient = clients.indexOf(serverThreadClient);
		try {
			clients.get(indexClient).checkAccess();
			sendmessageTopic(SocketMessageType.USER_DISCONNECT,serverThreadClient.getNickname());
			clients.get(indexClient).closeThread();
			clients.removeElementAt(indexClient);
			
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		System.out.println("Fin disconnectSocket");
		
	}
	public void sendmessageSocket(SocketMessage message)
	{
		System.out.println("sendmessageSocket --> " + message.getMessageContent());
		for(ServerThreadClient threadClient : clients)
		{
			try {
				if(message.getNicknameDestinataire().equals("***"))
				{
					threadClient.sendMessage(message);
					System.out.println("Send to all clients");
				}
				else
				{
					if(threadClient.getNickname().equals(message.getNicknameDestinataire()))
					{
						threadClient.sendMessage(message);
						System.out.println("Send to specific client");
					}
				}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void sendUpdateList()
	{
		List<String> strings = getNickname();
		String nicknames ="";
		for(String nickname : strings)
		{
			nicknames = nicknames.concat(nickname+";");
		}
		sendmessageTopic(SocketMessageType.USER_LIST,nicknames);
	}
	
	public void sendmessageTopic(SocketMessageType type,String text)
	{
		SocketMessage socketMessage = new SocketMessage("***", text, "Serveur", type);
		SocketCommunication socketCommunication = new SocketCommunication();
		TextMessage message ;
		
		
		try {
			if(messageProducer!=null)
			{
				message = messageProducer.getSession().createTextMessage(socketCommunication.convertSocketMessagetoString(socketMessage));
				messageProducer.sendMessage(message);
			}

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	public HashMap<String, String> createConnection(Socket socket) throws IOException
	{
		Boolean admin = false;
		HashMap<String, String> hashMap = new HashMap<>();
		SocketCommunication communication = new SocketCommunication();
		Boolean nicknameAvailable = false;
		String nickname = "";
		SocketMessageType socketMessageType = SocketMessageType.INFO_PSEUDO;
		DataInputStream streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
	   	DataOutputStream streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		if(!socket.isClosed())
		{
			SocketMessage message = new SocketMessage("", "PSEUDO", "SERVEUR", socketMessageType);
			communication.sendMessage(message, streamOut);
			message = communication.convertStringtoSocketMessage(communication.receiveMessage(streamIn));
			nickname = message.getMessageContent();
			nicknameAvailable = true;
			if(getNickname().contains(nickname))
			{
				nicknameAvailable = false;
				socketMessageType = SocketMessageType.INFO_PSEUDO_EXIST;
				message = new SocketMessage("", "PSEUDO", "SERVEUR", socketMessageType);
				communication.sendMessage(message, streamOut);
				socket.close();
			}
		}
		if(nickname.toUpperCase().equals("ADMIN"))
		{
			socketMessageType = SocketMessageType.INFO_ADMIN;
			SocketMessage message = new SocketMessage("", "PASSWORD", "SERVEUR", socketMessageType);
			communication.sendMessage(message, streamOut);
			String password = communication.convertStringtoSocketMessage(communication.receiveMessage(streamIn)).getMessageContent();
			System.out.println("Password " + password);
			if(password.toUpperCase().equals("ADMIN")) {
				admin=true;
			}
			else
			{
				nicknameAvailable = false;
				socketMessageType = SocketMessageType.INFO_ADMIN_ERROR;
				message = new SocketMessage("", "ADMINERROR", "SERVEUR", socketMessageType);
				communication.sendMessage(message, streamOut);
				socket.close();
			}
		}
		if(nicknameAvailable)
		{
			socketMessageType = SocketMessageType.INFO_SUCCESS;
			SocketMessage message = new SocketMessage("", "Success", "SERVEUR", socketMessageType);
			communication.sendMessage(message, streamOut);
			System.out.println(nickname);
			hashMap.put("NICKNAME", nickname);
			hashMap.put("ADMIN", Boolean.toString(admin));
			return hashMap;
		}
		return null;
	}
	
	public List<String> getNickname()
	{
		List<String> stringNickname = new ArrayList<String>();
		for(ServerThreadClient threadClient : clients)
		{
			stringNickname.add(threadClient.getNickname());
		}
		return stringNickname;
	}

	public void start()
	   {  if (thread == null)
	      {  thread = new Thread(this); 
	         thread.start();
	      }
	   }
}