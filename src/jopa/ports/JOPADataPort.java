package jopa.ports;

import static jopa.util.JOPAColorUtil.getColorForType;
import static jopa.util.JOPAColorUtil.getNameForType;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;

import jopa.main.JOPAMain;
import jopa.nodes.JOPANode;
import jopa.types.JOPAGLSLType;

public class JOPADataPort extends JOPAPort {

	private final int PORT_RADIUS = 7;

	public JOPAGLSLType dataType;

	private String typeName;
	private Color color;

	public JOPADataPort(JOPANode node, Point position, String name, boolean isOutput, JOPADataPort... connections) {
		super(node, position, isOutput, name);
		this.connections = new ArrayList<JOPAPort>(Arrays.asList(connections));
		this.dataType = JOPAGLSLType.JOPA_NONE;
		this.typeName = getNameForType(dataType);
		this.color = getColorForType(dataType);
	}

	public void draw(Graphics2D g, JOPAPort selectedPort) {
		if (selectedPort != this) {
			g.setColor(color);
		} else {
			g.setColor(JOPAMain.settings.defaultPalette.selectedPortColor);
		}
		g.fillOval(position.x - PORT_RADIUS, position.y - PORT_RADIUS, PORT_RADIUS * 2, PORT_RADIUS * 2);
		g.setColor(Color.BLACK);
		g.drawOval(position.x - PORT_RADIUS, position.y - PORT_RADIUS, PORT_RADIUS * 2, PORT_RADIUS * 2);
		String text = JOPAMain.settings.showPortTypes ? typeName + " " + name : name;
		if (!isOutput) {
			FontRenderContext frc = g.getFontRenderContext();
			Font font = g.getFont();
			Rectangle2D r = font.getStringBounds(text, frc);
			g.drawString(text, position.x - (int) r.getWidth() - PORT_RADIUS * 2, position.y + PORT_RADIUS);
		} else {
			g.drawString(text, position.x + PORT_RADIUS * 2, position.y + PORT_RADIUS);
		}
		if (isOutput) {
			for (JOPAPort port : connections) {
				drawConnection(g, position, port.position, color);
			}
		}
	}

	@Override
	public boolean hit(Point p) {
		return (p.distanceSq(position) <= PORT_RADIUS * PORT_RADIUS);
	}

	@Override
	public void destroyAllConnections() {
		connections.forEach(port -> port.connections.remove(this));
		connections.clear();
	}

}