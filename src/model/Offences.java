package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Offences {

	private final StringProperty type, minS, maxS;

	public Offences(String type, String minS, String maxS) {
		this.type = new SimpleStringProperty(type);
		this.minS = new SimpleStringProperty(minS);
		this.maxS = new SimpleStringProperty(maxS);
	}

	public String getType() {
		return type.get();
	}

	public String getMinS() {
		return minS.get();
	}

	public String getMaxS() {
		return maxS.get();
	}

	public void setType(String value) {
		type.set(value);
	}

	public void setMinS(String value) {
		minS.set(value);
	}

	public void setMaxS(String value) {
		maxS.set(value);
	}

	public StringProperty typeProperty() {
		return this.type;
	}

	public StringProperty minSProperty() {
		return this.minS;
	}

	public StringProperty maxSProperty() {
		return this.maxS;
	}
}