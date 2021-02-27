package com.fanavaran.main.model;

public class travel implements java.io.Serializable {

	static final long serialVersionUID = 1L;

	private int daysCount;
	private java.lang.String destination;
	private int coverLimit;
	private person person;

	public travel() {
	}

	public int getDaysCount() {
		return this.daysCount;
	}

	public void setDaysCount(int daysCount) {
		this.daysCount = daysCount;
	}

	public java.lang.String getDestination() {
		return this.destination;
	}

	public void setDestination(java.lang.String destination) {
		this.destination = destination;
	}

	public int getCoverLimit() {
		return this.coverLimit;
	}

	public void setCoverLimit(int coverLimit) {
		this.coverLimit = coverLimit;
	}

	public person getPerson() {
		return this.person;
	}

	public void setPerson(person person) {
		this.person = person;
	}

	public travel(int daysCount, java.lang.String destination, int coverLimit,
			person person) {
		this.daysCount = daysCount;
		this.destination = destination;
		this.coverLimit = coverLimit;
		this.person = person;
	}

}