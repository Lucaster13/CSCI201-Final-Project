package application2;
 
import java.io.IOException;

import application2.UserHomeController.Password;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
 
public class GuestHomeController 
{
    @FXML private Text actiontarget;
    
    @FXML private TableView<Password> passwordTable;
    @FXML private TableColumn accountName;
    @FXML private TableColumn password;
    
    
    //Make password hyperlink by changing Password class itself
    //Password class is in UserHomeController
    private final ObservableList<Password> data =
        FXCollections.observableArrayList(
            new Password("Hello", "Z"),
            new Password("Sup", "X"),
            new Password("Hi", "W"),
            new Password("Bye", "Y"),
            new Password("See you", "V")
        );
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@FXML public void initialize() 
    {
    	passwordTable.setEditable(true);
        
    	accountName = new TableColumn("Account Name");
        accountName.setCellValueFactory(
            new PropertyValueFactory<Password,String>("accountName")
        );

        password = new TableColumn("Password");
        password.setCellValueFactory(
            new PropertyValueFactory<Password,String>("password")
        );
                           
        passwordTable.setItems(data);
        passwordTable.getColumns().addAll(accountName, password);
        System.out.println("done");
    }
    
    @FXML protected void handleNewPasswordAction(ActionEvent event) 
    {
    	//actiontarget.setText("Sign in button pressed");
    	Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("NewPassword.fxml"));
			Scene scene = new Scene(root, 800, 500);
		    
	        primaryStage.setTitle("New Password");
	        primaryStage.setScene(scene);
	        primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    @FXML protected void handleNewAccountAction(ActionEvent event) 
    {
        //actiontarget.setText("Sign in button pressed");
    	Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("NewAccount.fxml"));
			Scene scene = new Scene(root, 800, 500);
		    
	        primaryStage.setTitle("Create New Account");
	        primaryStage.setScene(scene);
	        primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @FXML protected void handleEndSessionAction(ActionEvent event) 
    {
        //actiontarget.setText("Sign in button pressed");
    	Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
			Scene scene = new Scene(root, 800, 500);
		    
	        primaryStage.setTitle("Login Page");
	        primaryStage.setScene(scene);
	        primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}