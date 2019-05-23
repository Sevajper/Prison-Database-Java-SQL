package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.sqlite.SQLiteException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.ConnectToDatabase;

public class AddNModifyOffences implements Initializable {

	@FXML
	private TextField addType, addMin, addMax;

	private String type;

	@SuppressWarnings("unused")
	private boolean maybe;

	@FXML
	private Button addBtn, modifyBtn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		addType.setText(this.type);
	}

	public void modifyInformation(String type, String min, String max) {
		addType.setText(type);
		addMin.setText(min);
		addMax.setText(max);
	}

	public void checkModify(boolean maybe) {
		this.maybe = maybe;
		if (maybe) {
			addType.setDisable(true);
		}
	}

	@FXML
	private void addToOffencesTable(ActionEvent event) {

		try {
			Connection connect = ConnectToDatabase.getConnection();
			PreparedStatement state = connect
					.prepareStatement("INSERT INTO OFFENCES(type, min_sentence, max_sentence) VALUES (?,?,?)");
			try {
				if (this.addType.getText().isEmpty() || this.addType.getText() == null) {
					Alert errorAlert = new Alert(AlertType.ERROR);
					errorAlert.setHeaderText("Input not valid");
					errorAlert.setContentText("Type of sentence cannot be empty.");
					errorAlert.showAndWait();
					return;
				}
				int min = Integer.parseInt(this.addMin.getText());
				int max = Integer.parseInt(this.addMax.getText());

				if (min <= max) {
					state.setString(1, this.addType.getText());
					state.setString(2, this.addMin.getText());
					state.setString(3, this.addMax.getText());

					state.execute();
					connect.close();

				} else {
					Alert errorAlert = new Alert(AlertType.ERROR);
					errorAlert.setHeaderText("Input not valid");
					errorAlert.setContentText("Minimum sentence cannot be bigger than maximum sentence.");
					errorAlert.showAndWait();
					return;
				}
			} catch (SQLiteException sq) {
				Alert errorAlert = new Alert(AlertType.ERROR);
				errorAlert.setHeaderText("Input not valid");
				errorAlert.setContentText("That type of sentence already exists.");
				errorAlert.showAndWait();
				return;

			} catch (RuntimeException re) {
				Alert errorAlert = new Alert(AlertType.ERROR);
				errorAlert.setHeaderText("Input not valid");
				errorAlert.setContentText("Sentence must be a number.");
				errorAlert.showAndWait();
				return;
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		addBtn.getScene().getWindow().hide();

	}

	@FXML
	private void modifyOffencesTable(ActionEvent event) {
		try {
			addType.setDisable(true);
			Connection connect = ConnectToDatabase.getConnection();
			String updateInfo = "UPDATE OFFENCES SET type=?, min_sentence=?, max_sentence=? WHERE type='"
					+ this.addType.getText() + "'";
			PreparedStatement state = connect.prepareStatement(updateInfo);
			try {
				if (this.addType.getText().isEmpty()) {
					Alert errorAlert = new Alert(AlertType.ERROR);
					errorAlert.setHeaderText("Input not valid");
					errorAlert.setContentText("Type of sentence cannot be empty.");
					errorAlert.showAndWait();
					return;
				}
				int min = Integer.parseInt(this.addMin.getText());
				int max = Integer.parseInt(this.addMax.getText());

				if (min <= max) {
					state.setString(1, this.addType.getText());
					state.setString(2, this.addMin.getText());
					state.setString(3, this.addMax.getText());

					state.execute();
					connect.close();

				} else {
					Alert errorAlert = new Alert(AlertType.ERROR);
					errorAlert.setHeaderText("Input not valid");
					errorAlert.setContentText("Minimum sentence cannot be bigger than maximum sentence.");
					errorAlert.showAndWait();
					return;
				}
			} catch (SQLiteException sq) {
				Alert errorAlert = new Alert(AlertType.ERROR);
				errorAlert.setHeaderText("Input not valid");
				errorAlert.setContentText("That type of sentence already exists.");
				errorAlert.showAndWait();
				return;

			} catch (RuntimeException re) {
				Alert errorAlert = new Alert(AlertType.ERROR);
				errorAlert.setHeaderText("Input not valid");
				errorAlert.setContentText("Sentence must be a number.");
				errorAlert.showAndWait();
				return;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		modifyBtn.getScene().getWindow().hide();

	}
}
