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
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
 
public class LoginPageController 
{
    @FXML private Text actiontarget;
    @FXML private TextField username;
    @FXML private TextField password;
    
    @FXML public void initialize() 
    {
    	password.textProperty().addListener((observable, oldValue, newValue) -> {
    		/*
    		 * TODO: ADD PASSWORD STRENGTH
    		 */
    	    System.out.println("textfield changed from " + oldValue + " to " + newValue);
    	});
    }
    
    @FXML protected void handleSignInAction(ActionEvent event) 
    {
    	if(username.getText().isEmpty() || !password.getText().isEmpty()) return;
    	
    	int loginSuccess = ClientSocket.login(username.getText(), password.getText());
    	//ClientInfo c = new ClientInfo(username.getText(), false);
    	
    	if(loginSuccess==LoginResponse.TYPE_SUCCESS) {
    		Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        	Parent root;
    		try {
    			ClientSocket.setLastPage("LoginPage");
    			root = FXMLLoader.load(getClass().getResource("EmailVerify.fxml"));
    			Scene scene = new Scene(root, 800, 500);
    		    
    	        primaryStage.setTitle("Verify Email");
    	        primaryStage.setScene(scene);
    	        primaryStage.show();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	} else if(loginSuccess==LoginResponse.TYPE_INVALID) { // TODO: DISPLAY INVALID CREDENTIALS MESSAGE
    		
    	} else { // TODO: DISPLAY SERVER ERROR
    		
    	}
    }
    
    
    @FXML protected void handleNewAccountAction(ActionEvent event) 
    {
        //actiontarget.setText("Sign in button pressed");
    	Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	Parent root;
		try {
			ClientSocket.setLastPage("LoginPage");
			root = FXMLLoader.load(getClass().getResource("NewAccount.fxml"));
			Scene scene = new Scene(root, 800, 500);
		    
	        primaryStage.setTitle("Create New Account");
	        primaryStage.setScene(scene);
	        primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    @FXML protected void handleGuestSignInAction(ActionEvent event) 
    {
    	ClientInfo c = new ClientInfo(true);
    	
    	Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	Parent root;
		try {
			ClientSocket.setLastPage("LoginPage");
			root = FXMLLoader.load(getClass().getResource("GuestHome.fxml"));
			Scene scene = new Scene(root, 800, 500);
		    
	        primaryStage.setTitle("Guest Home");
	        primaryStage.setScene(scene);
	        primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}