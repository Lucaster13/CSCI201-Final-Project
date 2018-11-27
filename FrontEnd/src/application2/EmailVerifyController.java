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
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
 
public class EmailVerifyController 
{
    @FXML private Text actiontarget;
    @FXML private TextField code;
    @FXML private Text sending;
    @FXML private Button submitButton;
    
    @FXML public void initialize() 
    {
    	submitButton.setDefaultButton(true);
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
    	String inputCode = code.getText();
    	String regex = "\\d{6}";
    	if(inputCode.matches(regex)) { //Valid 6 digit number
    		boolean validCode = ClientSocket.verifyCode(Integer.valueOf(inputCode));
    		if(validCode) {
    			if(ClientSocket.getLastPage().equals("ManageAccount")) { // Change password
    				ClientSocket.changeMasterPass();
    			}    			
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
    		} else { 
    			actiontarget.setText("Invalid Code.");
    		}
    	} else { 
    		actiontarget.setText("Code is a 6-digit number.");
    	}
    }
}