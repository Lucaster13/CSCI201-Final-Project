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
	private Date last_update;
	private Date suggested_reset;
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public Password(int passwordID, String username, String app_name, String encrypted_pass, String last_update, String suggested_reset) {
		this.username=username;
		this.passwordID=passwordID;
		this.app_name=app_name;
		this.encrypted_pass=encrypted_pass;
		try {
			this.last_update=dateFormat.parse(last_update);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			this.suggested_reset=dateFormat.parse(suggested_reset);
		} catch (ParseException e) {
			e.printStackTrace();
		}
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
	public Date getLastUpdate() {
		return last_update;
	}
	public Date getSuggestedReset() {
		return suggested_reset;
	}
	public long numDaysUntilReset() {
		return Math.round((suggested_reset.getTime() - last_update.getTime()) / (double) 86400000);
	}
}
