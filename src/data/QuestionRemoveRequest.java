package data;

import java.io.Serializable;

public class QuestionRemoveRequest extends ClientRequest implements Serializable {
	private static final long serialVersionUID = 1;
	private int questionID;
	
	public QuestionRemoveRequest(int questionID) {
		super(ClientRequest.TYPE_REMOVE_QUESTION);
		this.questionID=questionID;
	}
	
	public int getPasswordID() {
		return questionID;
	}
}
