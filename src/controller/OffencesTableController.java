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
import model.Offences;

public class OffencesTableController implements Initializable {

	@FXML
	private TableView<Offences> offencesTable; // Temporary holds the data while program running

	@FXML
	private TableColumn<Offences, String> typeColumn;
	@FXML
	private TableColumn<Offences, String> minSColumn;
	@FXML
	private TableColumn<Offences, String> maxSColumn;

	private ObservableList<Offences> offences;

	@FXML
	private AnchorPane offencesAnchorPane;

	@FXML
	private Button addBtn, deleteBtn, modifyBtn, joinBtn;
	
	@FXML
	private TextField searchTF;

	private String offencesType, min, max;

	private boolean pickle;

	private final ReadOnlyObjectWrapper<Offences> currentOffences = new ReadOnlyObjectWrapper<>();

	public ReadOnlyObjectProperty<Offences> currentOffencesProperty() {
		return currentOffences.getReadOnlyProperty();
	}

	public Offences getCurrentOffences() {
		return currentOffences.get();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		getDatabase();
		getType();
	}

	public void getDatabase() {
		try {
			Connection connect = ConnectToDatabase.getConnection();
			this.offences = FXCollections.observableArrayList();

			ResultSet res = connect.createStatement().executeQuery("SELECT * FROM OFFENCES");
			while (res.next()) {
				this.offences.add(new Offences(res.getString(1), res.getString(2), res.getString(3)));
			}
			res.close();
			connect.close();
		} catch (SQLException e) {
			System.err.println("Error" + e);
		}

		this.typeColumn.setCellValueFactory(new PropertyValueFactory<Offences, String>("type"));
		this.minSColumn.setCellValueFactory(new PropertyValueFactory<Offences, String>("minS"));
		this.maxSColumn.setCellValueFactory(new PropertyValueFactory<Offences, String>("maxS"));

		this.offencesTable.setItems(null);
		this.offencesTable.setItems(this.offences);
	}

	@FXML
	private void addToCurrentTable(ActionEvent event) throws IOException {
		pickle = false;
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainWindow.class.getResource("/view/addOffences.fxml"));
		offencesAnchorPane = loader.load();
		Stage s1 = new Stage();
		s1.initModality(Modality.APPLICATION_MODAL);
		s1.setTitle("Add Offences Information");
		Scene scene = new Scene(offencesAnchorPane);
		s1.setScene(scene);
		s1.show();
		s1.setResizable(false);
		s1.setOnHidden(e -> {
			getDatabase();
		});
	}

	private void getType() {
		offencesTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if (offencesTable.getSelectionModel().getSelectedIndex() >= 0) {
					Offences os = offencesTable.getItems().get(offencesTable.getSelectionModel().getSelectedIndex());
					offencesType = os.getType();
					min = os.getMinS();
					max = os.getMaxS();
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

				PreparedStatement st = connect.prepareStatement("DELETE FROM OFFENCES WHERE type = ?");
				st.setString(1, this.offencesType);

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
			if (!offencesType.isEmpty()) {
				pickle = true;
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/modifyOffences.fxml"));
				Parent root = (Parent) loader.load();
				AddNModifyOffences ao = loader.getController();
				ao.modifyInformation(offencesType, min, max);
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
		this.offences = FXCollections.observableArrayList();
		ResultSet rs = connect.createStatement()
				.executeQuery("SELECT * FROM OFFENCES WHERE type LIKE '%" + this.searchTF.getText() + "%'");
		while (rs.next()) {
			this.offences.add(new Offences(rs.getString(1), rs.getString(2), rs.getString(3)));
		}
		if(searchTF.getLength()==0) {
			getDatabase();
		}
		this.typeColumn.setCellValueFactory(new PropertyValueFactory<Offences, String>("type"));
		this.minSColumn.setCellValueFactory(new PropertyValueFactory<Offences, String>("minS"));
		this.maxSColumn.setCellValueFactory(new PropertyValueFactory<Offences, String>("maxS"));

		this.offencesTable.setItems(null);
		this.offencesTable.setItems(this.offences);
		rs.close();

	}

}
