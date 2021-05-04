package jopa.nodes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import jopa.ports.JOPAControlFlowPort;
import jopa.ports.JOPAPort;

public class JOPAStatementNode extends JOPANode {

	public JOPAControlFlowPort incomingControlFlow;
	public JOPAControlFlowPort outcomingControlFlow;

	public JOPAStatementNode(Rectangle rect, String header, String template) {
		super(rect, header, template);
		incomingControlFlow = new JOPAControlFlowPort(this, new Point(rect.x, rect.y), "in", false);
		outcomingControlFlow = new JOPAControlFlowPort(this,
				new Point(rect.x + rect.width - JOPAControlFlowPort.CONTROL_FLOW_PORT_WIDTH, rect.y), "out", true);
	}

	@Override
	public void draw(Graphics2D g, JOPANode selectedNode, JOPAPort selectedPort) {
		super.draw(g, selectedNode, selectedPort);
		incomingControlFlow.draw(g, selectedPort);
		outcomingControlFlow.draw(g, selectedPort);
	}

	@Override
	public void move(int x, int y) {
		super.move(x, y);
		incomingControlFlow.move(x, y);
		outcomingControlFlow.move(x, y);
	}

	@Override
	public JOPAPort hitPort(Point p) {
		JOPAPort port = super.hitPort(p);
		if (port != null) {
			return port;
		}
		if (incomingControlFlow.hit(p)) {
			return incomingControlFlow;
		}
		if (outcomingControlFlow.hit(p)) {
			return outcomingControlFlow;
		}

		return null;
	}

}