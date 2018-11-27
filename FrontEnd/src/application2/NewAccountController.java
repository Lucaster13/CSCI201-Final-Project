package application2;

import java.io.IOException;

import client.ClientSocket;
import client.PasswordStrength;
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
    		//Change this part so that it corresponds to the real password strengths
    		int pwstrength = PasswordStrength.check(newValue);
    		if(pwstrength <= 1)
    		{
    			strength.setText("Very Weak");
    			strength.setTextFill(Color.RED);
    			if(pwstrength == -1) { //Must be more than 8
    				actiontarget.setText("Master password must be more than 8 characters.");
    			} else if(pwstrength == -2) { //Cannot be a common password
    				actiontarget.setText("Master password cannot be a common password.");
    			} else if(pwstrength == -4) { //3 sequential characters
    				actiontarget.setText("Master password cannot contain 3 sequential characters.");
    			} else {
    				actiontarget.setText("Master password must contain:\r\n" + 
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
    			actiontarget.setText("Master password must contain:\r\n" + 
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
    			actiontarget.setText("Master password must contain:\r\n" + 
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
    			actiontarget.setText("Master password must contain:\r\n" + 
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
   				actiontarget.setText("Master password meets all criteria.");
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
		int pwstrength = PasswordStrength.check(password.getText());
		if(username.getText().isEmpty()) {
			actiontarget.setText("Username cannot be empty.");
		} else if(password.getText().isEmpty()) {
			actiontarget.setText("Password cannot be empty.");
		} else if(!password.getText().equals(confirmPassword.getText())) { 
			actiontarget.setText("Password fields do not match.");
		} else if(email.getText().isEmpty()||!email.getText().matches(emailRegex)) {
			actiontarget.setText("Email is invalid.");
		} else if(pwstrength != 5) {
			actiontarget.setText("Master password must contain:\r\n" + 
					"	8 characters\r\n" + 
					"	1 special character\r\n" + 
					"	1 uppercase letter\r\n" + 
					"	1 lowercase letter\r\n" + 
					"	1 number");
			return;
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
