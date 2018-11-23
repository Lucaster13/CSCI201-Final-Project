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
import javafx.stage.Stage;

public class DeletePasswordController 
{
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
    
    @FXML protected void handleDeleteAction(ActionEvent event) 
    {
    	DisplayPassword passwordPicked = ClientSocket.getViewPassword();
    	if(ClientSocket.isGuest())
    	{
    		GuestInfo.removePassword(passwordPicked.getPassID());
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
    		ClientSocket.removePassword(passwordPicked.getPassID());
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
}
