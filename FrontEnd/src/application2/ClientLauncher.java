package application2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ClientLauncher extends Application
{
	public static void main(String[] args) {
        launch(args);
    }
	
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
    
        Scene scene = new Scene(root, 800, 500);
    
        stage.setTitle("Login Page");
        stage.setScene(scene);
        stage.getIcons().add(new Image(this.getClass().getResource("trojanicon.jpg").toString()));
        stage.show();
    }
}