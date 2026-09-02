package com.razorrecon.graph;

public class MoneyNode {

	private String id;
	private String type;
	private String label;
	private double amount;

	public MoneyNode() {
	}

	public MoneyNode(String id, String type, String label, double amount) {
		this.id = id;
		this.type = type;
		this.label = label;
		this.amount = amount;
	}

	public String getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public String getLabel() {
		return label;
	}

	public double getAmount() {
		return amount;
	}
}
