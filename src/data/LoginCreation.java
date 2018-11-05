package data;

import java.io.Serializable;

public class LoginCreation extends LoginCredentials implements Serializable {
	private static final long serialVersionUID = 1;
	private String phone;
	
	public LoginCreation(String username, String hashpass, String phone) {
		super(username, hashpass, TYPE_CREATE);
		this.phone=phone;
	}
	
	public String getPhone() {
		return phone;
	}
}
