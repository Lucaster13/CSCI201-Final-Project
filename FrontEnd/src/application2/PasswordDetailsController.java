package application2;

import java.io.IOException;
import java.util.ArrayList;

import application2.UserHomeController.DisplayPassword;
import client.ClientSocket;
import data.SecurityQuestion;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PasswordDetailsController 
{
	@FXML private Text actiontarget;
    @FXML private TextField code;
    @FXML private TableView<PasswordDetails> passwordTable;
    @FXML private TableColumn<PasswordDetails,String> field;
    @FXML private TableColumn<PasswordDetails,String> info;
    @FXML private TableView<QuestionDisplay> questionTable;
    @FXML private TableColumn<QuestionDisplay,String> question;
    @FXML private TableColumn<QuestionDisplay,String> answer;
    
//            	new PasswordDetails("Username", "text"),
//            	new PasswordDetails("Password", "Z"),
//                new PasswordDetails("Security Question #1", "X"),
//                new PasswordDetails("Security Question #2", "W"),
//                new PasswordDetails("Security Question #3", "Y")
    
	@FXML public void initialize() 
    {
    	DisplayPassword passwordPicked = ClientSocket.getViewPassword();
    	ObservableList<PasswordDetails> data =
                FXCollections.observableArrayList();
    	data.add(new PasswordDetails("Application", passwordPicked.getAccountName()));
    	data.add(new PasswordDetails("Username", passwordPicked.getUsername()));
    	data.add(new PasswordDetails("Password", passwordPicked.getPassword()));
        field.setCellValueFactory(
            new PropertyValueFactory<PasswordDetails,String>("field")
        );

        info.setCellValueFactory(
            new PropertyValueFactory<PasswordDetails,String>("info")
        );
                           
        passwordTable.setItems(data);
        
        ObservableList<QuestionDisplay> questionData =
                FXCollections.observableArrayList();
        if(!ClientSocket.isGuest()) {
        	ArrayList<SecurityQuestion> questions = ClientSocket.getQuestions(passwordPicked.getPassID());
        	if(questions != null) {
        		for(SecurityQuestion q : questions) {
        			questionData.add(new QuestionDisplay(q.getQuestion(), q.getAnswer()));
        		}
        	}
        }
        
        question.setCellValueFactory(
            new PropertyValueFactory<QuestionDisplay,String>("question")
        );

        answer.setCellValueFactory(
            new PropertyValueFactory<QuestionDisplay,String>("answer")
        );
                           
        questionTable.setItems(questionData);
    }
    
	@FXML protected void handleAddQuestionAction(ActionEvent event) 
    {
        if(ClientSocket.isGuest())
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
				e.printStackTrace();
			}
        }
    }
    
    @FXML protected void handleBackAction(ActionEvent event) 
    {
    	if(ClientSocket.isGuest())
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
			e.printStackTrace();
		}
    }
    
    public static class PasswordDetails {
        private final SimpleStringProperty field;
        private final SimpleStringProperty info;

        PasswordDetails(String field, String info) {
            this.field = new SimpleStringProperty(field);
            this.info = new SimpleStringProperty(info);
        }

        public String getField() {
            return field.get();
        }
        
        public String getInfo() {
            return info.get();
        }
    }
    
    public static class QuestionDisplay {
    	private SimpleStringProperty question;
    	private SimpleStringProperty answer;
    	QuestionDisplay(String question, String answer) {
    		this.question=new SimpleStringProperty(question);
    		this.answer=new SimpleStringProperty(answer);
    	}
    	public String getQuestion() {
    		return question.get();
    	}
    	public String getAnswer() {
    		return answer.get();
    	}
    }
}
