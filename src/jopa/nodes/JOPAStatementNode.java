package jopa.nodes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import jopa.ports.JOPAControlPort;
import jopa.ports.JOPAPort;

public class JOPAStatementNode extends JOPANode {

	public JOPAControlPort incomingControlFlow;
	public JOPAControlPort outcomingControlFlow;

	public JOPAStatementNode(Rectangle rect, String header, String template) {
		super(rect, header, template);
	}

	public JOPAStatementNode(int x, int y, String header, String template) {
		super(x, y, header, template);
	}

	public JOPAStatementNode(Rectangle rect, String template) {
		super(rect, "STATEMENT", template);
	}

	public JOPAStatementNode(int x, int y, String template) {
		super(x, y, "STATEMENT", template);
	}

	@Override
	protected void init() {
		incomingControlFlow = new JOPAControlPort(this, "in", false);
		outcomingControlFlow = new JOPAControlPort(this, "out", true);
	}

	@Override
	public boolean remove() {
		inputs.forEach(port -> port.destroyAllConnections());
		outputs.forEach(port -> port.destroyAllConnections());
		incomingControlFlow.destroyAllConnections();
		outcomingControlFlow.destroyAllConnections();

		return true;
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
		if (incomingControlFlow.hit(p)) {
			return incomingControlFlow;
		}
		if (outcomingControlFlow.hit(p)) {
			return outcomingControlFlow;
		}

		return super.hitPort(p);
	}

}