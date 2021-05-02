package jopa.ports;

import java.awt.Point;

import jopa.nodes.JOPANode;

public class JOPAPort {

	final int PORT_RADIUS = 7;

	public JOPANode node;
	public Point position;
	public boolean isOutput;
	public String name;

	public JOPAPort(JOPANode node, Point position, boolean isOutput, String name) {
		this.node = node;
		this.position = position;
		this.isOutput = isOutput;
		this.name = name;
	}

}