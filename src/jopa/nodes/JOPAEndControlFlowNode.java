package jopa.nodes;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import jopa.ports.JOPAPort;

public class JOPAEndControlFlowNode extends JOPANode {

	public JOPAEndControlFlowNode flowEnd;

	public JOPAEndControlFlowNode(Rectangle rect, String header, JOPAEndControlFlowNode flowEnd) {
		super(rect, header);
		this.flowEnd = flowEnd;
	}

	@Override
	public void draw(Graphics2D g, JOPANode selectedNode, JOPAPort selectedPort) {
		super.draw(g, selectedNode, selectedPort);
	}

}