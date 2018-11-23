package application2;

import java.io.IOException;

import client.ClientSocket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ManageAccountController 
{
	@FXML private Text actiontarget;
	@FXML private TextField newEmail;
	
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	@FXML protected void handleChangeAction(ActionEvent event) 
    {
		ClientInfo.verifyInManage();
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
			// TODO Auto-generated catch block
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
