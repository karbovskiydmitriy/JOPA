package ports;

import static app.Main.settings;
import static util.TypeUtil.getColorForType;
import static util.TypeUtil.getNameForType;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import app.Constant;
import app.Variable;
import nodes.Node;

public class DataPort extends Port {

	private static final long serialVersionUID = 6506059782634794376L;

	private final int PORT_RADIUS = 7;

	private String typeName;
	private Color color;

	public Variable variable;

	public DataPort(Node node, Variable variable, boolean isOutput) {
		super(node, isOutput);
		this.connections = new ArrayList<Port>();
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

	public void draw(Graphics2D g, Port selectedPort) {
		if (selectedPort != this) {
			g.setColor(color);
		} else {
			g.setColor(settings.defaultPalette.selectedPortColor);
		}
		g.fillOval(position.x - PORT_RADIUS, position.y - PORT_RADIUS, PORT_RADIUS * 2, PORT_RADIUS * 2);
		g.setColor(Color.BLACK);
		g.drawOval(position.x - PORT_RADIUS, position.y - PORT_RADIUS, PORT_RADIUS * 2, PORT_RADIUS * 2);
		String text;
		if (!variable.getClass().equals(Constant.class)) {
			text = settings.showPortTypes ? typeName + " " + variable.name : variable.name;
		} else {
			text = variable.toString();
		}
		if (!isOutput) {
			FontRenderContext frc = g.getFontRenderContext();
			Font font = g.getFont();
			Rectangle2D r = font.getStringBounds(text, frc);
			g.drawString(text, position.x - (int) r.getWidth() - PORT_RADIUS * 2, position.y + PORT_RADIUS);
		} else {
			g.drawString(text, position.x + PORT_RADIUS * 2, position.y + PORT_RADIUS);
		}
		if (isOutput) {
			for (Port port : connections) {
				drawConnection(g, position.x, position.y, port.position.x, port.position.y, color);
			}
		}
	}

	@Override
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
		if (!variable.type.equals(((DataPort) to).variable.type)) {
			return false;
		}

		connect(to);

		return true;
	}

	@Override
	public void destroyAllConnections() {
		connections.forEach(port -> port.connections.remove(this));
		connections.clear();
	}

	public String generateCode() {
		return variable.toString();
	}

	@Override
	public String getName() {
		if (variable.name.contains("gl_")) {
			return variable.name;
		}
		if (node == node.function.startNode) {
			return variable.name;
		}
		if (node == node.function.endNode) {
			return variable.name;
		}

		return variable.name + "_" + node.getID();
	}

}