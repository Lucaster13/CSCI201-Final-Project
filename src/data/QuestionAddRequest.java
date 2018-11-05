package data;

import java.io.Serializable;

public class QuestionAddRequest extends ClientRequest implements Serializable {
	private static final long serialVersionUID = 1;
	private int passwordID;
	private String question;
	private String answer;
	
	public QuestionAddRequest(int passwordID, String question, String answer) {
		super(ClientRequest.TYPE_ADD_QUESTION);
		this.passwordID=passwordID;
		this.question=question;
		this.answer=answer;
	}
	
	public int getPasswordID() {
		return passwordID;
	}
	
	public String getQuestion() {
		return question;
	}
	
	public String getAnswer() {
		return answer;
	}
}
