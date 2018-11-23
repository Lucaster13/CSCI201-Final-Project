package data;

import java.io.Serializable;

public class ServerResponse implements Serializable {
	private static final long serialVersionUID = 1;
	private int status;
	
	public ServerResponse(int status) {
		this.status=status;
	}
	
	public ServerResponse(boolean success) {
		if(success) {
			this.status=1;
		} else {
			this.status=0;
		}
		
	}
	
	public int getStatus() {
		return status;
	}
}
