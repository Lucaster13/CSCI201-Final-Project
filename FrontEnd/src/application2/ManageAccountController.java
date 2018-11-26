package application2;

import java.io.IOException;

import client.ClientSocket;
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

public class ManageAccountController 
{
	@FXML private Text actiontarget;
	@FXML private TextField password;
	@FXML private TextField confirmPassword;
	@FXML private Label strength;
	
    @FXML public void initialize() 
    {
    	password.textProperty().addListener((observable, oldValue, newValue) -> {
    		//Change this part so that it corresponds to the real password strengths
    		if(password.getLength() < 2)
    		{
    			strength.setText("Very Weak");
    			strength.setTextFill(Color.RED);
    		}
    		else if(password.getLength() < 4)
    		{
    			strength.setText("Mediocre");
    			strength.setTextFill(Color.ORANGE);
    		}
    		else if(password.getLength() < 6)
    		{
    			strength.setText("Fine");
    			strength.setTextFill(Color.YELLOW);
    		}
    		else if(password.getLength() < 8)
    		{
    			strength.setText("Strong");
    			strength.setTextFill(Color.LIGHTGREEN);
    		}
    		else
    		{
    			strength.setText("Very Strong");
    			strength.setTextFill(Color.DARKGREEN);
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
		if(password.getText().isEmpty() || !password.getText().equals(confirmPassword.getText())) {
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
