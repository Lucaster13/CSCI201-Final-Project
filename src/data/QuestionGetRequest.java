package data;

import java.io.Serializable;

public class QuestionGetRequest extends ClientRequest implements Serializable {
	private static final long serialVersionUID = 1;
	private int passwordID;
	
	public QuestionGetRequest(int passwordID) {
		super(ClientRequest.TYPE_RETRIEVE_QUESTIONS);
		this.passwordID=passwordID;
	}
	
	public int getPasswordID() {
		return passwordID;
	}
}
