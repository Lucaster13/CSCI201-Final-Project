package application2;
 
import java.io.IOException;
import java.util.ArrayList;

import application2.UserHomeController.DisplayPassword;
import client.ClientSocket;
import client.GuestInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import javafx.stage.Stage;
 
public class GuestHomeController 
{
    @FXML private TableView<DisplayPassword> passwordTable;
    @FXML private TableColumn<DisplayPassword,String> accountName;
    @FXML private TableColumn<DisplayPassword,String> password;
    
	@FXML public void initialize() 
    {
		ObservableList<DisplayPassword> data =
		        FXCollections.observableArrayList();
		ArrayList<DisplayPassword> storedPass = GuestInfo.getPasswords();
		for(DisplayPassword pass : storedPass) {
			data.add(pass);
		}
		accountName.setCellValueFactory(
            new PropertyValueFactory<DisplayPassword,String>("accountName")
        );
        password.setCellValueFactory(
            new PropertyValueFactory<DisplayPassword,String>("displayPassword")
        );
        passwordTable.setRowFactory(tv -> {
        	TableRow<DisplayPassword> row = new TableRow<>();
        	row.setOnMouseClicked(event -> {
        		if (!row.isEmpty() && event.getButton().equals(MouseButton.PRIMARY) 
        	             && event.getClickCount() == 2) {
        			DisplayPassword clickedRow = row.getItem();
    	            System.out.println("Clicked on: "+clickedRow.getAccountName());
    	            Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
                	Parent root;
            		try {
            			ClientSocket.setViewPassword(clickedRow);
            			ClientSocket.setLastPage("GuestHome");
            			root = FXMLLoader.load(getClass().getResource("PasswordDetails.fxml"));
            			Scene scene = new Scene(root, 800, 500);
            	        primaryStage.setTitle("Password Details");
            	        primaryStage.setScene(scene);
            	        primaryStage.show();
            		} catch (IOException e) {
            			e.printStackTrace();
            		}
    	        }
        	});
        	return row;
        });
        passwordTable.setItems(data);
    }
    
    @FXML protected void handleNewPasswordAction(ActionEvent event) 
    {
    	//actiontarget.setText("Sign in button pressed");
    	Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	Parent root;
		try {
			ClientSocket.setLastPage("GuestHome");
			root = FXMLLoader.load(getClass().getResource("NewPassword.fxml"));
			Scene scene = new Scene(root, 800, 500);
		    
	        primaryStage.setTitle("New Password");
	        primaryStage.setScene(scene);
	        primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    
    @FXML protected void handleNewAccountAction(ActionEvent event) 
    {
        //actiontarget.setText("Sign in button pressed");
    	Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	Parent root;
		try {
			ClientSocket.setLastPage("GuestHome");
			root = FXMLLoader.load(getClass().getResource("NewAccount.fxml"));
			Scene scene = new Scene(root, 800, 500);
		    
	        primaryStage.setTitle("Create New Account");
	        primaryStage.setScene(scene);
	        primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    @FXML protected void handleEndSessionAction(ActionEvent event) 
    {
        //actiontarget.setText("Sign in button pressed");
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