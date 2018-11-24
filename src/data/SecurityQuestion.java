package data;

import java.io.Serializable;

public class SecurityQuestion implements Serializable {
	private static final long serialVersionUID = 1;
	private int questionID;
	private String question;
	private String answer;
	public SecurityQuestion(int questionID, String question, String answer) {
		this.questionID=questionID;
		this.question=question;
		this.answer=answer;
	}
	public int getQuestionID() {
		return questionID;
	}
	public String getQuestion() {
		return question;
	}
	public String getAnswer() {
		return answer;
	}
}
