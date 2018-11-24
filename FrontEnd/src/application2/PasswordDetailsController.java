package application2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import application2.UserHomeController.DisplayPassword;
import client.ClientSocket;
import client.GuestInfo;
import data.SecurityQuestion;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;

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
        
	@FXML public void initialize() 
    {
    	DisplayPassword passwordPicked = ClientSocket.getViewPassword();
    	ObservableList<PasswordDetails> data =
                FXCollections.observableArrayList();
    	data.add(new PasswordDetails("Application", passwordPicked.getAccountName()));
    	data.add(new PasswordDetails("Username", passwordPicked.getUsername()));
    	data.add(new PasswordDetails("Password", passwordPicked.getPassword()));
        
    	passwordTable.setEditable(true);
    	field.setCellValueFactory(
            new PropertyValueFactory<PasswordDetails,String>("field")
        );

        info.setCellValueFactory(
            new PropertyValueFactory<PasswordDetails,String>("info")
        );

        info.setCellFactory(TextFieldTableCell.forTableColumn());
        info.setOnEditCommit(
            new EventHandler<CellEditEvent<PasswordDetails, String>>() {
                @Override
                public void handle(CellEditEvent<PasswordDetails, String> t) {
                	PasswordDetails row = (PasswordDetails) t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                	String fieldChange = row.getField();
                	String oldVal = row.getInfo();
                	String newVal = t.getNewValue();
                	if(!oldVal.equals(newVal)) {
                		/*
                		 * POP UP TO CONFIRM
                		 */
                		Alert alert = new Alert(AlertType.CONFIRMATION);
                		alert.setTitle("Change Password");
                		String fieldChangeDisplay="";
                		if(fieldChange.equals("Application")) fieldChangeDisplay="application name";
                		else if(fieldChange.equals("Username")) fieldChangeDisplay="username";
                		else if(fieldChange.equals("Password")) fieldChangeDisplay="password";
                		alert.setHeaderText("Confirm change of "+fieldChangeDisplay+".");
                		alert.setContentText("Are you sure you want to change "+fieldChangeDisplay+" from \""+oldVal+"\" to \""+newVal+"\"?");

                		ButtonType buttonYes = new ButtonType("Yes");
                		ButtonType buttonNo = new ButtonType("No", ButtonData.CANCEL_CLOSE);

                		alert.getButtonTypes().setAll(buttonYes, buttonNo);

                		Optional<ButtonType> result = alert.showAndWait();
                		if (result.get() == buttonYes){
                			row.setInfo(newVal);
                    		if(ClientSocket.isGuest()) {
                    			GuestInfo.editPassword(passwordPicked.getPassID(), fieldChange, newVal);
                    		} else {
                    			ClientSocket.editPassword(passwordPicked.getPassID(), fieldChange, newVal);
                    		}
                    		System.out.println("Changed "+fieldChange+" from "+oldVal+" to "+newVal);
                		} else {
                			row.setInfo(oldVal);
                			t.getTableView().getItems().set(t.getTablePosition().getRow(), row);
                		}
                	}
                }
            }
        );
        
        passwordTable.setItems(data);
        
        ObservableList<QuestionDisplay> questionData =
                FXCollections.observableArrayList();
        if(!ClientSocket.isGuest()) {
        	ArrayList<SecurityQuestion> questions = ClientSocket.getQuestions(passwordPicked.getPassID());
        	if(questions != null) {
        		for(SecurityQuestion q : questions) {
        			questionData.add(new QuestionDisplay(q.getQuestionID(), q.getQuestion(), q.getAnswer()));
        		}
        	}
        }
        
        question.setCellValueFactory(
            new PropertyValueFactory<QuestionDisplay,String>("question")
        );

        answer.setCellValueFactory(
            new PropertyValueFactory<QuestionDisplay,String>("answer")
        );
        
        if(!ClientSocket.isGuest()) {
        	questionTable.setRowFactory(tv -> {
            	TableRow<QuestionDisplay> row = new TableRow<>();
            	row.setOnMouseClicked(event -> {
            		if (!row.isEmpty() && event.getButton().equals(MouseButton.PRIMARY) 
            	             && event.getClickCount() == 2) {
            			QuestionDisplay clickedRow = row.getItem();
        	            System.out.println("Clicked on: "+clickedRow.getQuestion());
        	            
        	            /*
                		 * POP UP TO CHANGE VALUES
                		 */
                		
        	            Dialog<Pair<String, String>> dialog = new Dialog<>();
        	            dialog.setTitle("Change Security Question");
        	            dialog.setHeaderText("Change the question and answer associated with the security question or delete it.");
        	            
        	            ButtonType buttonChange = new ButtonType("Change", ButtonData.OK_DONE);
        	            ButtonType buttonDelete = new ButtonType("Delete");
        	            dialog.getDialogPane().getButtonTypes().addAll(buttonChange, buttonDelete, ButtonType.CANCEL);
        	            
        	            GridPane grid = new GridPane();
        	            grid.setHgap(10);
        	            grid.setVgap(10);
        	            grid.setPadding(new Insets(20, 150, 10, 10));

        	            TextField changeQ = new TextField();
        	            changeQ.setText(clickedRow.getQuestion());
        	            TextField changeA = new TextField();
        	            changeA.setText(clickedRow.getAnswer());
        	            
        	            grid.add(new Label("Question:"), 0, 0);
        	            grid.add(changeQ, 1, 0);
        	            grid.add(new Label("Answer:"), 0, 1);
        	            grid.add(changeA, 1, 1);
        	            
        	            Node changeButton = dialog.getDialogPane().lookupButton(buttonChange);
        	            changeButton.setDisable(true);
        	            
        	            changeQ.textProperty().addListener((observable, oldValue, newValue) -> {
        	                changeButton.setDisable(newValue.trim().isEmpty()||changeA.getText().trim().isEmpty());
        	            });
        	            changeA.textProperty().addListener((observable, oldValue, newValue) -> {
        	                changeButton.setDisable(newValue.trim().isEmpty()||changeQ.getText().trim().isEmpty());
        	            });
        	            
        	            dialog.getDialogPane().setContent(grid);
        	            
        	            dialog.setResultConverter(dialogButton -> {
        	                if(dialogButton == buttonChange) {
        	                    return new Pair<>(changeQ.getText(), changeA.getText());
        	                } else if(dialogButton == buttonDelete) {
        	                	ClientSocket.removeQuestion(passwordPicked.getPassID(), clickedRow.getQuestionID());
        	                	questionData.remove(clickedRow);
        	                }
        	                return null;
        	            });
        	            Optional<Pair<String, String>> result = dialog.showAndWait();
        	            result.ifPresent(newSQ -> {
        	            	String newQ = newSQ.getKey();
        	            	String newA = newSQ.getValue();
        	            	if(!clickedRow.getQuestion().equals(newQ) || !clickedRow.getAnswer().equals(newA)) {
        	            		ClientSocket.editQuestion(passwordPicked.getPassID(), clickedRow.getQuestionID(), newQ, newA);
        	            		clickedRow.setQuestion(newQ);
        	            		clickedRow.setAnswer(newA);
        	            	}
        	            });
        	        }
            	});
            	return row;
            });
        }                 
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
        private SimpleStringProperty field;
        private SimpleStringProperty info;

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
             
        public void setInfo(String info) {
        	this.info = new SimpleStringProperty(info);
        }
    }
    
    public static class QuestionDisplay {
    	private int questionID;
    	private SimpleStringProperty question;
    	private SimpleStringProperty answer;
    	QuestionDisplay(int questionID, String question, String answer) {
    		this.question=new SimpleStringProperty(question);
    		this.answer=new SimpleStringProperty(answer);
    	}
    	public int getQuestionID() {
    		return questionID;
    	}
    	public String getQuestion() {
    		return question.get();
    	}
    	public String getAnswer() {
    		return answer.get();
    	}
    	public void setQuestion(String question) {
    		this.question=new SimpleStringProperty(question);
    	}
    	public void setAnswer(String answer) {
    		this.answer=new SimpleStringProperty(answer);
    	}
    }
}
