package application2;

import java.io.IOException;

import client.ClientSocket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AddQuestionController 
{
	@FXML private Text actiontarget;
    @FXML private TextArea question;
    @FXML private TextField answer;
    @FXML private Button addButton;
    
    @FXML public void initialize() 
    {
    	addButton.setDefaultButton(true);	
    }
	
	@FXML protected void handleBackAction(ActionEvent event) 
    {
    	Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("PasswordDetails.fxml"));
			Scene scene = new Scene(root, 800, 500);
	        primaryStage.setTitle("Password Details");
	        primaryStage.setScene(scene);
	        primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	@FXML protected void handleAddAction(ActionEvent event) 
    {
    	if(question.getText().trim().isEmpty()||answer.getText().trim().isEmpty()) {
    		actiontarget.setText("Some fields were left blank.");
    		return;
    	}
    	ClientSocket.addQuestion(ClientSocket.getViewPassword().getPassID(), question.getText().trim(), answer.getText().trim());
		Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("PasswordDetails.fxml"));
			Scene scene = new Scene(root, 800, 500);
	        primaryStage.setTitle("Password Details");
	        primaryStage.setScene(scene);
	        primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
