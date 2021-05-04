package jopa.nodes;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import jopa.main.JOPAVariable;
import jopa.ports.JOPAPort;

public class JOPAVariableNode extends JOPANode {

	public JOPAVariable variable;

	public JOPAVariableNode(Rectangle rect, String header) {
		super(rect, header);
	}

	@Override
	protected void init() {
	}

	@Override
	public void draw(Graphics2D g, JOPANode selectedNode, JOPAPort selectedPort) {
		super.draw(g, selectedNode, selectedPort);
	}

}