package application2;

import java.io.IOException;

import client.ClientSocket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DeleteAccountController 
{
	@FXML protected void handleBackAction(ActionEvent event) 
    {
    	Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("ManageAccount.fxml"));
			Scene scene = new Scene(root, 800, 500);
		    
	        primaryStage.setTitle("Manage Account");
	        primaryStage.setScene(scene);
	        primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    @FXML protected void handleDeleteAction(ActionEvent event) 
    {
    	ClientSocket.deleteAccount();
    	ClientSocket.logout();
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
