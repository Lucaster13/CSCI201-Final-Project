package data;

import java.io.Serializable;

public class MasterPasswordChange extends ClientRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	private String newPass;
	
	public MasterPasswordChange(String newPass) {
		super(ClientRequest.TYPE_MASTER_PASS);
		this.newPass=newPass;
	}
	public String getNewPass() {
		return newPass;
	}
}
