package application2;

import java.io.IOException;

import application2.UserHomeController.DisplayPassword;
import client.ClientSocket;
import client.GuestInfo;
import client.PasswordStrength;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    @FXML private Button submitButton;
	
    @FXML public void initialize() 
    {
    	submitButton.setDefaultButton(true);
    	password.textProperty().addListener((observable, oldValue, newValue) -> {
    		//Change this part so that it corresponds to the real password strengths
    		int pwstrength = PasswordStrength.check(newValue);
    		if(pwstrength <= 1)
    		{
    			strength.setText("Very Weak");
    			strength.setTextFill(Color.RED);
    			if(pwstrength == -1) { //Must be more than 8
    				actiontarget.setText("It is recommended to have a password with more than 8 characters.");
    			} else if(pwstrength == -2) { //Cannot be a common password
    				actiontarget.setText("It is recommended to use a password that is in the list of commonly used passwords.");
    			} else if(pwstrength == -4) { //3 sequential characters
    				actiontarget.setText("It is recommended to have a password that does not have 3 sequential characters.");
    			} else {
    				actiontarget.setText("It is recommended for your passwords to contain:\r\n" + 
    						"	8 characters\r\n" + 
    						"	1 special character\r\n" + 
    						"	1 uppercase letter\r\n" + 
    						"	1 lowercase letter\r\n" + 
    						"	1 number");
    			}
    		}
    		else if(pwstrength == 2)
    		{
    			strength.setText("Mediocre");
    			strength.setTextFill(Color.ORANGE);
				actiontarget.setText("It is recommended for your passwords to contain:\r\n" + 
						"	8 characters\r\n" + 
						"	1 special character\r\n" + 
						"	1 uppercase letter\r\n" + 
						"	1 lowercase letter\r\n" + 
						"	1 number");
    		}
    		else if(pwstrength == 3)
    		{
    			strength.setText("Fine");
    			strength.setTextFill(Color.YELLOW);
				actiontarget.setText("It is recommended for your passwords to contain:\r\n" + 
						"	8 characters\r\n" + 
						"	1 special character\r\n" + 
						"	1 uppercase letter\r\n" + 
						"	1 lowercase letter\r\n" + 
						"	1 number");
    		}
    		else if(pwstrength == 4)
    		{
    			strength.setText("Strong");
    			strength.setTextFill(Color.LIGHTGREEN);
				actiontarget.setText("It is recommended for your passwords to contain:\r\n" + 
						"	8 characters\r\n" + 
						"	1 special character\r\n" + 
						"	1 uppercase letter\r\n" + 
						"	1 lowercase letter\r\n" + 
						"	1 number");
    		}
    		else if(pwstrength == 5)
    		{
    			strength.setText("Very Strong");
    			strength.setTextFill(Color.DARKGREEN);
   				actiontarget.setText("This password is very strong!");
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
			actiontarget.setText("Password fields do not match. Make sure you typed them in correctly!");
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
