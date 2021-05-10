package jopa.ports;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

import jopa.nodes.JOPANode;

public abstract class JOPAPort implements Serializable {

	private static final long serialVersionUID = 4696720939425567556L;
	
	public JOPANode node;
	public Point position;
	public boolean isOutput;
//	public String name;
	public ArrayList<JOPAPort> connections;

	public JOPAPort(JOPANode node, boolean isOutput/*, String name*/) {
		this.node = node;
		this.isOutput = isOutput;
//		this.name = name;
	}

	public abstract boolean hit(Point p);

	public abstract void update();

	public abstract void destroyAllConnections();

	public void move(int x, int y) {
		position.translate(x, y);
	}

	public static void drawConnection(Graphics2D g, int x1, int y1, int x2, int y2, Color color) {
		g.setColor(color);
		g.drawLine(x1, y1, x2, y2);
	}

	public boolean makeConnection(JOPAPort to) {
		if (to == null) {
			return false;
		}
		if (isOutput == to.isOutput) {
			return false;
		}
		if (node == to.node) {
			return false;
		}
		if (!getClass().equals(to.getClass())) {
			return false;
		}

		if (!isOutput) {
			destroyAllConnections();
		} else if (!to.isOutput) {
			to.destroyAllConnections();
		}

		connect(to);

		return true;
	}
	
	protected void connect(JOPAPort to) {
		connections.add(to);
		to.connections.add(this);
	}

}