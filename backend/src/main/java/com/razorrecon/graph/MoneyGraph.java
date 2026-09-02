package com.razorrecon.graph;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class MoneyGraph {

	private final List<MoneyNode> nodes = new ArrayList<>();
	private final List<MoneyRelationship> relationships = new ArrayList<>();

	public void addNode(MoneyNode node) {
		nodes.add(node);
	}

	public void addRelationship(MoneyRelationship relationship) {
		relationships.add(relationship);
	}

	public List<MoneyNode> getNodes() {
		return List.copyOf(nodes);
	}

	public List<MoneyRelationship> getRelationships() {
		return List.copyOf(relationships);
	}

	public void clear() {
		nodes.clear();
		relationships.clear();
	}
}
