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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.ConnectToDatabase;
import model.Prisoners;

public class PrisonersTableController implements Initializable {

	@FXML
	private TableView<Prisoners> prisonersTable; // Temporary holds the data while program running

	@FXML
	private TableColumn<Prisoners, String> prisonerIDColumn;
	@FXML
	private TableColumn<Prisoners, String> nameColumn;
	@FXML
	private TableColumn<Prisoners, String> sexColumn;
	@FXML
	private TableColumn<Prisoners, String> offenceColumn;
	@FXML
	private TableColumn<Prisoners, String> sentenceColumn;
	@FXML
	private TableColumn<Prisoners, Integer> ageColumn;

	private ObservableList<Prisoners> prisoners;

	@FXML
	private AnchorPane prisonersAnchorPane;

	private boolean pickle;

	@FXML
	private Button addBtn, deleteBtn, modifyBtn;

	@FXML
	private TextField searchTF;

	private String prisonerID, name, sex, offence, sentence;
	private int age;

	private final ReadOnlyObjectWrapper<Prisoners> currentPrisoners = new ReadOnlyObjectWrapper<>();

	public ReadOnlyObjectProperty<Prisoners> currentPrisonStaffProperty() {
		return currentPrisoners.getReadOnlyProperty();
	}

	public Prisoners getCurrentPrisonStaff() {
		return currentPrisoners.get();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		getDatabase();
		getID();
	}

	public void getDatabase() {
		try {
			Connection connect = ConnectToDatabase.getConnection();
			this.prisoners = FXCollections.observableArrayList();

			ResultSet res = connect.createStatement().executeQuery("SELECT * FROM PRISONERS");
			while (res.next()) {
				this.prisoners.add(new Prisoners(res.getString(1), res.getString(2), res.getInt(3), res.getString(4),
						res.getString(5), res.getString(6)));
			}
			res.close();
			connect.close();
		} catch (SQLException e) {
			System.err.println("Error" + e);
		}

		this.prisonerIDColumn.setCellValueFactory(new PropertyValueFactory<Prisoners, String>("prisonerID"));
		this.nameColumn.setCellValueFactory(new PropertyValueFactory<Prisoners, String>("name"));
		this.sexColumn.setCellValueFactory(new PropertyValueFactory<Prisoners, String>("sex"));
		this.offenceColumn.setCellValueFactory(new PropertyValueFactory<Prisoners, String>("offence"));
		this.sentenceColumn.setCellValueFactory(new PropertyValueFactory<Prisoners, String>("sentence"));
		this.ageColumn
				.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAge()).asObject());

		this.prisonersTable.setItems(null);
		this.prisonersTable.setItems(this.prisoners);
	}

	@FXML
	private void addToCurrentTable(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainWindow.class.getResource("/view/addPrisoner.fxml"));
		prisonersAnchorPane = loader.load();
		Stage s1 = new Stage();
		s1.initModality(Modality.APPLICATION_MODAL);
		s1.setTitle("Add Prisoner Information");
		Scene scene = new Scene(prisonersAnchorPane);
		s1.setScene(scene);
		s1.show();
		s1.setResizable(false);
		s1.setOnHidden(e -> {
			getDatabase();
		});
	}

	private void getID() {
		prisonersTable.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				if (prisonersTable.getSelectionModel().getSelectedIndex() >= 0) {
					Prisoners ps = prisonersTable.getItems().get(prisonersTable.getSelectionModel().getSelectedIndex());
					prisonerID = ps.getPrisonerID();
					name = ps.getName();
					age = ps.getAge();
					sex = ps.getSex();
					offence = ps.getOffence();
					sentence = ps.getSentence();

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
		if (result.get() == ButtonType.OK) {
			try {
				Connection connect = ConnectToDatabase.getConnection();

				PreparedStatement st = connect.prepareStatement("DELETE FROM PRISONERS WHERE prisoner_id = ?");
				st.setString(1, this.prisonerID);

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
			if (!prisonerID.isEmpty()) {
				pickle = true;
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/modifyPrisoner.fxml"));
				Parent root = (Parent) loader.load();
				AddNModifyPrisoner ao = loader.getController();
				ao.modifyInformation(prisonerID, name, age, sex, offence, sentence);
				ao.checkModify(pickle);
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
		this.prisoners = FXCollections.observableArrayList();
		ResultSet rs = connect.createStatement()
				.executeQuery("SELECT * FROM PRISONERS WHERE name LIKE '%" + this.searchTF.getText() + "%'");
		while (rs.next()) {
			this.prisoners.add(new Prisoners(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getString(4),
					rs.getString(5), rs.getString(6)));
		}
		if(searchTF.getLength()==0) {
			getDatabase();
		}
		this.prisonerIDColumn.setCellValueFactory(new PropertyValueFactory<Prisoners, String>("prisonerID"));
		this.nameColumn.setCellValueFactory(new PropertyValueFactory<Prisoners, String>("name"));
		this.sexColumn.setCellValueFactory(new PropertyValueFactory<Prisoners, String>("sex"));
		this.offenceColumn.setCellValueFactory(new PropertyValueFactory<Prisoners, String>("offence"));
		this.sentenceColumn.setCellValueFactory(new PropertyValueFactory<Prisoners, String>("sentence"));
		this.ageColumn
				.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAge()).asObject());

		this.prisonersTable.setItems(null);
		this.prisonersTable.setItems(this.prisoners);
		rs.close();

	}

}
