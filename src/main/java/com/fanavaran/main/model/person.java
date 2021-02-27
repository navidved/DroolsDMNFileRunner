package com.fanavaran.main.model;


public class person implements java.io.Serializable {

	static final long serialVersionUID = 1L;

	private int age;

	public person() {
	}

	public int getAge() {
		return this.age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public person(int age) {
		this.age = age;
	}

}