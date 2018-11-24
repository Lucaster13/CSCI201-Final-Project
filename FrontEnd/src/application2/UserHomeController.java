package application2;
 
import java.io.IOException;
import java.util.ArrayList;

import client.ClientSocket;
import data.Password;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
 
public class UserHomeController 
{
    @FXML private TextField username;
    @FXML private TableView<DisplayPassword> passwordTable;
    @FXML private TableColumn<DisplayPassword,String> accountName;
    @FXML private TableColumn<DisplayPassword,String> password;
    
	@FXML public void initialize() 
    {
		ObservableList<DisplayPassword> data =
		        FXCollections.observableArrayList();
		ArrayList<Password> dbPasswords = ClientSocket.getPasswords();
		for(Password pass : dbPasswords) {
			data.add(new DisplayPassword(pass.getID(), pass.getName(), pass.getUsername(), pass.getPass()));
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
    	Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	Parent root;
		try {
			ClientSocket.setLastPage("UserHome");
			root = FXMLLoader.load(getClass().getResource("NewPassword.fxml"));
			Scene scene = new Scene(root, 800, 500);
		    
	        primaryStage.setTitle("New Password");
	        primaryStage.setScene(scene);
	        primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    
    @FXML protected void handleManageAccountAction(ActionEvent event) 
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
    
    @FXML protected void handleEndSessionAction(ActionEvent event) 
    {
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
    
    @FXML protected void fire(ActionEvent event)
    {
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
    
    public static class DisplayPassword {
    	private int passID;
    	private String username;
        private SimpleStringProperty accountName;
        private Hyperlink displayPassword=new Hyperlink("•••••••");
        private boolean hidden=true;
        private String password;

        public DisplayPassword(int passID, String accountName, String username, String password) {
            this.passID=passID;
            this.username=username;
        	this.accountName = new SimpleStringProperty(accountName);
            this.password = password;
            this.displayPassword.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) 
                {
                	 if(event.getButton().equals(MouseButton.PRIMARY)){
                         if(event.getClickCount() == 1){
                        	 if(hidden) {
                        		 displayPassword.setText(getPassword());
                        		 hidden=false;
                        	 } else {
                        		 displayPassword.setText("•••••••");
                        		 hidden=true;
                        	 }
                         }
                     }
                	event.consume();
                }
            });
        }

        public String getAccountName() {
            return accountName.get();
        }
        
        public String getUsername() {
        	return username;
        }
        
        public String getPassword() {
            return password;
        }
        
        public Hyperlink getDisplayPassword() {
        	return displayPassword;
        }
        
        public int getPassID() {
        	return passID;
        }

		public void setPassID(int passID) {
			this.passID=passID;
		}
		
		public void setPassword(String password) {
			this.password = password;
		}
		
		public void setAppname(String appName) {
			this.accountName = new SimpleStringProperty(appName);
		}
		
		public void setUsername(String username) {
			this.username = username;
		}
		
		public DisplayPassword hide() {
			displayPassword.setText("•••••••");
   		 	hidden=true;
   		 	return this;
		}
    }
}