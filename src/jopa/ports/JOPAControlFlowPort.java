package jopa.ports;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

import jopa.main.JOPAMain;
import jopa.nodes.JOPANode;

public class JOPAControlFlowPort extends JOPAPort {

	public static final int CONTROL_FLOW_PORT_WIDTH = 10;
	public static final int CONTROL_FLOW_PORT_HEIGHT = 20;

	public JOPAControlFlowPort(JOPANode node, Point position, String name, boolean isOutput,
			JOPAControlFlowPort... connections) {
		super(node, position, isOutput, name);
		this.connections = new ArrayList<JOPAPort>(Arrays.asList(connections));
	}

	public void draw(Graphics2D g, JOPAPort selectedPort) {
		if (selectedPort != this) {
			g.setColor(Color.BLACK);
		} else {
			g.setColor(JOPAMain.settings.defaultPalette.selectedPortColor);
		}
		g.drawRect(position.x, position.y, CONTROL_FLOW_PORT_WIDTH, CONTROL_FLOW_PORT_HEIGHT);
	}

	@Override
	public boolean hit(Point p) {
		return position.x <= p.x && position.x + CONTROL_FLOW_PORT_WIDTH >= p.x && position.y <= p.y
				&& position.y + CONTROL_FLOW_PORT_HEIGHT >= p.y;
	}

	@Override
	public void destroyAllConnections() {
		connections.forEach(port -> port.connections.remove(this));
		connections.clear();
	}

}