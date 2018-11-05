package data;

import java.io.Serializable;

public class ServerResponse implements Serializable {
	private static final long serialVersionUID = 1;
	private boolean success;
	
	public ServerResponse(boolean success) {
		this.success=success;
	}
	
	public boolean getSuccess() {
		return success;
	}
}
