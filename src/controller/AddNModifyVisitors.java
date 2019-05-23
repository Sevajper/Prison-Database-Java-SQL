package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

import org.sqlite.SQLiteException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.ConnectToDatabase;

public class AddNModifyVisitors implements Initializable {

	@FXML
	private TextField addVisitorID, addName, addAge, addVisitedPrisoner;

	private String id;

	@FXML
	private Button addBtn, modifyBtn;

	@SuppressWarnings("unused")
	private boolean maybe;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		addVisitorID.setText(this.id);
	}

	public void modifyInformation(String id, String name, int age, String visitedPrisoner) {
		addVisitorID.setText(id);
		addName.setText(name);
		addAge.setText(Integer.toString(age));
		addVisitedPrisoner.setText(visitedPrisoner);
	}

	public void checkModify(boolean maybe) {
		this.maybe = maybe;
		if (maybe) {
			addVisitorID.setDisable(true);
		}
	}

	@FXML
	private void addToVisitorsTable(ActionEvent event) {

		try {
			Connection connect = ConnectToDatabase.getConnection();
			PreparedStatement state = connect
					.prepareStatement("INSERT INTO VISITORS(visitor_id, name, age, visitedPrisoner) VALUES (?,?,?,?)");

			if (this.addVisitorID.getText() == null || this.addVisitorID.getText().isEmpty()
					|| this.addName.getText().isEmpty() || this.addAge.getText().isEmpty()) {
				Alert errorAlert = new Alert(AlertType.ERROR);
				errorAlert.setHeaderText("Input not valid");
				errorAlert.setContentText("Fields cannot be empty.");
				errorAlert.showAndWait();
				return;

			} else {
				@SuppressWarnings("unused")
				int age = Integer.parseInt(this.addAge.getText());
				state.setString(1, this.addVisitorID.getText());
				state.setString(2, this.addName.getText());
				state.setInt(3, Integer.valueOf(this.addAge.getText()));
				state.setString(4, this.addVisitedPrisoner.getText());

				state.execute();
				connect.close();
			}
		} catch (SQLiteException sq) {
			System.out.println(sq);
			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setHeaderText("Input not valid");
			errorAlert.setContentText("That visitorID already exists.");
			errorAlert.showAndWait();
			return;

		} catch (NumberFormatException nfe) {
			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setHeaderText("Input not valid");
			errorAlert.setContentText("Age must be a number.");
			errorAlert.showAndWait();
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		addBtn.getScene().getWindow().hide();

	}

	@FXML
	private void modifyVisitorsTable(ActionEvent event) {
		try {
			addVisitorID.setDisable(true);
			Connection connect = ConnectToDatabase.getConnection();
			String updateInfo = "UPDATE VISITORS SET visitor_id=?, name=?, age=?, visitedPrisoner=? WHERE visitor_id='"
					+ this.addVisitorID.getText() + "'";
			PreparedStatement state = connect.prepareStatement(updateInfo);

			if (this.addVisitorID.getText().isEmpty() || this.addName.getText().isEmpty()
					|| this.addAge.getText().isEmpty()) {
				Alert errorAlert = new Alert(AlertType.ERROR);
				errorAlert.setHeaderText("Input not valid");
				errorAlert.setContentText("Fields cannot be empty.");
				errorAlert.showAndWait();
				return;

			} else {
				@SuppressWarnings("unused")
				int age = Integer.parseInt(this.addAge.getText());
				state.setString(1, this.addVisitorID.getText());
				state.setString(2, this.addName.getText());
				state.setInt(3, Integer.valueOf(this.addAge.getText()));
				state.setString(4, this.addVisitedPrisoner.getText());

				state.execute();
				connect.close();
			}
		} catch (SQLiteException sq) {
			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setHeaderText("Input not valid");
			errorAlert.setContentText("That visitorID already exists.");
			errorAlert.showAndWait();
			return;

		} catch (NumberFormatException nfe) {
			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setHeaderText("Input not valid");
			errorAlert.setContentText("Age must be a number.");
			errorAlert.showAndWait();
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		modifyBtn.getScene().getWindow().hide();
	}

}
