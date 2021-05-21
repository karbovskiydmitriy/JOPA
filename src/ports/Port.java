package ports;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

import nodes.Node;

public abstract class Port implements Serializable {

	private static final long serialVersionUID = 4696720939425567556L;

	public Node node;
	public Point position;
	public boolean isOutput;
	public ArrayList<Port> connections;

	public Port(Node node, boolean isOutput/* , String name */) {
		this.node = node;
		this.isOutput = isOutput;
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

	public boolean makeConnection(Port to) {
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
		if (getClass().equals(ControlPort.class)) {
			destroyAllConnections();
			to.destroyAllConnections();
		}
		connect(to);

		return true;
	}

	protected void connect(Port to) {
		connections.add(to);
		to.connections.add(this);
	}

	public String getName() {
		return null;
	}

}