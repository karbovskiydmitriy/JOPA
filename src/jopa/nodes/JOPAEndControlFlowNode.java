package jopa.nodes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import jopa.ports.JOPAControlFlowPort;
import jopa.ports.JOPAPort;

public class JOPAEndControlFlowNode extends JOPANode {

	public JOPAControlFlowPort flowEnd;

	public JOPAEndControlFlowNode(Rectangle rect, String template) {
		super(rect, "End", template);
		this.flowEnd = new JOPAControlFlowPort(this, new Point(rect.x, rect.y), "end", false);
	}

	@Override
	public void draw(Graphics2D g, JOPANode selectedNode, JOPAPort selectedPort) {
		super.draw(g, selectedNode, selectedPort);
		flowEnd.draw(g, selectedPort);
	}

	@Override
	public void move(int x, int y) {
		super.move(x, y);
		flowEnd.move(x, y);
	}

	@Override
	public JOPAPort hitPort(Point p) {
		JOPAPort port = super.hitPort(p);
		if (port != null) {
			return port;
		}
		if (flowEnd.hit(p)) {
			return flowEnd;
		}

		return null;
	}

}