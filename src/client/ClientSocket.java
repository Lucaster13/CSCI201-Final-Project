package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import application2.UserHomeController.DisplayPassword;
import data.ClientRequest;
import data.LoginCreation;
import data.LoginCredentials;
import data.LoginResponse;
import data.Password;
import data.PasswordAddRequest;
import data.PasswordEditRequest;
import data.PasswordRemoveRequest;
import data.QuestionAddRequest;
import data.QuestionEditRequest;
import data.QuestionGetRequest;
import data.QuestionRemoveRequest;
import data.SecurityQuestion;
import data.ServerResponse;

public class ClientSocket {
	private static String hostname = "localhost";
	private static int port = 1880;
	private static Socket s = null;
	private static ObjectInputStream ois;
	private static ObjectOutputStream oos;
	private static boolean connected = false;
	private static String email=null;
	private static boolean loggedIn=false;
	private static String lastPage="";
	private static DisplayPassword viewPassword=null;
	
	private static boolean createConnection() {
		try {
			s = new Socket(hostname, port);
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			email= null;
			loggedIn = false;
			connected = true;
		} catch(IOException e) {
			connected = false;
		}
		return connected;
	}
	
	/*
	 * Write an object to the output stream & flush
	 */
	public static void sendMsg(Object obj) throws IOException {
		oos.writeObject(obj);
		oos.flush();
	}
	
	/*
	 *  Returns -1 if connection to server failed
	 *  	LoginResponse.TYPE_INVALID if invalid credentials
	 *  	LoginResponse.TYPE_SUCCESS if valid credentials
	 */
	public static int login(String username, String password) {
		int result = -1;
		if(!connected) createConnection();
		if(connected) {
			/*
			 * TODO: Hash password
			 */
			try {
				//Send credentials to server
				sendMsg(new LoginCredentials(username, password));
				LoginResponse response = (LoginResponse) ois.readObject();
				result = response.getType();
				if(result == LoginResponse.TYPE_SUCCESS) {
					email = (String) ois.readObject();
					loggedIn=true;
				}
			} catch(IOException|ClassNotFoundException e) { }
		}
		return result;
	}
	
	/*
	 *  Returns -1 if connection to server failed
	 *  	LoginResponse.TYPE_INVALID if a user with that username already exists
	 *  	LoginResponse.TYPE_FAIL if creation into database failed
	 *  	LoginResponse.TYPE_SUCCESS if valid credentials
	 */
	public static int createUser(String username, String password, String email) {
		int result = -1;
		if(!connected) createConnection();
		if(connected) {
			/*
			 * TODO: Hash password
			 */
			try {
				//Send info to server
				sendMsg(new LoginCreation(username, password, email));
				LoginResponse response = (LoginResponse) ois.readObject();
				result = response.getType();
				if(result == LoginResponse.TYPE_SUCCESS) {
					ClientSocket.email = (String) ois.readObject();
				}
			} catch(IOException|ClassNotFoundException e) { }
		}
		return result;
	}
	
	/*
	 * Verifies the code sent to the user's email
	 * Returns: false if the code is incorrect
	 * 			true if the code is correct
	 */
	public static boolean verifyCode(Integer input) {
		boolean result = false;
		try {
			//Send info to server
			System.out.println("Verify: "+input.intValue());
			sendMsg(input);
			LoginResponse response = (LoginResponse) ois.readObject();
			if(response.getType()==LoginResponse.TYPE_SUCCESS) {
				result = true;
				if(isGuest()) { //Was a guest
					ArrayList<DisplayPassword> guestPass = GuestInfo.getPasswords();
					for(DisplayPassword pass : guestPass) {
						addPassword(pass.getUsername(), pass.getAccountName(), pass.getPassword());
					}
					loggedIn=true;
				}
			}
		} catch(IOException|ClassNotFoundException e) { }
		return result;
	}
	
	/*
	 * Returns: null if error obtaining data
	 * 			ArrayList of the Passwords from database if successful
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Password> getPasswords() {
		ArrayList<Password> result = null;
		try {
			sendMsg(new ClientRequest(ClientRequest.TYPE_RETRIEVE_PASSWORDS));
			result = (ArrayList<Password>) ois.readObject();
		} catch(IOException|ClassNotFoundException e) { }
		return result;
	}
	
	/*
	 * Returns: 0 if unsuccessful
	 * 			passwordID if successfully added to database
	 */
	public static int addPassword(String app_name, String username, String password) {
		int result = 0;
		try {
			sendMsg(new PasswordAddRequest(username, app_name, password));
			ServerResponse response = (ServerResponse) ois.readObject();
			result = response.getStatus();
		} catch(IOException|ClassNotFoundException e) { }
		return result;
	}
	
	/*
	 * Returns: false if unsuccessful
	 * 			true if successfully removed from database
	 */
	public static boolean removePassword(int passwordID) {
		boolean result = false;
		try {
			sendMsg(new PasswordRemoveRequest(passwordID));
			ServerResponse response = (ServerResponse) ois.readObject();
			result = (response.getStatus()!=0);
		} catch(IOException|ClassNotFoundException e) { }
		return result;
	}
	
	public static boolean editPassword(int passwordID, String property, String value) {
		boolean result = false;
		try {
			sendMsg(new PasswordEditRequest(passwordID, property, value));
			ServerResponse response = (ServerResponse) ois.readObject();
			result = (response.getStatus()!=0);
		} catch(IOException|ClassNotFoundException e) { }
		return result;
	}
	
	public static boolean editQuestion(int passwordID, int questionID, String property, String value) {
		boolean result = false;
		try {
			sendMsg(new QuestionEditRequest(passwordID, questionID, property, value));
			ServerResponse response = (ServerResponse) ois.readObject();
			result = (response.getStatus()!=0);
		} catch(IOException|ClassNotFoundException e) { }
		return result;
	}
	
	/*
	 * Returns: null if error obtaining data
	 * 			ArrayList of the SecurityQuestions from database if successful
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<SecurityQuestion> getQuestions(int passwordID) {
		ArrayList<SecurityQuestion> result = null;
		try {
			sendMsg(new QuestionGetRequest(passwordID));
			result = (ArrayList<SecurityQuestion>) ois.readObject();
		} catch(IOException|ClassNotFoundException e) { }
		return result;
	}
	
	/*
	 * Returns: false if unsuccessful
	 * 			true if successfully removed from database
	 */
	public static boolean addQuestion(int passwordID, String question, String answer) {
		boolean result = false;
		try {
			sendMsg(new QuestionAddRequest(passwordID, question, answer));
			ServerResponse response = (ServerResponse) ois.readObject();
			result = (response.getStatus()!=0);
		} catch(IOException|ClassNotFoundException e) { }
		return result;
	}
	
	/*
	 * Returns: false if unsuccessful
	 * 			true if successfully removed from database
	 */
	public static boolean removeQuestion(int passwordID, int questionID) {
		boolean result = false;
		try {
			sendMsg(new QuestionRemoveRequest(passwordID, questionID));
			ServerResponse response = (ServerResponse) ois.readObject();
			result = (response.getStatus()!=0);
		} catch(IOException|ClassNotFoundException e) { }
		return result;
	}
	
	public static void deleteAccount() {
		try {
			sendMsg(new ClientRequest(ClientRequest.TYPE_DELETE_ACCOUNT));
		} catch(IOException e) { }
	}
	
	/*
	 * Returns the email of the user if loggedIn
	 */
	public static String getEmail() {
		return email;
	}
	
	public static void logout() {
		try {
			if(s!=null) s.close();
		} catch (IOException e) {
			
		} finally {
			s=null;
			email=null;
			loggedIn=false;
			connected=false;
			lastPage="";
			viewPassword=null;
		}
	}
	
	public static boolean activeSocket() {
		return connected;
	}
	
	public static void setLastPage(String lastPage) {
		ClientSocket.lastPage=lastPage;
	}
	
	public static String getLastPage() {
		return lastPage;
	}
	
	public static boolean isGuest() {
		return !loggedIn;
	}
	
	public static void setViewPassword(DisplayPassword pass) {
		viewPassword=pass;
	}
	
	public static DisplayPassword getViewPassword() {
		return viewPassword;
	}
}
