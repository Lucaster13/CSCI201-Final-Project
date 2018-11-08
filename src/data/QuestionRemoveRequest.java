package data;

import java.io.Serializable;

public class QuestionRemoveRequest extends ClientRequest implements Serializable {
	private static final long serialVersionUID = 1;
	private int passwordID;
	private int questionID;
	
	public QuestionRemoveRequest(int passwordID, int questionID) {
		super(ClientRequest.TYPE_REMOVE_QUESTION);
		this.passwordID=passwordID;
		this.questionID=questionID;
	}
	
	public int getPasswordID() {
		return passwordID;
	}
	
	public int getQuestionID() {
		return questionID;
	}
}
