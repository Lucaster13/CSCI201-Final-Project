package data;

import java.io.Serializable;

public class LoginCreation extends LoginCredentials implements Serializable {
	private static final long serialVersionUID = 1;
	private String email;
	
	public LoginCreation(String username, String hashpass, String email) {
		super(username, hashpass, TYPE_CREATE);
		this.email=email;
	}
	
	public String getEmail() {
		return email;
	}
}
