package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import data.ClientRequest;
import data.LoginCreation;
import data.LoginCredentials;
import data.LoginResponse;
import data.Password;
import data.PasswordAddRequest;
import data.PasswordRemoveRequest;
import data.QuestionAddRequest;
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
	
	public static boolean createConnection() {
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
			 * 		 Validate email here or where this function is called
			 */
			try {
				//Send info to server
				sendMsg(new LoginCreation(username, password, email));
				LoginResponse response = (LoginResponse) ois.readObject();
				result = response.getType();
				if(result == LoginResponse.TYPE_SUCCESS) {
					email = (String) ois.readObject();
				}
			} catch(IOException|ClassNotFoundException e) { }
		}
		return result;
	}
	
	/*
	 * Verifies the code sent to the user's email
	 * Returns: LoginResponse.TYPE_FAIL if the code is incorrect
	 * 			LoginResponse.TYPE_SUCCESS if the code is correct
	 */
	public static int verifyCode(Integer input) {
		int result = LoginResponse.TYPE_FAIL;
		try {
			//Send info to server
			sendMsg(input);
			LoginResponse response = (LoginResponse) ois.readObject();
			result = response.getType();
			loggedIn=true;
		} catch(IOException|ClassNotFoundException e) { }
		return result;
	}
	
	/*
	 * Returns: null if error obtaining data
	 * 			ArrayList of the Passwords from database if successful
	 */
	public static ArrayList<Password> getPasswords() {
		ArrayList<Password> result = null;
		try {
			sendMsg(new ClientRequest(ClientRequest.TYPE_RETRIEVE_PASSWORDS));
			result = (ArrayList<Password>) ois.readObject();
		} catch(IOException|ClassNotFoundException e) { }
		return result;
	}
	
	/*
	 * Returns: false if unsuccessful
	 * 			true if successfully added to database
	 */
	public static boolean addPassword(String username, String app_name, String password) {
		boolean result = false;
		try {
			sendMsg(new PasswordAddRequest(username, app_name, password));
			ServerResponse response = (ServerResponse) ois.readObject();
			result = response.getSuccess();
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
			result = response.getSuccess();
		} catch(IOException|ClassNotFoundException e) { }
		return result;
	}
	
	/*
	 * Returns: null if error obtaining data
	 * 			ArrayList of the SecurityQuestions from database if successful
	 */
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
			result = response.getSuccess();
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
			result = response.getSuccess();
		} catch(IOException|ClassNotFoundException e) { }
		return result;
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
		}
	}
	
	public static boolean activeSocket() {
		return connected;
	}
}
