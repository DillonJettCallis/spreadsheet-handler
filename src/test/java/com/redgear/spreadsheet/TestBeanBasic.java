package com.redgear.spreadsheet;

import javax.validation.constraints.Min;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
public class TestBeanBasic {

	@Column(index = 0, header = "Name")
	public String name;

	@Column(index = 1, header = "Email")
	private String email;

	@Column(index = 2, header = "Title")
	private String title;

	@Min(value = 21) //For the validation test.
	@Column(index = 4, header = "Age")
	public int age;

	public TestBeanBasic(){

	}

	public TestBeanBasic(String name, String email, String title, int age){
		this.name = name;
		this.email = email;
		this.title = title;
		this.age = age;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTitle() {
		return title;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TestBeanBasic that = (TestBeanBasic) o;

		if (age != that.age) return false;
		if (name != null ? !name.equals(that.name) : that.name != null) return false;
		if (email != null ? !email.equals(that.email) : that.email != null) return false;
		return title != null ? title.equals(that.title) : that.title == null;

	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (email != null ? email.hashCode() : 0);
		result = 31 * result + (title != null ? title.hashCode() : 0);
		result = 31 * result + age;
		return result;
	}

	@Override
	public String toString() {
		return "TestBeanBasic{" +
				"name='" + name + '\'' +
				", email='" + email + '\'' +
				", title='" + title + '\'' +
				", age=" + age +
				'}';
	}
}
