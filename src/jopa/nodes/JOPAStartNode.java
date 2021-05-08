package jopa.nodes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import jopa.ports.JOPAControlPort;
import jopa.ports.JOPAPort;

public class JOPAStartNode extends JOPANode {

	private static final long serialVersionUID = 4818138558904580829L;
	
	public JOPAControlPort flowStart;

	public JOPAStartNode(Rectangle rect, String template) {
		super(rect, "Start", template);
	}

	public JOPAStartNode(int x, int y, String template) {
		super(x, y, "Start", template);
	}

	@Override
	protected void init() {
		this.flowStart = new JOPAControlPort(this, "start", true);
	}

	@Override
	public boolean check() {
		if (flowInconsistency()) {
			return false;
		}

		return true;
	}

	@Override
	public String generateCode() {
		String chainCode = flowStart.connections.get(0).node.generateCode();

		return chainCode;
	}

	@Override
	public boolean remove() {
		return false;
	}

	@Override
	protected boolean flowInconsistency() {
		return flowStart.connections.size() != 1;
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
		if (flowStart.hit(p)) {
			return flowStart;
		}

		return super.hitPort(p);
	}

}