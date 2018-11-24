package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import data.ClientRequest;
import data.LoginCreation;
import data.LoginCredentials;
import data.LoginResponse;
import data.MasterPasswordChange;
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

public class ClientConnection extends Thread {
	private ObjectInputStream ois;
	private ObjectOutputStream oos;	
	private int userID=0;
	
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
			listen();
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
				String email="";
				if(msg.getType()==LoginCredentials.TYPE_CREATE) {
					LoginCreation info = (LoginCreation) msg;
					userID = DBHandler.createUser(username, hashPass, info.getEmail());
					if(userID == -1) { //Username already exists
						//Send response that username is in use
						System.out.println("User Exists");
						sendMsg(new LoginResponse(LoginResponse.TYPE_INVALID));
					} else if(userID == 0) {
						//Send response that creation failed
						System.out.println("Fail creation");
						sendMsg(new LoginResponse(LoginResponse.TYPE_FAIL));
					} else {
						email = info.getEmail();
						correctCreds = true;
					}
				}
				else if(msg.getType()==LoginCredentials.TYPE_LOGIN) {
					DBUserInfo info = DBHandler.verifyUser(username, hashPass);
					if(info == null) {
						//Send response invalid credentials
						System.out.println("Invalid creds");
						sendMsg(new LoginResponse(LoginResponse.TYPE_INVALID));
					} else {
						userID = info.getUserID();
						email = info.getEmail();
						correctCreds = true;
					}
				}
				if(correctCreds) {
					//Send response of success
					System.out.println("Successful login");
					sendMsg(new LoginResponse(LoginResponse.TYPE_SUCCESS));
					sendMsg(email);
					//Send email and await code verification
					EmailSender sendCode = new EmailSender(email);
					boolean correctCode = false;
					while(!correctCode) {
						//Wait for reply from client
						Integer inputCode = (Integer) ois.readObject();
						if(inputCode.intValue() == sendCode.getCode()) {
							correctCode = true;
						}
						if(!correctCode) {
							sendMsg(new LoginResponse(LoginResponse.TYPE_FAIL));
						}
					}
					sendMsg(new LoginResponse(LoginResponse.TYPE_SUCCESS));
					loggedIn=true;
				}
			} catch(IOException | ClassNotFoundException e) {
				System.out.println(e.getMessage());
				throw new ClientDisconnectException();
			}
		}
	}

	/*
	 * After the user has been logged in, wait for responses from the client
	 */
	public void listen() throws ClientDisconnectException {
		try {
			boolean loggedOut=false;
			while(!loggedOut) {
				//Obtain request from the client
				ClientRequest msg = (ClientRequest) ois.readObject();
				switch(msg.getType()) {
				case ClientRequest.TYPE_RETRIEVE_PASSWORDS:
					sendPasswordList();
					break;
				case ClientRequest.TYPE_ADD_PASSWORD:
					addPassword((PasswordAddRequest) msg);
					break;
				case ClientRequest.TYPE_REMOVE_PASSWORD:
					removePassword((PasswordRemoveRequest) msg);
					break;
				case ClientRequest.TYPE_RETRIEVE_QUESTIONS:
					sendQuestionList((QuestionGetRequest) msg);
					break;
				case ClientRequest.TYPE_ADD_QUESTION:
					addQuestion((QuestionAddRequest) msg);
					break;
				case ClientRequest.TYPE_REMOVE_QUESTION:
					removeQuestion((QuestionRemoveRequest) msg);
					break;
				case ClientRequest.TYPE_DELETE_ACCOUNT:
					deleteAccount();
					break;
				case ClientRequest.TYPE_EDIT_PASSWORD:
					editPassword((PasswordEditRequest) msg);
					break;
				case ClientRequest.TYPE_EDIT_QUESTION:
					editQuestion((QuestionEditRequest) msg);
					break;
				case ClientRequest.TYPE_MASTER_PASS:
					changeMaster((MasterPasswordChange) msg);
					break;
				case ClientRequest.TYPE_LOGOUT:
					loggedOut=true;
					break;
				}
			}			
		} catch(IOException | ClassNotFoundException e) {
			System.out.println(e.getMessage());
			throw new ClientDisconnectException();
		}
	}
	
	public void sendPasswordList() throws IOException {
		ArrayList<Password> passwords = DBHandler.getPasswords(userID);
		sendMsg(passwords);
	}
	
	public void addPassword(PasswordAddRequest request) throws IOException {
		int id = DBHandler.addPassword(userID, request.getUsername(), request.getAppName(), request.getPassword());
		sendMsg(new ServerResponse(id));
	}
	
	public void removePassword(PasswordRemoveRequest request) throws IOException {
		boolean success = DBHandler.removePassword(userID, request.getPasswordID());
		sendMsg(new ServerResponse(success));
	}
	
	public void editPassword(PasswordEditRequest request) throws IOException {
		boolean success = DBHandler.editPassword(userID, request.getPasswordID(), request.getProperty(), request.getValue());
		sendMsg(new ServerResponse(success));
	}
	
	public void editQuestion(QuestionEditRequest request) throws IOException {
		boolean success = DBHandler.editQuestion(userID, request.getPasswordID(), request.getQuestionID(), request.getQuestion(), request.getAnswer());
		sendMsg(new ServerResponse(success));
	}	
	
	public void sendQuestionList(QuestionGetRequest request) throws IOException {
		ArrayList<SecurityQuestion> questions = DBHandler.getSecurityQuestions(request.getPasswordID());
		sendMsg(questions);
	}
	
	public void addQuestion(QuestionAddRequest request) throws IOException {
		boolean success = DBHandler.addQuestion(request.getPasswordID(), request.getQuestion(), request.getAnswer());
		sendMsg(new ServerResponse(success));
	}
	
	public void removeQuestion(QuestionRemoveRequest request) throws IOException {
		boolean success = DBHandler.removeQuestion(userID, request.getPasswordID(), request.getQuestionID());
		sendMsg(new ServerResponse(success));
	}
	
	public void changeMaster(MasterPasswordChange request) {
		DBHandler.changeMaster(userID, request.getNewPass());
	}
	
	public void deleteAccount() throws IOException {
		DBHandler.deleteAccount(userID);
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