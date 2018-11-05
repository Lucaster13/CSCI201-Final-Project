package data;

import java.io.Serializable;

public class PasswordRemoveRequest extends ClientRequest implements Serializable {
	private static final long serialVersionUID = 1;
	private int passwordID;
	
	public PasswordRemoveRequest(int passwordID) {
		super(ClientRequest.TYPE_REMOVE_PASSWORD);
		this.passwordID=passwordID;
	}
	
	public int getPasswordID() {
		return passwordID;
	}
}
