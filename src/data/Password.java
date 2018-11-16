package data;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Password implements Serializable {
	private static final long serialVersionUID = 1;
	private int passwordID;
	private String username;
	private String app_name;
	private String encrypted_pass;

	public Password(int passwordID, String username, String app_name, String encrypted_pass) {
		this.username=username;
		this.passwordID=passwordID;
		this.app_name=app_name;
		this.encrypted_pass=encrypted_pass;
	}
	public int getID() {
		return passwordID;
	}
	public String getUsername() {
		return username;
	}
	public String getName() {
		return app_name;
	}
	public String getPass() {
		return encrypted_pass;
	}
}
