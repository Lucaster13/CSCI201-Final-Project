package application2;
 
import java.io.IOException;

import application2.UserHomeController.Password;
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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
 
public class UserHomeController 
{
    @FXML private Text actiontarget;
    @FXML private TextField username;
    @FXML private TableView<Password> passwordTable;
    @FXML private TableColumn<Password,String> accountName;
    @FXML private TableColumn<Password,String> password;
    
    //Make password hyperlink by changing Password class itself
    private final ObservableList<Password> data =
        FXCollections.observableArrayList(
            new Password("Hello", "Z"),
            new Password("Sup", "X"),
            new Password("Hi", "W"),
            new Password("Bye", "Y"),
            new Password("See you", "V"),
            new Password("Bye", "Y")
        );
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@FXML public void initialize() 
    {
    	accountName.setCellValueFactory(
            new PropertyValueFactory<Password,String>("accountName")
        );
        password.setCellValueFactory(
            new PropertyValueFactory<Password,String>("displayPassword")
        );
        
        passwordTable.setItems(data);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static class Password {
        private final SimpleStringProperty accountName;
        private Hyperlink displayPassword=new Hyperlink("•••••••");
        private boolean hidden=true;
        private final String password;

        Password(String accountName, String password) {
            this.accountName = new SimpleStringProperty(accountName);
            this.password = password;
            this.displayPassword.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) 
                {
                	 if(event.getButton().equals(MouseButton.PRIMARY)){
                         if(event.getClickCount() == 1){
                        	 if(hidden) {
                        		 displayPassword.setText(password);
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
        
        public String getPassword() {
            return password;
        }
        
        public Hyperlink getDisplayPassword() {
        	return displayPassword;
        }              
    }
}