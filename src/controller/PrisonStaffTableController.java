package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.ConnectToDatabase;
import model.PrisonStaff;

public class PrisonStaffTableController implements Initializable {
	
	@FXML
	private TableView<PrisonStaff> prisonStaffTable; // Temporary holds the data while program running

	@FXML
	private TableColumn<PrisonStaff, String> staffIDColumn;
	@FXML
	private TableColumn<PrisonStaff, String> nameColumn;
	@FXML
	private TableColumn<PrisonStaff, String> sexColumn;
	@FXML
	private TableColumn<PrisonStaff, String> jobColumn;
	@FXML
	private TableColumn<PrisonStaff, Integer> ageColumn;
	
	@FXML
	private AnchorPane prisonStaffAnchorPane;
	
	@FXML
	private BorderPane mainBorderPane;
	
	@FXML
	private Button addBtn, deleteBtn, modifyBtn, joinBtn;
	
	@FXML
	private TextField searchTF;
	
	private String staffID, name, sex, job;
	private int age;
	
	private boolean pickle;
	
	private ObservableList<PrisonStaff> prisonStaff;
	private final ReadOnlyObjectWrapper <PrisonStaff> currentPrisonStaff = new ReadOnlyObjectWrapper <> ();
	
	public ReadOnlyObjectProperty <PrisonStaff> currentPrisonStaffProperty(){
		return currentPrisonStaff.getReadOnlyProperty();
	}
	
	public PrisonStaff getCurrentPrisonStaff () {
		return currentPrisonStaff.get();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		getDatabase();		
		getID();
	}
	
	public void getDatabase() {
		try {
			Connection connect = ConnectToDatabase.getConnection();
			this.prisonStaff = FXCollections.observableArrayList();

			ResultSet res = connect.createStatement().executeQuery("SELECT * FROM PRISON_STAFF");
			while (res.next()) {
				this.prisonStaff.add(new PrisonStaff(res.getString(1), res.getString(2), res.getInt(3),
						res.getString(4), res.getString(5)));
			}
			res.close();
			connect.close();
		} catch (SQLException e) {
			System.err.println("Error" + e);
		}

		this.staffIDColumn.setCellValueFactory(new PropertyValueFactory<PrisonStaff, String>("staffID"));
		this.nameColumn.setCellValueFactory(new PropertyValueFactory<PrisonStaff, String>("name"));
		this.sexColumn.setCellValueFactory(new PropertyValueFactory<PrisonStaff, String>("sex"));
		this.jobColumn.setCellValueFactory(new PropertyValueFactory<PrisonStaff, String>("job"));
		this.ageColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAge()).asObject());
		
		this.prisonStaffTable.setItems(null);
		this.prisonStaffTable.setItems(this.prisonStaff);
	}
	
	@FXML
	private void addToCurrentTable(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainWindow.class.getResource("/view/addPrisonStaff.fxml"));
		prisonStaffAnchorPane = loader.load();
		Stage s1 = new Stage();
		s1.initModality(Modality.APPLICATION_MODAL);
		s1.setTitle("Add Prison Staff Information");
		Scene scene = new Scene(prisonStaffAnchorPane);
		s1.setScene(scene);
		s1.show();
		s1.setResizable(false);
		s1.setOnHidden(e -> {
		getDatabase();
		});
	}
	
	private void getID() {
		prisonStaffTable.setOnMouseClicked(new EventHandler <MouseEvent> () {
			@Override
			public void handle(MouseEvent arg0) {
				if(prisonStaffTable.getSelectionModel().getSelectedIndex() >= 0) {
					PrisonStaff ps = prisonStaffTable.getItems().get(prisonStaffTable.getSelectionModel().getSelectedIndex());
					staffID = ps.getStaffID();
					name = ps.getName();
					age = ps.getAge();
					sex = ps.getSex();
					job = ps.getJob();
				}			
			}		
		});
	}

	@FXML
	private void deleteSelection(ActionEvent event) throws IOException {
		Alert alertC = new Alert(AlertType.CONFIRMATION);
		alertC.setTitle("Confirmation Dialog");
		alertC.setHeaderText("Are you sure you want to delete this entry?");

		Optional<ButtonType> result = alertC.showAndWait();
		if (result.get() == ButtonType.OK){
			try {
				Connection connect = ConnectToDatabase.getConnection();
			
				PreparedStatement st = connect.prepareStatement("DELETE FROM PRISON_STAFF WHERE staff_id = ?");
				st.setString(1, this.staffID);
				
				st.executeUpdate();
				getDatabase();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			getDatabase();
		}
	}
	
	@FXML
	private void modifyCurrentTable(ActionEvent event) throws IOException {
		try {
			if (!staffID.isEmpty()) {
				pickle = true;
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/modifyPrisonStaff.fxml"));
				Parent root = (Parent) loader.load();
				AddNModifyPrisonStaff ap = loader.getController();
				ap.modifyInformation(staffID, name, age, sex, job);
				ap.checkModify(pickle);
				Stage stage = new Stage();
				stage.setScene(new Scene(root));
				stage.show();
				stage.setResizable(false);
				stage.setOnHidden(e -> {
					getDatabase();
				});
			}
		} catch (RuntimeException re) {
			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setHeaderText("No selection");
			errorAlert.setContentText("Please select a field you want to modify.");
			errorAlert.showAndWait();
			return;
		}
	}

	@FXML
	private void onSearch(KeyEvent event) throws SQLException {
		Connection connect = ConnectToDatabase.getConnection();
		this.prisonStaff = FXCollections.observableArrayList();
		ResultSet rs = connect.createStatement()
				.executeQuery("SELECT * FROM PRISON_STAFF WHERE name LIKE '%" + this.searchTF.getText() + "%'");
		while (rs.next()) {
			this.prisonStaff.add(new PrisonStaff(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getString(4),
					rs.getString(5)));
		}
		if(searchTF.getLength()==0) {
			getDatabase();
		}
		this.staffIDColumn.setCellValueFactory(new PropertyValueFactory<PrisonStaff, String>("staffID"));
		this.nameColumn.setCellValueFactory(new PropertyValueFactory<PrisonStaff, String>("name"));
		this.sexColumn.setCellValueFactory(new PropertyValueFactory<PrisonStaff, String>("sex"));
		this.jobColumn.setCellValueFactory(new PropertyValueFactory<PrisonStaff, String>("job"));
		this.ageColumn
				.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAge()).asObject());

		this.prisonStaffTable.setItems(null);
		this.prisonStaffTable.setItems(this.prisonStaff);
		rs.close();

	}



}
