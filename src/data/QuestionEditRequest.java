package data;

import java.io.Serializable;

public class QuestionEditRequest extends ClientRequest implements Serializable {
	private static final long serialVersionUID = 1;
	private int passwordID;
	private int questionID;
	private String newQ;
	private String newA;
	
	public QuestionEditRequest(int passwordID, int questionID, String newQ, String newA) {
		super(ClientRequest.TYPE_EDIT_QUESTION);
		this.passwordID=passwordID;
		this.questionID=questionID;
		this.newQ=newQ;
		this.newA=newA;
	}
	
	public int getPasswordID() {
		return passwordID;
	}
	
	public int getQuestionID() {
		return questionID;
	}
	
	public String getQuestion() {
		return newQ;
	}
	
	public String getAnswer() {
		return newA;
	}
}
