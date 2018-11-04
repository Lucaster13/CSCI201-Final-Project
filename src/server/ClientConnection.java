package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import data.LoginCreation;
import data.LoginCredentials;
import data.LoginResponse;

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
	
	/*
	 * Write an object to the output stream & flush
	 */
	public void sendMsg(Object obj) throws IOException {
		oos.writeObject(obj);
		oos.flush();
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
				boolean correctCreds = false;
				String phone="";
				if(msg.getType()==LoginCredentials.TYPE_CREATE) {
					LoginCreation info = (LoginCreation) msg;
					int userID = DBHandler.createUser(username, hashPass, info.getPhone());
					if(userID == -1) { //Username already exists
						//Send response that username is in use
						sendMsg(new LoginResponse(LoginResponse.TYPE_INVALID));
					} else if(userID == 0) {
						//Send response that creation failed
						sendMsg(new LoginResponse(LoginResponse.TYPE_FAIL));
					} else {
						phone = info.getPhone();
						correctCreds = true;
					}
				}
				else if(msg.getType()==LoginCredentials.TYPE_LOGIN) {
					DBUserInfo info = DBHandler.verifyUser(username, hashPass);
					if(info == null) {
						//Send response invalid credentials
						sendMsg(new LoginResponse(LoginResponse.TYPE_INVALID));
					} else {
						phone = info.getPhone();
						correctCreds = true;
					}
				}
				if(correctCreds) {
					//Send response of success
					sendMsg(new LoginResponse(LoginResponse.TYPE_SUCCESS));
					//Send SMS text and await code verification
					/*
					 * SEND TEXT HERE
					 */
					boolean correctCode = false;
					while(!correctCode) {
						//WAIT FOR REPLY OF CODE FROM CLIENT
						String inputCode = (String) ois.readObject();
						/*
						 * CHECK IF CODE RECIEVED MATCHES CODE SENT VIA SMS
						 */
						if(!correctCode) {
							sendMsg(new LoginResponse(LoginResponse.TYPE_FAIL));
						}
					}
					sendMsg(new LoginResponse(LoginResponse.TYPE_SUCCESS));
					loggedIn=true;
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