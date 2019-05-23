package model;

import java.sql.SQLException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) throws SQLException {
		CreateDatabaseTable cdt = new CreateDatabaseTable(ConnectToDatabase.getConnection());
		cdt.createTables();
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(Main.class.getResource("/view/mainWindow.fxml"));
		Scene scene = new Scene(root);
        primaryStage.setTitle("Prison Database");
		primaryStage.setScene(scene);
		primaryStage.show();
		// Image applicationIcon = new Image(getClass().getResourceAsStream("/view/icon.png"));
        //primaryStage.getIcons().add(applicationIcon);
	}
	

}
