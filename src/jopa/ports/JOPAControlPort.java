package jopa.ports;

import static jopa.main.JOPAMain.settings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import jopa.nodes.JOPANode;

public class JOPAControlPort extends JOPAPort {

	private static final long serialVersionUID = -4488663573363159229L;

	public static final int WIDTH = 8;
	public static final int HEIGHT = 20;

	public JOPAControlPort(JOPANode node, String name, boolean isOutput) {
		super(node, isOutput);
		this.position = getControlPortPosition(node.rect, isOutput, 0);
		this.connections = new ArrayList<JOPAPort>();
	}

	@Override
	public boolean hit(Point p) {
		return position.x <= p.x && position.x + WIDTH >= p.x && position.y <= p.y && position.y + HEIGHT >= p.y;
	}

	@Override
	public void update() {
	}

	public void draw(Graphics2D g, JOPAPort selectedPort) {
		if (selectedPort != this) {
			g.setColor(Color.BLACK);
		} else {
			g.setColor(settings.defaultPalette.selectedPortColor);
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

	private static Point getControlPortPosition(Rectangle rect, boolean isOutput, int index) {
		int x = isOutput ? rect.x + rect.width - JOPAControlPort.WIDTH : rect.x;
		int y = rect.y + index * HEIGHT;

		return new Point(x, y);
	}

	public String generateCode() {
		if (!isOutput) {
			return null;
		}
		if (connections.size() == 0) {
			return null;
		}
		
		return connections.get(0).node.generateCode();
	}

}