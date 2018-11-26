package application2;

import java.io.IOException;

import application2.UserHomeController.DisplayPassword;
import client.ClientSocket;
import client.GuestInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class NewPasswordController 
{
	@FXML private Text actiontarget;
    @FXML private TextField title;
    @FXML private TextField username;
    @FXML private TextField password;
    @FXML private TextField confirmPassword;
    @FXML private Label strength;
	
    @FXML public void initialize() 
    {
    	password.textProperty().addListener((observable, oldValue, newValue) -> {
    		//Change this part so that it corresponds to the real password strengths
    		if(password.getLength() < 2)
    		{
    			strength.setText("Very Weak");
    			strength.setTextFill(Color.RED);
    		}
    		else if(password.getLength() < 4)
    		{
    			strength.setText("Mediocre");
    			strength.setTextFill(Color.ORANGE);
    		}
    		else if(password.getLength() < 6)
    		{
    			strength.setText("Fine");
    			strength.setTextFill(Color.YELLOW);
    		}
    		else if(password.getLength() < 8)
    		{
    			strength.setText("Strong");
    			strength.setTextFill(Color.LIGHTGREEN);
    		}
    		else
    		{
    			strength.setText("Very Strong");
    			strength.setTextFill(Color.DARKGREEN);
    		}
    	});
    }
    
	@FXML protected void handleBackAction(ActionEvent event) 
    {
		if(ClientSocket.isGuest())
        {
    		Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
	    	Parent root;
			try {
				root = FXMLLoader.load(getClass().getResource("GuestHome.fxml"));
				Scene scene = new Scene(root, 800, 500);
			    
		        primaryStage.setTitle("Guest Home");
		        primaryStage.setScene(scene);
		        primaryStage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        else 
        {
	    	Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
	    	Parent root;
			try {
				root = FXMLLoader.load(getClass().getResource("UserHome.fxml"));
				Scene scene = new Scene(root, 800, 500);
			    
		        primaryStage.setTitle("User Home");
		        primaryStage.setScene(scene);
		        primaryStage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }
	
	@FXML protected void handleCreateAction(ActionEvent event) 
    {
        //actiontarget.setText("Sign in button pressed");
    	//System.out.println("Username: " + username.getText());
		if(title.getText().isEmpty()) {
			actiontarget.setText("Please provide an application name");
			return;
		}
		if(username.getText().isEmpty()) {
			actiontarget.setText("Please provide the associated username");
			return;
		}
		if(password.getText().isEmpty()) {
			actiontarget.setText("Please provide the password to store");
			return;
		}
		if(!password.getText().equals(confirmPassword.getText())) {
			actiontarget.setText("Passwords do not match. Make sure you typed them in correctly!");
			return;
		}
		DisplayPassword dp=null;
		boolean error = false;
		if(ClientSocket.isGuest()) {
			dp = GuestInfo.addPassword(title.getText(), username.getText(), password.getText());
		} else {
			int passID = ClientSocket.addPassword(title.getText(), username.getText(), password.getText());
			if(passID==0) error = true;
			else {
				dp = new DisplayPassword(passID, title.getText(), username.getText(), password.getText());
			}
		}
		if(!error) {
	    	Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
	    	Parent root;
			try {
				ClientSocket.setViewPassword(dp);
				root = FXMLLoader.load(getClass().getResource("PasswordDetails.fxml"));
				Scene scene = new Scene(root, 800, 500);
			    
		        primaryStage.setTitle("Password Details");
		        primaryStage.setScene(scene);
		        primaryStage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else { //TODO: DISPLAY ERROR ADDING PASSWORD
			actiontarget.setText("Error adding password.");
		}
    }
	
	@FXML protected void handleAddQuestionAction(ActionEvent event) 
    {
        if(ClientSocket.isGuest())
        {
        	actiontarget.setText("Make account to access feature");
        }
        else 
        {
	    	Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
	    	Parent root;
			try {
				root = FXMLLoader.load(getClass().getResource("AddQuestion.fxml"));
				Scene scene = new Scene(root, 800, 500);
			    
		        primaryStage.setTitle("Add Question");
		        primaryStage.setScene(scene);
		        primaryStage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }
}
