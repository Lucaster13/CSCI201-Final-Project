package data;

import java.io.Serializable;

public class QuestionEditRequest extends ClientRequest implements Serializable {
	private static final long serialVersionUID = 1;
	private int passwordID;
	private int questionID;
	private String property;
	private String value;
	
	public QuestionEditRequest(int passwordID, int questionID, String property, String value) {
		super(ClientRequest.TYPE_EDIT_QUESTION);
		this.passwordID=passwordID;
		this.questionID=questionID;
		this.property=property;
		this.value=value;
	}
	
	public int getPasswordID() {
		return passwordID;
	}
	
	public int getQuestionID() {
		return questionID;
	}
	
	public String getProperty() {
		return property;
	}
	
	public String getValue() {
		return value;
	}
}
