package jopa.nodes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import jopa.ports.JOPAControlPort;
import jopa.ports.JOPAPort;

public class JOPAStartControlNode extends JOPANode {

	public JOPAControlPort flowStart;

	public JOPAStartControlNode(Rectangle rect, String template) {
		super(rect, "Start", template);
	}

	public JOPAStartControlNode(int x, int y, String template) {
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
	protected boolean flowInconsistency() {
		return flowStart.connections.size() != 1;
	}

	@Override
	public boolean remove() {
		return false;
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