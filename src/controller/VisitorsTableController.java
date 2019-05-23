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
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.ConnectToDatabase;
import model.Visitors;

public class VisitorsTableController implements Initializable {
	
	@FXML
	private TableView<Visitors> visitorsTable; // Temporary holds the data while program running

	@FXML
	private TableColumn<Visitors, String> visitorIDColumn;
	@FXML
	private TableColumn<Visitors, String> nameColumn;
	@FXML
	private TableColumn<Visitors, Integer> ageColumn;
	@FXML
	private TableColumn<Visitors, String> visitedPrisonerColumn;
	
	private ObservableList<Visitors> visitors;

	@FXML
	private AnchorPane visitorsAnchorPane;
	
	@FXML
	private Button addBtn, deleteBtn, modifyBtn, joinBtn;
	
	private boolean pickle;

	private String visitorsID, name, visitedPrisoner;
	private int age;
	
	private final ReadOnlyObjectWrapper <Visitors> currentVisitors = new ReadOnlyObjectWrapper <> ();
	
	@FXML
	private TextField searchTF;
	
	public ReadOnlyObjectProperty <Visitors> currentPrisonStaffProperty(){
		return currentVisitors.getReadOnlyProperty();
	}
	
	public Visitors getCurrentPrisonStaff () {
		return currentVisitors.get();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		getDatabase();		
		getID();
	}
	
	public void getDatabase() {
		try {
			Connection connect = ConnectToDatabase.getConnection();
			this.visitors = FXCollections.observableArrayList();

			ResultSet res = connect.createStatement().executeQuery("SELECT * FROM VISITORS");
			while (res.next()) {
				this.visitors.add(new Visitors(res.getString(1), res.getString(2), res.getInt(3), res.getString(4)));
			}
			res.close();
			connect.close();
		} catch (SQLException e) {
			System.err.println("Error" + e);
		}

		this.visitorIDColumn.setCellValueFactory(new PropertyValueFactory<Visitors, String>("visitorID"));
		this.nameColumn.setCellValueFactory(new PropertyValueFactory<Visitors, String>("name"));
		this.ageColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAge()).asObject());
		this.visitedPrisonerColumn.setCellValueFactory(new PropertyValueFactory<Visitors, String>("visitedPrisoner"));
		this.visitorsTable.setItems(null);
		this.visitorsTable.setItems(this.visitors);
	}
	
	@FXML
	private void addToCurrentTable(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainWindow.class.getResource("/view/addVisitors.fxml"));
		visitorsAnchorPane = loader.load();
		Stage s1 = new Stage();
		s1.initModality(Modality.APPLICATION_MODAL);
		s1.setTitle("Add Visitors Information");
		Scene scene = new Scene(visitorsAnchorPane);
		s1.setScene(scene);
		s1.show();
		s1.setResizable(false);
		s1.setOnHidden(e -> {
		getDatabase();
		});
	}
	
	private void getID() {
		visitorsTable.setOnMouseClicked(new EventHandler <MouseEvent> () {

			@Override
			public void handle(MouseEvent arg0) {
				if(visitorsTable.getSelectionModel().getSelectedIndex() >= 0) {
					Visitors vs = visitorsTable.getItems().get(visitorsTable.getSelectionModel().getSelectedIndex());
					visitorsID = vs.getVisitorID();
					name = vs.getName();
					age = vs.getAge();
					visitedPrisoner = vs.getVisitedPrisoner().getValue();
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
			
				PreparedStatement st = connect.prepareStatement("DELETE FROM VISITORS WHERE visitor_id = ?");
				st.setString(1, this.visitorsID);
				
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
			if (!visitorsID.isEmpty()) {
				pickle = true;
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/modifyVisitors.fxml"));
				Parent root = (Parent) loader.load();
				AddNModifyVisitors av = loader.getController();
				av.modifyInformation(visitorsID, name, age, visitedPrisoner);
				av.checkModify(pickle);
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
		this.visitors = FXCollections.observableArrayList();
		ResultSet rs = connect.createStatement()
				.executeQuery("SELECT * FROM VISITORS WHERE name LIKE '%" + this.searchTF.getText() + "%'");
		while (rs.next()) {
			this.visitors.add(new Visitors(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getString(4)));
		}
		if(searchTF.getLength()==0) {
			getDatabase();
		}
		this.visitorIDColumn.setCellValueFactory(new PropertyValueFactory<Visitors, String>("visitorID"));
		this.nameColumn.setCellValueFactory(new PropertyValueFactory<Visitors, String>("name"));
		this.ageColumn
		.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAge()).asObject());
		this.visitedPrisonerColumn.setCellValueFactory(new PropertyValueFactory<Visitors, String>("visitedPrisoner"));

		this.visitorsTable.setItems(null);
		this.visitorsTable.setItems(this.visitors);
		rs.close();

	}


}
