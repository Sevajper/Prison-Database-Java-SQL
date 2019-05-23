package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.ConnectToDatabase;
import model.JoinTable;

public class JoinTablesController implements Initializable {

	@FXML
	private TableView<JoinTable> jt;

	@FXML
	private TableColumn<JoinTable, String> prisonerIDColumn;
	@FXML
	private TableColumn<JoinTable, String> nameColumn;
	@FXML
	private TableColumn<JoinTable, String> sexColumn;
	@FXML
	private TableColumn<JoinTable, String> offenceColumn;
	@FXML
	private TableColumn<JoinTable, String> sentenceColumn;
	@FXML
	private TableColumn<JoinTable, Integer> ageColumn;

	private ObservableList<JoinTable> ol;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {

			Connection connect = ConnectToDatabase.getConnection();
			this.ol = FXCollections.observableArrayList();
			ResultSet rs = connect.createStatement()
					.executeQuery("SELECT PRISONERS.prisoner_id, PRISONERS.name, PRISONERS.age, PRISONERS.sex, "
							+ "PRISONERS.offence, PRISONERS.sentence FROM PRISONERS "
							+ "INNER JOIN OFFENCES ON PRISONERS.offence=OFFENCES.type ORDER BY PRISONERS.name");
			while (rs.next()) {
				this.ol.add(new JoinTable(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getString(4),
						rs.getString(5), rs.getString(6)));
			}

			rs.close();
			connect.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		this.prisonerIDColumn.setCellValueFactory(new PropertyValueFactory<JoinTable, String>("prisonerID"));
		this.nameColumn.setCellValueFactory(new PropertyValueFactory<JoinTable, String>("name"));
		this.sexColumn.setCellValueFactory(new PropertyValueFactory<JoinTable, String>("sex"));
		this.offenceColumn.setCellValueFactory(new PropertyValueFactory<JoinTable, String>("offence"));
		this.sentenceColumn.setCellValueFactory(new PropertyValueFactory<JoinTable, String>("sentence"));
		this.ageColumn
				.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAge()).asObject());

		this.jt.setItems(null);
		this.jt.setItems(this.ol);
	}


}
