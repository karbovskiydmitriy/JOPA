package jopa.ports;

import static jopa.util.JOPATypeUtil.getColorForType;
import static jopa.util.JOPATypeUtil.getNameForType;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import jopa.main.JOPACodeConvertible;
import jopa.main.JOPAMain;
import jopa.main.JOPAVariable;
import jopa.nodes.JOPANode;

public class JOPADataPort extends JOPAPort implements JOPACodeConvertible {

	private static final long serialVersionUID = 6506059782634794376L;

	private final int PORT_RADIUS = 7;

	private String typeName;
	private Color color;

	public JOPAVariable variable;

	public JOPADataPort(JOPANode node, JOPAVariable variable, boolean isOutput) {
		super(node, isOutput);
		this.connections = new ArrayList<JOPAPort>();
		this.variable = variable;
		update();
	}

	@Override
	public boolean hit(Point p) {
		return (p.distanceSq(position) <= PORT_RADIUS * PORT_RADIUS);
	}

	@Override
	public void update() {
		typeName = getNameForType(variable.type);
		color = getColorForType(variable.type);
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
		String text = JOPAMain.settings.showPortTypes ? typeName + " " + variable.name : variable.name;
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
				drawConnection(g, position.x, position.y, port.position.x, port.position.y, color);
			}
		}
	}

	@Override
	public void destroyAllConnections() {
		connections.forEach(port -> port.connections.remove(this));
		connections.clear();
	}

	@Override
	public String generateCode() {
		return variable.toString();
	}

}