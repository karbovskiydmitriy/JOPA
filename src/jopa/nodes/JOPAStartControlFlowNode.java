package jopa.nodes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import jopa.ports.JOPAControlFlowPort;
import jopa.ports.JOPAPort;

public class JOPAStartControlFlowNode extends JOPANode {

	public JOPAControlFlowPort flowStart;

	public JOPAStartControlFlowNode(Rectangle rect, String template) {
		super(rect, "Start", template);
		this.flowStart = new JOPAControlFlowPort(this,
				new Point(rect.x + rect.width - JOPAControlFlowPort.CONTROL_FLOW_PORT_WIDTH, rect.y), "start", true);
	}

	@Override
	public void draw(Graphics2D g, JOPANode selectedNode, JOPAPort selectedPort) {
		super.draw(g, selectedNode, selectedPort);
		flowStart.draw(g, selectedPort);
	}

	@Override
	public void move(int x, int y) {
		super.move(x, y);
		flowStart.move(x, y);
	}

	@Override
	public JOPAPort hitPort(Point p) {
		JOPAPort port = super.hitPort(p);
		if (port != null) {
			return port;
		}
		if (flowStart.hit(p)) {
			return flowStart;
		}

		return null;
	}

}