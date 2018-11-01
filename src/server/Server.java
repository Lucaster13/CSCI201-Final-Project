package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	private Server() {
		startServer(1880);
	}
	
	private void startServer(int port) {
		if(DBHandler.createConnection()) {
			System.out.println("Connection to database successful!");
			ServerSocket ss = null;
			System.out.print("Attempting to bind on port "+port+"...");
			boolean serverRunning = false;
			try {
				ss = new ServerSocket(port);
				System.out.println("Success!\n");
				serverRunning=true;
			} catch(NumberFormatException|IOException e) {
				System.out.println("Unable to bind port.");
				serverRunning=false;
			}
			try {
				while(serverRunning) {
					Socket s=ss.accept();
					new ClientConnection(s);
				}
			} catch(IOException e) { 
				System.out.println("ioe:" + e.getMessage());
			}
		} else {
			System.out.println("Failed to connect to database.");
		}		
	}
	
	public static void main(String[] args) {
		new Server();
	}
}
