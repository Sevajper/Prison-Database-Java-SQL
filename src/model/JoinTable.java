package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class JoinTable {

	private final StringProperty prisonerID, name, sex, offence, sentence;
	private final IntegerProperty age;

	public JoinTable(String prisonerID, String name, int age, String sex, String offence, String sentence) {
		this.prisonerID = new SimpleStringProperty(prisonerID);
		this.name = new SimpleStringProperty(name);
		this.sex = new SimpleStringProperty(sex);
		this.offence = new SimpleStringProperty(offence);
		this.sentence = new SimpleStringProperty(sentence);
		this.age = new SimpleIntegerProperty(age);
	}

	public String getPrisonerID() {
		return prisonerID.get();
	}

	public String getName() {
		return name.get();
	}

	public String getSex() {
		return sex.get();
	}

	public String getOffence() {
		return offence.get();
	}

	public String getSentence() {
		return sentence.get();
	}

	public int getAge() {
		return age.get();
	}

	public void setPrisonerID(String value) {
		prisonerID.set(value);
	}

	public void setName(String value) {
		name.set(value);
	}

	public void setSex(String value) {
		sex.set(value);
	}

	public void setOffence(String value) {
		offence.set(value);
	}

	public void setSentence(String value) {
		sentence.set(value);
	}

	public void setAge(int value) {
		age.set(value);
	}

	public StringProperty prisonerIDProperty() {
		return this.prisonerID;
	}

	public StringProperty nameProperty() {
		return this.name;
	}

	public StringProperty sexProperty() {
		return this.sex;
	}

	public StringProperty offenceProperty() {
		return this.offence;
	}
	
	public StringProperty sentenceProperty() {
		return this.sentence;
	}

	public IntegerProperty ageProperty() {
		return this.age;
	}
}