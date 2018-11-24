package data;

import java.io.Serializable;

public class ClientRequest implements Serializable {
	private static final long serialVersionUID = 1;
	
	public static final int TYPE_RETRIEVE_PASSWORDS = 0;
	public static final int TYPE_ADD_PASSWORD = 1;
	public static final int TYPE_REMOVE_PASSWORD = 2;
	public static final int TYPE_RETRIEVE_QUESTIONS = 3;
	public static final int TYPE_ADD_QUESTION = 4;
	public static final int TYPE_REMOVE_QUESTION = 5;
	public static final int TYPE_DELETE_ACCOUNT = 6;
	public static final int TYPE_EDIT_PASSWORD = 7;
	public static final int TYPE_EDIT_QUESTION = 8;
	public static final int TYPE_MASTER_PASS = 9;
	public static final int TYPE_LOGOUT = 10;

	private int type;
	public ClientRequest(int type) {
		this.type=type;
	}
	
	public int getType() {
		return type;
	}
}
