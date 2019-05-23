package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class MainWindow implements Initializable {

	@FXML
	private Button prisonStaffBtn, visitorBtn, prisonersBtn, offencesBtn, joinButton;

	@SuppressWarnings("unused")
	private boolean prisonStaffBool, prisonersBool, visitorsBool, offencesBool;

	@FXML
	private AnchorPane prisonStaffAnchor;

	@FXML
	private AnchorPane prisonersAnchor;

	@FXML
	private BorderPane mainBorderPane;

	AnchorPane addPrisonStaffAnchorPane, addPrisonersAnchorPane, addVisitorsAnchorPane, addOffencesAnchorPane;

	String staffID;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			// mainBorderPane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/prisonStaff.fxml")));
			mainBorderPane.setCenter((Parent) FXMLLoader.load(getClass().getResource("/view/prisonStaff.fxml")));
			prisonStaffBool = true;
			prisonersBool = false;
			visitorsBool = false;
			offencesBool = false;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML
	private void openPrisonersTable(ActionEvent event) throws IOException {
		mainBorderPane.setCenter(null);
		mainBorderPane.setCenter((Parent) FXMLLoader.load(getClass().getResource("/view/prisoners.fxml")));
		prisonStaffBool = false;
		prisonersBool = true;
		visitorsBool = false;
		offencesBool = false;
	}

	@FXML
	private void openPrisonStaffTable(ActionEvent event) throws IOException {
		mainBorderPane.setCenter(null);
		mainBorderPane.setCenter((Parent) FXMLLoader.load(getClass().getResource("/view/prisonStaff.fxml")));
		prisonStaffBool = true;
		prisonersBool = false;
		visitorsBool = false;
		offencesBool = false;
	}

	@FXML
	private void openVisitorsTable(ActionEvent event) throws IOException {
		mainBorderPane.setCenter(null);
		mainBorderPane.setCenter((Parent) FXMLLoader.load(getClass().getResource("/view/visitors.fxml")));
		prisonStaffBool = false;
		prisonersBool = false;
		visitorsBool = true;
		offencesBool = false;
	}

	@FXML
	private void openOffencesTable(ActionEvent event) throws IOException {
		mainBorderPane.setCenter(null);
		mainBorderPane.setCenter((Parent) FXMLLoader.load(getClass().getResource("/view/offences.fxml")));
		prisonStaffBool = false;
		prisonersBool = false;
		visitorsBool = false;
		offencesBool = true;
	}

	@FXML
	private void openJoinTable(ActionEvent event) throws IOException {
		mainBorderPane.setCenter(null);
		mainBorderPane.setCenter((Parent) FXMLLoader.load(getClass().getResource("/view/joinTables.fxml")));
	}

}
