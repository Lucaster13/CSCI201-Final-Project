package data;

public class LoginCreation extends LoginCredentials {
	private String phone;
	
	public LoginCreation(String username, String hashpass, String phone) {
		super(username, hashpass, TYPE_CREATE);
	}
	
	public String getPhone() {
		return phone;
	}
}
