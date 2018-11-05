package data;

import java.io.Serializable;

public class PasswordAddRequest extends ClientRequest implements Serializable {
	private static final long serialVersionUID = 1;
	private String username;
	private String app_name;
	private String password;
	
	public PasswordAddRequest(String username, String app_name, String password) {
		super(ClientRequest.TYPE_ADD_PASSWORD);
		this.username=username;
		this.app_name=app_name;
		this.password=password;
	}

	public String getUsername() {
		return username;
	}
	
	public String getAppName() {
		return app_name;
	}
	
	public String getPassword() {
		return password;
	}
}
