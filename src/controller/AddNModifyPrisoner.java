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

public class AddNModifyPrisoner implements Initializable {

	@FXML
	private TextField addPrisonerID, addName, addAge, addSex, addOffence, addSentence;

	private String id;
	@FXML
	private Button addBtn, modifyBtn;

	@SuppressWarnings("unused")
	private boolean maybe;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		addPrisonerID.setText(this.id);
	}

	public void modifyInformation(String id, String name, int age, String sex, String offence, String sentence) {
		addPrisonerID.setText(id);
		addName.setText(name);
		addAge.setText(Integer.toString(age));
		addSex.setText(sex);
		addOffence.setText(offence);
		addSentence.setText(sentence);
	}

	public void checkModify(boolean maybe) {
		this.maybe = maybe;
		if (maybe) {
			addPrisonerID.setDisable(true);
		}
	}

	@FXML
	private void addToPrisonersTable(ActionEvent event) {

		try {
			Connection connect = ConnectToDatabase.getConnection();
			PreparedStatement state = connect.prepareStatement(
					"INSERT INTO PRISONERS(prisoner_id, name, age, sex, offence, sentence) VALUES (?,?,?,?,?,?)");

			if (this.addPrisonerID.getText() == null ||this.addPrisonerID.getText().isEmpty() || this.addName.getText().isEmpty()
					|| this.addAge.getText().isEmpty() || this.addSex.getText().isEmpty()) {
				Alert errorAlert = new Alert(AlertType.ERROR);
				errorAlert.setHeaderText("Input not valid");
				errorAlert.setContentText("Fields cannot be empty.");
				errorAlert.showAndWait();
				return;
			} else {
				@SuppressWarnings("unused")
				int age = Integer.parseInt(this.addAge.getText());
				state.setString(1, this.addPrisonerID.getText());
				state.setString(2, this.addName.getText());
				state.setInt(3, Integer.valueOf(this.addAge.getText()));
				state.setString(4, this.addSex.getText());
				state.setString(5, this.addOffence.getText());
				state.setString(6, this.addSentence.getText());

				state.execute();
				connect.close();
			}
		} catch (SQLiteException sq) {
			System.out.println(sq);
			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setHeaderText("Input not valid");
			errorAlert.setContentText("That prisonerID already exists.");
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
	private void modifyPrisonersTable(ActionEvent event) {
		try {
			addPrisonerID.setDisable(true);
			Connection connect = ConnectToDatabase.getConnection();
			String updateInfo = "UPDATE PRISONERS SET prisoner_id=?, name=?, age=?, sex=?, offence=?, sentence=? WHERE prisoner_id='"
					+ this.addPrisonerID.getText() + "'";
			PreparedStatement state = connect.prepareStatement(updateInfo);

			if (this.addPrisonerID.getText().isEmpty() || this.addName.getText().isEmpty()
					|| this.addAge.getText().isEmpty() || this.addSex.getText().isEmpty()) {
				Alert errorAlert = new Alert(AlertType.ERROR);
				errorAlert.setHeaderText("Input not valid");
				errorAlert.setContentText("Fields cannot be empty.");
				errorAlert.showAndWait();
				return;
			} else {
				@SuppressWarnings("unused")
				int age = Integer.parseInt(this.addAge.getText());
				state.setString(1, this.addPrisonerID.getText());
				state.setString(2, this.addName.getText());
				state.setInt(3, Integer.valueOf(this.addAge.getText()));
				state.setString(4, this.addSex.getText());
				state.setString(5, this.addOffence.getText());
				state.setString(6, this.addSentence.getText());

				state.execute();
				connect.close();
			}
		} catch (SQLiteException sq) {
			System.out.println(sq);
			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setHeaderText("Input not valid");
			errorAlert.setContentText("That prisonerID already exists.");
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
