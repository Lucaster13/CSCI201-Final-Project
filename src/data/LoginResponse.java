package data;

import java.io.Serializable;

public class LoginResponse implements Serializable  {
	private static final long serialVersionUID = 1;
	public static final int TYPE_INVALID = 0;
	public static final int TYPE_FAIL = 1;
	public static final int TYPE_SUCCESS = 2;
	
	private int type;
	
	public LoginResponse(int type) {
		this.type=type;
	}
	
	public int getType() {
		return type;
	}
}
