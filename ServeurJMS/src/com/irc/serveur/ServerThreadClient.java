package com.irc.serveur;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.irc.socket.SocketCommunication;
import com.irc.socket.SocketMessage;


public class ServerThreadClient extends Thread {

	private Socket socket   = null;
	private Server server   = null;
	private String nickname   ="";
	private Boolean admin = false;
	private DataInputStream streamIn =  null;
	private DataOutputStream streamOut = null;
	private Integer index = 0;
	private SocketCommunication socketCommunication = null;
	private boolean stopThread = false;


	
	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public DataInputStream getStreamIn() {
		return streamIn;
	}

	public void setStreamIn(DataInputStream streamIn) {
		this.streamIn = streamIn;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public ServerThreadClient(Server _server, Socket _socket,Integer _index,String _nickname) 
	{
		server = _server;
		socket = _socket;
		nickname = _nickname;
		index = _index;
		socketCommunication = new SocketCommunication();
	}
	
	public ServerThreadClient(Server _server, Socket _socket,Integer _index,String _nickname,String admin_status) 
	{
		server = _server;
		socket = _socket;
		nickname = _nickname;
		index = _index;
		admin = Boolean.valueOf(admin_status);
		socketCommunication = new SocketCommunication();
	
		
		
		

	}
	
	public void sendMessage(SocketMessage message) throws IOException
	{
		socketCommunication.sendMessage(message, streamOut);
	}
	
	public void run()
	{  
		System.out.println("Server Thread " + nickname + " running.");
		while (socket != null)
		{  
			try
			{  
				String lecture = streamIn.readUTF();
				//System.out.println("Lecture du flux --> " + lecture);
				SocketMessage socketMessage = socketCommunication.convertStringtoSocketMessage(lecture);
				switch (socketMessage.getMessageType()) {
				case MESSAGE_TEXT:
					server.sendmessageSocket(socketMessage);
					break;
				case MESSAGE_QUIT:
					server.disconnectSocket(this);
					break;
				default:
					break;
				}
				//socketCommunication.convertStringtoSocketMessage(streamIn.readUTF());
				//System.out.println(streamIn.readUTF());	
				
			}
			catch(IOException ioe) {
				currentThread().interrupt();
			}
		}
	}
	@Override
    public void interrupt() {
        super.interrupt();
        try {
        	closeSocket();// Fermeture du flux si l'interruption n'a pas fonctionné.
        } catch (IOException e) {} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public void openThread() throws IOException
	{  
		openSocket();
		this.start();
	}
	public void openSocket() throws IOException
	{  
		streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
	}
	public void closeSocket() throws IOException, InterruptedException
	{  
		if (socket != null)    socket.close();
		if (streamIn != null)  streamIn.close();
		if (streamOut != null)  streamOut.close();
		
	}
	public void closeThread() throws IOException, InterruptedException
	{  
		this.interrupt();
		closeSocket();
		
	}

	public DataOutputStream getStreamOut() {
		return streamOut;
	}

	public void setStreamOut(DataOutputStream streamOut) {
		this.streamOut = streamOut;
	}

	


}
