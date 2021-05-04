package jopa.nodes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import jopa.ports.JOPAControlPort;
import jopa.ports.JOPAPort;

public class JOPAEndControlNode extends JOPANode {

	public JOPAControlPort flowEnd;

	public JOPAEndControlNode(Rectangle rect, String template) {
		super(rect, "End", template);
		this.flowEnd = new JOPAControlPort(this, "end", false);
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
		if (flowEnd.hit(p)) {
			return flowEnd;
		}

		return super.hitPort(p);
	}

}