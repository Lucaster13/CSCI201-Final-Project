package data;

import java.io.Serializable;

public class PasswordEditRequest extends ClientRequest implements Serializable {
	private static final long serialVersionUID = 1;
	private int passID;
	private String property;
	private String value;
	
	public PasswordEditRequest(int passID, String property, String value) {
		super(ClientRequest.TYPE_EDIT_PASSWORD);
		this.passID=passID;
		this.property=property;
		this.value=value;				
	}

	public int getPasswordID() {
		return passID;
	}
	
	public String getProperty() {
		return property;
	}
	
	public String getValue() {
		return value;
	}
}
