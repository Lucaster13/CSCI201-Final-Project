package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import data.LoginCredentials;

public class ClientConnection extends Thread {
	private ObjectInputStream ois;
	private ObjectOutputStream oos;	
	
	public ClientConnection(Socket s) {
		try {
			ois = new ObjectInputStream(s.getInputStream());
			oos = new ObjectOutputStream(s.getOutputStream());
			this.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		try {
			login();
			/*
			try {
				
			} catch (InterruptedException e) { 
				//USER TIMED OUT, Log out
			} */
		} catch(ClientDisconnectException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/*
	 * Handle verifying the client's login information
	 */
	public void login() throws ClientDisconnectException {
		boolean loggedIn = false;
		String username = "";
		String hashPass = "";
		while(!loggedIn) {
			try {
				LoginCredentials msg = (LoginCredentials) ois.readObject();
				username = msg.getUsername();
				hashPass = msg.getPassword();
				if(msg.getType()==LoginCredentials.TYPE_CREATE) {
					
				}
				else if(msg.getType()==LoginCredentials.TYPE_LOGIN) {
					
				}
			} catch(IOException | ClassNotFoundException e) {
				throw new ClientDisconnectException();
			}
		}
	}
}

class TimeoutThread extends Thread {
	private static final int TIMEOUT_LENGTH = 15;
	private ClientConnection t;
	private int minElapsed=0;
	
	public TimeoutThread(ClientConnection t) {
		this.t=t;
	}
	
	public void run() {
		while(minElapsed < TIMEOUT_LENGTH) {
			try {
				Thread.sleep(1000000);
				minElapsed++;
			} catch (InterruptedException e) { }
		}
		t.interrupt();
	}
	
	public void reset() {
		minElapsed=0;
	}
}

class ClientDisconnectException extends Exception 
{ 
	private static final long serialVersionUID = 1L;

	public ClientDisconnectException() 
    {  
        super("Client Disconnected"); 
    } 
}