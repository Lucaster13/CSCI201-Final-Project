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
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PasswordDetailsController 
{
	@FXML private Text actiontarget;
    @FXML private TextField code;
    @FXML private TableView<Password> passwordTable;
    @FXML private TableColumn detail;
    @FXML private TableColumn info;
    
    private final ObservableList<Password> data =
            FXCollections.observableArrayList(
                new Password("Password", "Z"),
                new Password("Security Question #1", "X"),
                new Password("Security Question #2", "W"),
                new Password("Security Question #3", "Y"),
                new Password("Life", "V")
            );
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@FXML public void initialize() 
    {
    	passwordTable.setEditable(true);
        
    	detail = new TableColumn("Title");
        detail.setCellValueFactory(
            new PropertyValueFactory<Password,String>("accountName")
        );

        info = new TableColumn("**Account Name**");
        info.setCellValueFactory(
            new PropertyValueFactory<Password,String>("password")
        );
                           
        passwordTable.setItems(data);
        passwordTable.getColumns().addAll(detail, info);
        System.out.println("done");
    }
    
    @FXML protected void handleEditAction(ActionEvent event) 
    {
        //actiontarget.setText("Sign in button pressed");
    	//System.out.println("Username: " + username.getText());
    	Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("EditPassword.fxml"));
			Scene scene = new Scene(root, 800, 500);
		    
	        primaryStage.setTitle("Edit Password");
	        primaryStage.setScene(scene);
	        primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
    
    @FXML protected void handleDeleteAction(ActionEvent event) 
    {
        //actiontarget.setText("Sign in button pressed");
    	//System.out.println("Username: " + username.getText());
    	Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("DeletePassword.fxml"));
			Scene scene = new Scene(root, 800, 500);
		    
	        primaryStage.setTitle("Delete Password");
	        primaryStage.setScene(scene);
	        primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static class PasswordDetails {
        private final SimpleStringProperty detail;
        private final Hyperlink info;

        PasswordDetails(String detail, String info) {
            this.detail = new SimpleStringProperty(detail);
            this.info = new Hyperlink(info);
            this.info.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) 
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
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}
                }
            });
        }

        public String getDetail() {
            return detail.get();
        }
        
        public Hyperlink getInfo() {
            return info;
        }
    }
}
