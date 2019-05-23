package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PrisonStaff {

	private final StringProperty staffID, name, sex, job;
	private final IntegerProperty age;

	public PrisonStaff(String staffID, String name, int age, String sex, String job)
	{
		this.staffID = new SimpleStringProperty(staffID);
		this.name = new SimpleStringProperty(name);
		this.sex = new SimpleStringProperty(sex);
		this.job = new SimpleStringProperty(job);
		this.age = new SimpleIntegerProperty(age);
	}
	
	public String getStaffID(){
		return staffID.get();
		}
	
	public String getName(){
		return name.get();
		}
	
	public String getSex(){
		return sex.get();
		}
	
	public String getJob(){
		return job.get();
		}
	
	public int getAge(){
		return age.get();
		}

	public void setStaffID(String value){
		staffID.set(value);
	}
	
	public void setName(String value){
		name.set(value);
	}
	
	public void setSex(String value){
		sex.set(value);
	}
	
	public void setJob(String value){
		job.set(value);
	}
	
	public void setAge(int value){
		age.set(value);
	}
	
	public StringProperty staffIDProperty(){
		return this.staffID;
	}
	
	public StringProperty nameProperty(){
		return this.name;
	}
	
	public StringProperty sexProperty(){
		return this.sex;
	}
	
	public StringProperty jobProperty(){
		return this.job;
	}
	
	public IntegerProperty ageProperty(){
		return this.age;
	}
}