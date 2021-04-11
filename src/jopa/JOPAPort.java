package jopa;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;

public class JOPAPort {

	final int PORT_RADIUS = 7;

	public boolean output;
	public JOPANode node;
	public Point position;
	public String name;
	public JOPAGLType datatype;
	public ArrayList<JOPAPort> connections;

	public JOPAPort(JOPANode node, Point position, String name, boolean isOutput, JOPAPort... connections) {
		this.node = node;
		this.position = position;
		this.name = name;
		this.output = isOutput;
		this.connections = new ArrayList<JOPAPort>(Arrays.asList(connections));
	}

	public void move(int x, int y) {
		position.translate(x, y);
	}

	public void draw(Graphics2D g, JOPAPort selectedPort) {
		if (selectedPort != this) {
			g.setColor(Color.WHITE);
		} else {
			g.setColor(Color.BLUE);
		}
		g.fillOval(position.x - PORT_RADIUS, position.y - PORT_RADIUS, PORT_RADIUS * 2, PORT_RADIUS * 2);
		FontRenderContext frc = g.getFontRenderContext();
		Font font = g.getFont();
		g.setColor(Color.BLACK);
		g.drawOval(position.x - PORT_RADIUS, position.y - PORT_RADIUS, PORT_RADIUS * 2, PORT_RADIUS * 2);
		if (!output) {
			Rectangle2D r = font.getStringBounds(name, frc);
			g.drawString(name, position.x - (int) r.getWidth() - PORT_RADIUS * 2, position.y + PORT_RADIUS);
		} else {
			g.drawString(name, position.x + PORT_RADIUS * 2, position.y + PORT_RADIUS);
		}
		if (output) {
			for (JOPAPort port : connections) {
				drawConnection(g, position, port.position);
			}
		}
	}

	public static void drawConnection(Graphics2D g, Point a, Point b) {
		g.setColor(Color.BLACK);
		g.drawLine(a.x, a.y, b.x, b.y);
	}

	public boolean hit(Point p) {
		return (p.distanceSq(position) <= PORT_RADIUS * PORT_RADIUS);
	}

	public void destroyAllConnections() {
		connections.forEach(port -> port.connections.remove(this));
	}

}