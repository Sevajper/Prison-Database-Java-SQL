package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Visitors {

	private final StringProperty visitorID, name, visitedPrisoner;
	private final IntegerProperty age;

	public Visitors(String visitorID, String name, int age, String visitedPrisoner) {
		this.visitorID = new SimpleStringProperty(visitorID);
		this.name = new SimpleStringProperty(name);
		this.age = new SimpleIntegerProperty(age);
		this.visitedPrisoner= new SimpleStringProperty(visitedPrisoner);
	}

	public String getVisitorID() {
		return visitorID.get();
	}

	public String getName() {
		return name.get();
	}

	public int getAge() {
		return age.get();
	}

	public void setVisitorID(String value) {
		visitorID.set(value);
	}

	public void setName(String value) {
		name.set(value);
	}

	public void setAge(int value) {
		age.set(value);
	}

	public StringProperty visitorIDProperty() {
		return this.visitorID;
	}

	public StringProperty nameProperty() {
		return this.name;
	}

	public IntegerProperty ageProperty() {
		return this.age;
	}
	
	public StringProperty visitedPrisonerProperty() {
		return this.visitedPrisoner;
	}

	public StringProperty getVisitedPrisoner() {
		return visitedPrisoner;
	}
}