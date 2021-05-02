package jopa.ports;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;

import jopa.JOPAMain;
import jopa.nodes.JOPANode;
import jopa.types.JOPAGLSLType;
import jopa.types.JOPAType;

public class JOPADataPort extends JOPAPort {

	public ArrayList<JOPADataPort> connections;
	public JOPAGLSLType dataType;

	private String typeName;
	private Color color;

	public JOPADataPort(JOPANode node, Point position, String name, boolean isOutput, JOPADataPort... connections) {
		super(node, position, isOutput, name);
		this.connections = new ArrayList<JOPADataPort>(Arrays.asList(connections));
		this.dataType = JOPAGLSLType.JOPA_NONE;
		this.typeName = getNameForType(dataType);
		this.color = getColorForType(dataType);
	}

	public void move(int x, int y) {
		position.translate(x, y);
	}

	public void draw(Graphics2D g, JOPAPort selectedPort) {
		if (selectedPort != this) {
			g.setColor(color);
		} else {
			g.setColor(JOPAMain.settings.defaultPalette.selectedPortColor);
		}
		g.fillOval(position.x - PORT_RADIUS, position.y - PORT_RADIUS, PORT_RADIUS * 2, PORT_RADIUS * 2);
		FontRenderContext frc = g.getFontRenderContext();
		Font font = g.getFont();
		g.setColor(Color.BLACK);
		g.drawOval(position.x - PORT_RADIUS, position.y - PORT_RADIUS, PORT_RADIUS * 2, PORT_RADIUS * 2);
		String text = JOPAMain.settings.showPortTypes ? typeName + " " + name : name;
		if (!isOutput) {
			Rectangle2D r = font.getStringBounds(text, frc);
			g.drawString(text, position.x - (int) r.getWidth() - PORT_RADIUS * 2, position.y + PORT_RADIUS);
		} else {
			g.drawString(text, position.x + PORT_RADIUS * 2, position.y + PORT_RADIUS);
		}
		if (isOutput) {
			for (JOPADataPort port : connections) {
				drawConnection(g, position, port.position, color);
			}
		}
	}

	public static void drawConnection(Graphics2D g, Point a, Point b, Color color) {
		g.setColor(color);
		g.drawLine(a.x, a.y, b.x, b.y);
	}

	public static String getNameForType(JOPAGLSLType type) {
		if (type == null) {
			return null;
		}

		switch (type) {
		case JOPA_BOOL:
			return "bool";
		case JOPA_INT:
			return "int";
		case JOPA_UINT:
			return "uint";
		case JOPA_FLOAT:
			return "float";
		case JOPA_BOOL_VECTOR_2:
			return "bvec2";
		case JOPA_BOOL_VECTOR_3:
			return "bvec3";
		case JOPA_BOOL_VECTOR_4:
			return "bvec4";
		case JOPA_INT_VECTOR_2:
			return "ivec2";
		case JOPA_INT_VECTOR_3:
			return "ivec3";
		case JOPA_INT_VECTOR_4:
			return "ivec4";
		case JOPA_UINT_VECTOR_2:
			return "uvec2";
		case JOPA_UINT_VECTOR_3:
			return "uvec3";
		case JOPA_UINT_VECTOR_4:
			return "uvec4";
		case JOPA_FLOAT_VECTOR_2:
			return "vec2";
		case JOPA_FLOAT_VECTOR_3:
			return "vec3";
		case JOPA_FLOAT_VECTOR_4:
			return "vec4";
		default:
			return "TODO";
		}
	}

	public static Color getColorForType(JOPAGLSLType type) {
		if (type == null) {
			return JOPAMain.settings.defaultPalette.portColor;
		}

		switch (type) {
		case JOPA_BOOL:
			return JOPAMain.settings.defaultPalette.boolTypeColor;
		case JOPA_INT:
			return JOPAMain.settings.defaultPalette.intTypeColor;
		case JOPA_UINT:
			return JOPAMain.settings.defaultPalette.boolTypeColor;
		case JOPA_FLOAT:
			return JOPAMain.settings.defaultPalette.boolTypeColor;
		case JOPA_BOOL_VECTOR_2:
		case JOPA_BOOL_VECTOR_3:
		case JOPA_BOOL_VECTOR_4:
		case JOPA_INT_VECTOR_2:
		case JOPA_INT_VECTOR_3:
		case JOPA_INT_VECTOR_4:
		case JOPA_UINT_VECTOR_2:
		case JOPA_UINT_VECTOR_3:
		case JOPA_UINT_VECTOR_4:
		case JOPA_FLOAT_VECTOR_2:
		case JOPA_FLOAT_VECTOR_3:
		case JOPA_FLOAT_VECTOR_4:
			return JOPAMain.settings.defaultPalette.vectorTypeColor;
		default:
			return JOPAMain.settings.defaultPalette.portColor;
		}
	}

	public static Color getColorForType(JOPAType type) {
		if (type == null) {
			return Color.BLACK;
		}

		switch (type.name) {
		default:
			return Color.BLACK;
		}
	}

	public boolean hit(Point p) {
		return (p.distanceSq(position) <= PORT_RADIUS * PORT_RADIUS);
	}

	public void destroyAllConnections() {
		connections.forEach(port -> port.connections.remove(this));
		connections.clear();
	}

}