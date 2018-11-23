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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
 
public class EmailVerifyController 
{
    @FXML private Text actiontarget;
    @FXML private TextField code;
    @FXML private Text sending;
    
    @FXML public void initialize() 
    {
    	//Replace the below email with actual client's email
    	String email = ClientSocket.getEmail();
    	String displayEmail="";
    	boolean foundAt = false;
    	for(int i=0; i<email.length(); i++) {
    		if(email.charAt(i)=='@') foundAt = true;
    		if(foundAt) displayEmail += email.charAt(i);
    		else {
    			if(i<3) {
    				displayEmail += email.charAt(i);
    			} else {
    				displayEmail += '*';
    			}
    		}
    	}
    	sending.setText("We're sending a code to: " + displayEmail);
    }
    
    @FXML protected void handleVerifyAction(ActionEvent event) 
    {
        //actiontarget.setText("Sign in button pressed");
    	//System.out.println("Username: " + username.getText());
    	String inputCode = code.getText();
    	String regex = "\\d{6}";
    	if(inputCode.matches(regex)) { //Valid 6 digit number
    		int validCode = ClientSocket.verifyCode(Integer.valueOf(inputCode));
    		if(validCode == LoginResponse.TYPE_SUCCESS) {
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
    		} else { //TODO: DISPLAY MESSAGE OF INVALID CODE
    			
    		}
    	} else { //TODO: DISPLAY MESSAGE THAT CODE IS IN AN INVALID FORMAT
    		
    	}
    }
}