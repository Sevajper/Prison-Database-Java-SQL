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

public class AddNModifyPrisonStaff implements Initializable {

	@FXML
	private TextField addStaffID, addName, addAge, addSex, addJob;

	private String id;
	
	@FXML
	private Button addBtn, modifyBtn;

	@SuppressWarnings("unused")
	private boolean maybe;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		addStaffID.setText(this.id);
	}

	public void modifyInformation(String id, String name, int age, String sex, String job) {
		addStaffID.setText(id);
		addName.setText(name);
		addAge.setText(Integer.toString(age));
		addSex.setText(sex);
		addJob.setText(job);
	}

	public void checkModify(boolean maybe) {
		this.maybe = maybe;
		if (maybe) {
			addStaffID.setDisable(true);
		}
	}

	@FXML
	private void addToPrisonStaffTable(ActionEvent event) {

		try {
			Connection connect = ConnectToDatabase.getConnection();
			PreparedStatement state = connect
					.prepareStatement("INSERT INTO PRISON_STAFF(staff_id, name, age, sex, job) VALUES (?,?,?,?,?)");

			if 	(this.addStaffID.getText() == null || this.addStaffID.getText().isEmpty() || this.addName.getText().isEmpty()
					|| this.addAge.getText().isEmpty() || this.addSex.getText().isEmpty()
					|| this.addJob.getText().isEmpty()) {
				Alert errorAlert = new Alert(AlertType.ERROR);
				errorAlert.setHeaderText("Input not valid");
				errorAlert.setContentText("Fields cannot be empty.");
				errorAlert.showAndWait();
				return;

			} else {
				@SuppressWarnings("unused")
				int age = Integer.parseInt(this.addAge.getText());
				state.setString(1, this.addStaffID.getText());
				state.setString(2, this.addName.getText());
				state.setInt(3, Integer.valueOf(this.addAge.getText()));
				state.setString(4, this.addSex.getText());
				state.setString(5, this.addJob.getText());

				state.execute();
				connect.close();
			}
		} catch (SQLiteException sq) {
			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setHeaderText("Input not valid");
			errorAlert.setContentText("That staffID already exists.");
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
	private void modifyPrisonStaffTable(ActionEvent event) {
		try {
			addStaffID.setDisable(true);
			Connection connect = ConnectToDatabase.getConnection();
			String updateInfo = "UPDATE PRISON_STAFF SET staff_id=?, name=?, age=?, sex=?, job=? WHERE staff_id='"
					+ this.addStaffID.getText() + "'";
			PreparedStatement state = connect.prepareStatement(updateInfo);

			if (this.addStaffID.getText().isEmpty() || this.addName.getText().isEmpty()
					|| this.addAge.getText().isEmpty() || this.addSex.getText().isEmpty()
					|| this.addJob.getText().isEmpty()) {
				Alert errorAlert = new Alert(AlertType.ERROR);
				errorAlert.setHeaderText("Input not valid");
				errorAlert.setContentText("Fields cannot be empty.");
				errorAlert.showAndWait();
				return;

			} else {
				@SuppressWarnings("unused")
				int age = Integer.parseInt(this.addAge.getText());
				state.setString(1, this.addStaffID.getText());
				state.setString(2, this.addName.getText());
				state.setInt(3, Integer.valueOf(this.addAge.getText()));
				state.setString(4, this.addSex.getText());
				state.setString(5, this.addJob.getText());

				state.execute();
				connect.close();
			}
		} catch (SQLiteException sq) {
			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setHeaderText("Input not valid");
			errorAlert.setContentText("That staffID already exists.");
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
