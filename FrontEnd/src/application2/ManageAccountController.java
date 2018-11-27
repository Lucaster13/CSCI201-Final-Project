package application2;

import java.io.IOException;

import client.ClientSocket;
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

public class ManageAccountController 
{
	@FXML private Text actiontarget;
	@FXML private TextField password;
	@FXML private TextField confirmPassword;
	@FXML private Label strength;
	@FXML private Button changeButton;
	
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
	
	@FXML protected void handleChangeAction(ActionEvent event) 
    {
		int pwstrength = PasswordStrength.check(password.getText());
		if(pwstrength != 5) {
			actiontarget.setText("Master password must contain:\r\n" + 
					"	8 characters\r\n" + 
					"	1 special character\r\n" + 
					"	1 uppercase letter\r\n" + 
					"	1 lowercase letter\r\n" + 
					"	1 number");
			return;
		} else if(!password.getText().equals(confirmPassword.getText())) {
			actiontarget.setText("Password fields do not match.");
			return;
		}
		ClientSocket.setNewPass(password.getText());
    	Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	Parent root;
		try {
			ClientSocket.setLastPage("ManageAccount");
			root = FXMLLoader.load(getClass().getResource("EmailVerify.fxml"));
			Scene scene = new Scene(root, 800, 500);
		    
	        primaryStage.setTitle("Email Verify");
	        primaryStage.setScene(scene);
	        primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	@FXML protected void handleDeleteAction(ActionEvent event) 
    {
    	Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("DeleteAccount.fxml"));
			Scene scene = new Scene(root, 800, 500);
		    
	        primaryStage.setTitle("Delete Account");
	        primaryStage.setScene(scene);
	        primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
