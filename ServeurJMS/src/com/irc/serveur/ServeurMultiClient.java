package com.irc.serveur;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Vector;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import com.component.jms.JMSMessageConsumer;
import com.component.jms.JMSMessageProducer;
import com.irc.socket.SocketCommunication;
import com.irc.socket.SocketMessage;
import com.irc.socket.SocketMessageType;

@SuppressWarnings("unused")
public class ServeurMultiClient {
	private Socket       socket = null;
	private ServerSocket server = null;
	private JMSMessageConsumer consumer = null;
	private HashMap<String,UserCommunication> vectoruser = new HashMap<String, UserCommunication>();
	private Vector<ServerThreadClient> vector = new Vector<ServerThreadClient>();
	private DataInputStream streamIn;
	private DataOutputStream streamOut;
	
//	public ServeurMultiClient(int port,String ipaddress) {
//		super();
//		try {
//			server = new ServerSocket(port);
//		//	consumer = new JMSMessageConsumer("tcp://"+ipaddress+":61616", "SERVEUR");
//		/*	socket = server.accept();
//			HashMap<String, String> hashMap = createConnection(socket);
//			
//			vectoruser.put(hashMap.get("NICKNAME"),new UserCommunication(new JMSMessageProducer("vm://127.0.0.1", hashMap.get("NICKNAME")), hashMap.get("NICKNAME"), hashMap.get("ADMIN")));
//	*/
//			waitinguser();
//			broadcast();
//			/*	for(Entry<String, UserCommunication> entry : vectoruser.entrySet()) {
//			    String cle = entry.getKey();
//			    UserCommunication valeur = entry.getValue();
//			    
//			}*/
//			
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}  
//	}
//	
//	private void waitinguser() {
//		Thread waitingThread = new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				Boolean done = false;
//				while(!done)
//				{	
//					try {
//						Socket my_socket = server.accept();
//						HashMap<String, String> hashMap = createConnection(my_socket);
//						ServerThreadClient client = new ServerThreadClient(my_socket, hashMap.get("NICKNAME"),hashMap.get("ADMIN"),null);
//						vector.add(client);
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//					
//				}
//			}
//				
//		});
//		
//	}
//	
	
//	
///*	public void waitinguser(final String ipaddress)
//	{
//		Thread waitingThread = new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				Boolean done = false;
//				while(!done)
//				{
//					try {
//						socket = server.accept();
//						HashMap<String, String> hashMap = createConnection(socket);
//						Client client = new Client(socket, hashMap.get("NICKNAME"),hashMap.get("ADMIN"));
//						vectoruser.put(hashMap.get("NICKNAME"),new UserCommunication(new JMSMessageProducer("tcp://"+ipaddress+":61616", hashMap.get("NICKNAME")), hashMap.get("NICKNAME"), hashMap.get("ADMIN")));
//						UserCommunication communication = vectoruser.get(hashMap.get("NICKNAME"));
//						//System.out.println("J'envoie sur " + communication.getMessageProducer().getQueue().getQueueName());
//						communication.sendMsg("Bonjour");
//					
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (JMSException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//				}
//				
//			}
//		});
//		waitingThread.start();
//	}
//	
//	
//	public void broadcast()
//	{
//		Thread broadcastThread = new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				try {
//					broadcastMessage();
//				} catch (JMSException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//			}
//		});
//		broadcastThread.start();
//	}
//	
//	public void broadcastMessage() throws JMSException, IOException, InterruptedException
//	{
//		Boolean done = false;
//		while(!done){
//			TextMessage message= consumer.receiveMessage();
//		
//			
//			
//			SocketCommunication socketCommunication = new SocketCommunication();
//			if(message!=null)
//			{
//				SocketMessage socketMessage = socketCommunication.convertStringtoSocketMessage(message.getText());
//				if(socketMessage.getNicknameDestinataire()!="")
//				{
//					UserCommunication destinataire = vectoruser.get(socketMessage.getNicknameDestinataire());
//					destinataire.sendMsg(message);
//				}
//				else
//				{
//					for(UserCommunication communication : vectoruser.values()) {
//						communication.sendMsg(message);
//					    
//					}
//				}	
//			}
//		}
//	}
//	*/
//	
//	
//
//	public HashMap<String, String> createConnection(Socket socket) throws IOException
//	   {
//		   Boolean admin = false;
//		   open();
//		 //  System.out.println("createConnection");
//		   HashMap<String, String> hashMap = new HashMap<>();
//			SocketCommunication communication = new SocketCommunication();
//			Boolean nicknameAvailable = false;
//			String nickname = "";
//			while(!nicknameAvailable||socket.isClosed())
//			{	
//				System.out.println("nicknameAvailable");
//				SocketMessage message = new SocketMessage("", "PSEUDO", "SERVEUR", SocketMessageType.INFO_PSEUDO);
//				communication.sendMessage(message, streamOut);
//				message = communication.convertStringtoSocketMessage(communication.receiveMessage(streamIn));
//				nickname = message.getMessageContent();
//				nicknameAvailable = true;
//				if(vectoruser.containsKey(nickname))
//				{
//					nicknameAvailable = false;
//				}
//			/*	for(UserCommunication userCommunication : vectoruser)
//				{
//					if(userCommunication.getNickname().equals(nickname))
//					{
//						nicknameAvailable = false;
//					}
//				}*/
//			}
//			if(nickname.toUpperCase().equals("ADMIN")){
//				SocketMessage message = new SocketMessage("", "PASSWORD", "SERVEUR", SocketMessageType.INFO_ADMIN);
//				communication.sendMessage(message, streamOut);
//				String password = communication.receiveMessage(streamIn);
//				if(password.toUpperCase().equals("ADMIN")) {
//					admin=true;
//				}
//				else
//				{
//					socket.close();
//				}
//			}
//			SocketMessage message = new SocketMessage("", "Success", "SERVEUR", SocketMessageType.INFO_SUCCESS);
//			communication.sendMessage(message, streamOut);
//			hashMap.put("NICKNAME", nickname);
//			hashMap.put("ADMIN", Boolean.toString(admin));
//			//System.out.println(hashMap.get("NICKNAME")+ " - " + hashMap.get("ADMIN"));
//			//close();
//			return hashMap;
//		   
//	   }
//	private void open(Socket my_socket) throws IOException
//	   {  	streamIn = new DataInputStream(new BufferedInputStream(my_socket.getInputStream()));
//	   		streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
//	   }
//	   
//	
//	
//	private void close() throws IOException
//	   {  if (streamOut != null)    socket.close();
//	      if (streamIn != null)  streamIn.close();
//	      
//	   }
//
//	/*   public static void main(String args[])
//	   {  	ServeurMultiClient server = null;
//			int port = 1002;
//			server = new ServeurMultiClient(port,"127.0.0.1");
//	   }
//	   
//	   */

	
}
