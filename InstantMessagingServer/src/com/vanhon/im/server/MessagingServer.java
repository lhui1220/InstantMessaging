package com.vanhon.im.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class MessagingServer implements IMessagingServerConstants{
	
	private ServerSocket serverSocket;
	private boolean isStarted = false;
	
	private OnStateChangedListener listener;
	
	public void setOnStateChangedListener(OnStateChangedListener listener){
		this.listener = listener;
	}
	
	public void start(){
		this.start(DEFAULT_TCP_PORT);
	}
	
	public void start(int port){

		try {
			serverSocket = new ServerSocket(port);
System.out.println("Server has been started at port: "+serverSocket.getLocalPort());
			isStarted = true;
		} catch (IOException e) {
			e.printStackTrace();
			isStarted = false;
		}
		
		//notify the caller that the server be started successful
		if(listener != null){
			listener.onStart(isStarted);
		}
		//start connect listen thread
		new Thread(new ListenConnectTask()).start();
		
	}
	
	public class ListenConnectTask implements Runnable{

		@Override
		public void run() {
			while(isStarted){
				try {
					Socket s = serverSocket.accept();
System.out.println("a client has connected !");
				} catch(SocketException ex){
					isStarted = false;
System.out.println("Server has been stoped !");
					ex.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(listener != null){
				listener.onStop(!isStarted);
			}
			
		}
		
	}
	
	public void stop(){
		if(serverSocket != null){
			try {
				serverSocket.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		this.isStarted = false;
	}
	
	public interface OnStateChangedListener{
		public void onStart(boolean success);
		public void onStop(boolean success);
	}
}
