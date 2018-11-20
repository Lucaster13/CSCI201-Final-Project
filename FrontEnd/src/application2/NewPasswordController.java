package application2;

import java.io.IOException;

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

public class NewPasswordController 
{
	@FXML private Text actiontarget;
    @FXML private TextField title;
    @FXML private TextField password;
    @FXML private TextField confirmPassword;
    @FXML private Label strength;
	
    @FXML public void initialize() 
    {
    	password.textProperty().addListener((observable, oldValue, newValue) -> {
    		//Change this part so that it corresponds to the real password strengths
    		if(password.getLength() < 3)
    		{
    			strength.setText("Poor");
    			strength.setTextFill(Color.RED);
    		}
    		else if(password.getLength() < 6)
    		{
    			strength.setText("Fine");
    			strength.setTextFill(Color.YELLOW);
    		}
    		else
    		{
    			strength.setText("Great");
    			strength.setTextFill(Color.GREEN);
    		}
    	});
    }
    
	@FXML protected void handleBackAction(ActionEvent event) 
    {
		if(ClientInfo.getGuestStatus() == true)
        {
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
	
	@FXML protected void handleCreateAction(ActionEvent event) 
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
	
	@FXML protected void handleAddQuestionAction(ActionEvent event) 
    {
        if(ClientInfo.getGuestStatus() == true)
        {
        	actiontarget.setText("Make account to access feature");
        }
        else 
        {
	    	Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
	    	Parent root;
			try {
				root = FXMLLoader.load(getClass().getResource("AddQuestion.fxml"));
				Scene scene = new Scene(root, 800, 500);
			    
		        primaryStage.setTitle("Add Question");
		        primaryStage.setScene(scene);
		        primaryStage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
	
	@FXML protected void handleViewQuestionsAction(ActionEvent event) 
    {
		if(ClientInfo.getGuestStatus() == true)
        {
        	actiontarget.setText("Make account to access feature");
        }
        else 
        {
	    	Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
	    	Parent root;
			try {
				root = FXMLLoader.load(getClass().getResource("ViewQuestions.fxml"));
				Scene scene = new Scene(root, 800, 500);
			    
		        primaryStage.setTitle("View Questions");
		        primaryStage.setScene(scene);
		        primaryStage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
}
