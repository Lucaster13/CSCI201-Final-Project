package application2;

import java.io.IOException;

import client.ClientSocket;
import data.LoginResponse;
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

public class NewAccountController 
{
	@FXML private Text actiontarget;
    @FXML private TextField username;
    @FXML private TextField password;
    @FXML private TextField confirmPassword;
    @FXML private TextField email;
    @FXML private Label strength;
	
    @FXML public void initialize() 
    {
    	password.textProperty().addListener((observable, oldValue, newValue) -> {
    		//TODO: Change this part so that it corresponds to the real password strengths
    		if(password.getLength() < 3)
    		{
    			strength.setText("Poor");
    			strength.setTextFill(Color.RED);
    		}
    		else if(password.getLength() < 6)
    		{
    			strength.setText("Fine");
    			strength.setTextFill(Color.YELLOW);
    		}
    		else
    		{
    			strength.setText("Great");
    			strength.setTextFill(Color.GREEN);
    		}
    	});
    }
    
	@FXML protected void handleBackAction(ActionEvent event) 
    {
        if(ClientSocket.getLastPage().equals("GuestHome")) {
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
        } else {
        	Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        	Parent root;
    		try {
    			root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
    			Scene scene = new Scene(root, 800, 500);
    		    
    	        primaryStage.setTitle("Login Page");
    	        primaryStage.setScene(scene);
    	        primaryStage.show();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
        }
    }
	
	@FXML protected void handleRegisterAction(ActionEvent event) 
    {
		String emailRegex = "^[-a-z0-9~!$%^&*_=+}{\\'?]+(\\.[-a-z0-9~!$%^&*_=+}{\\'?]+)*@([a-z0-9_][-a-z0-9_]*(\\.[-a-z0-9_]+)*\\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,5})?$";
		if(username.getText().isEmpty()) {
			actiontarget.setText("Username cannot be empty.");
		} else if(password.getText().isEmpty()) { //TODO: check that it meets minimum password strength
			actiontarget.setText("Password cannot be empty.");
		} else if(!password.getText().equals(confirmPassword.getText())) { 
			actiontarget.setText("Passwords do not match.");
		} else if(email.getText().isEmpty()||!email.getText().matches(emailRegex)) {
			actiontarget.setText("Email is invalid.");
		} else { //Valid inputs have been used
			int loginSuccess = ClientSocket.createUser(username.getText(), password.getText(), email.getText());
			if(loginSuccess == LoginResponse.TYPE_SUCCESS) { // Successful login
				System.out.println("Valid login creation");
				Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		    	Parent root;
				try {
					ClientSocket.setLastPage("NewAccount");
					root = FXMLLoader.load(getClass().getResource("EmailVerify.fxml"));
					Scene scene = new Scene(root, 800, 500);
				    
			        primaryStage.setTitle("Email Verify");
			        primaryStage.setScene(scene);
			        primaryStage.show();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if(loginSuccess == LoginResponse.TYPE_INVALID) { 
				actiontarget.setText("Username already exists.");
			} else { 
				actiontarget.setText("Failed to communicate with server.");
			}
		}
    }
}
