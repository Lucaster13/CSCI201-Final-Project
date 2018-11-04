package data;

public class LoginCredentials {
	private String username;
	private String hashpass;
	private int type;
	public static final int TYPE_CREATE = 0;
	public static final int TYPE_LOGIN = 1;
	
	public LoginCredentials(String username, String hashpass) {
		this(username, hashpass, TYPE_LOGIN);
	}
	
	public LoginCredentials(String username, String hashpass, int type) {
		this.username=username;
		this.hashpass=hashpass;
		this.type=type;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return hashpass;
	}
	
	public int getType() {
		return type;
	}
	
}
