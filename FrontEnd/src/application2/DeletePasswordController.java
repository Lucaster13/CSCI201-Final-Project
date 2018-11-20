package application2;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DeletePasswordController 
{
	@FXML protected void handleBackAction(ActionEvent event) 
    {
        //actiontarget.setText("Sign in button pressed");
    	//System.out.println("Username: " + username.getText());
    	Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("PasswordDetails.fxml"));
			Scene scene = new Scene(root, 800, 500);
		    
	        primaryStage.setTitle("Password Details");
	        primaryStage.setScene(scene);
	        primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @FXML protected void handleDeleteAction(ActionEvent event) 
    {
    	if(ClientInfo.getGuestStatus() == true)
    	{
    		//actiontarget.setText("Sign in button pressed");
        	//System.out.println("Username: " + username.getText());
        	Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        	Parent root;
    		try {
    			root = FXMLLoader.load(getClass().getResource("GuestHome.fxml"));
    			Scene scene = new Scene(root, 800, 500);
    		    
    	        primaryStage.setTitle("Guest Home");
    	        primaryStage.setScene(scene);
    	        primaryStage.show();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    	else 
    	{
	        //actiontarget.setText("Sign in button pressed");
	    	//System.out.println("Username: " + username.getText());
	    	Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
	    	Parent root;
			try {
				root = FXMLLoader.load(getClass().getResource("UserHome.fxml"));
				Scene scene = new Scene(root, 800, 500);
			    
		        primaryStage.setTitle("User Home");
		        primaryStage.setScene(scene);
		        primaryStage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
}
