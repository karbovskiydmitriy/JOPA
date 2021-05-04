package jopa.ports;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import jopa.nodes.JOPANode;

public abstract class JOPAPort {

	public JOPANode node;
	public Point position;
	public boolean isOutput;
	public String name;
	public ArrayList<JOPAPort> connections;

	public JOPAPort(JOPANode node, Point position, boolean isOutput, String name) {
		this.node = node;
		this.position = position;
		this.isOutput = isOutput;
		this.name = name;
	}

	public abstract boolean hit(Point p);
	public abstract void destroyAllConnections();

	public void move(int x, int y) {
		position.translate(x, y);
	}

	public static void drawConnection(Graphics2D g, Point a, Point b, Color color) {
		g.setColor(color);
		g.drawLine(a.x, a.y, b.x, b.y);
	}

}