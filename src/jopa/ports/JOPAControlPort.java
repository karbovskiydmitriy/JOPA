package jopa.ports;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import jopa.main.JOPAMain;
import jopa.nodes.JOPANode;

public class JOPAControlPort extends JOPAPort {

	public static final int WIDTH = 8;
	public static final int HEIGHT = 20;

	public JOPAControlPort(JOPANode node, String name, boolean isOutput) {
		super(node, getControlPortPosition(node.rect, isOutput), isOutput, name);
		this.connections = new ArrayList<JOPAPort>();
	}

	@Override
	public boolean hit(Point p) {
		return position.x <= p.x && position.x + WIDTH >= p.x && position.y <= p.y && position.y + HEIGHT >= p.y;
	}
	
	@Override
	public void update() {
		// TODO?
	}

	public void draw(Graphics2D g, JOPAPort selectedPort) {
		if (selectedPort != this) {
			g.setColor(Color.BLACK);
		} else {
			g.setColor(JOPAMain.settings.defaultPalette.selectedPortColor);
		}
		g.drawRect(position.x, position.y, WIDTH, HEIGHT);
		if (isOutput) {
			for (JOPAPort port : connections) {
				drawConnection(g, position.x + WIDTH / 2, position.y + HEIGHT / 2, port.position.x + WIDTH / 2,
						port.position.y + HEIGHT / 2, Color.BLACK);
			}
		}
	}

	@Override
	public void destroyAllConnections() {
		connections.forEach(port -> port.connections.remove(this));
		connections.clear();
	}

	private static Point getControlPortPosition(Rectangle rect, boolean isOutput) {
		return isOutput ? new Point(rect.x + rect.width - JOPAControlPort.WIDTH, rect.y) : new Point(rect.x, rect.y);
	}

}