package jopa.nodes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import jopa.ports.JOPAControlFlowPort;
import jopa.ports.JOPAPort;

public class JOPAEndControlFlowNode extends JOPANode {

	public JOPAControlFlowPort flowEnd;

	public JOPAEndControlFlowNode(Rectangle rect) {
		super(rect, "End");
		this.flowEnd = new JOPAControlFlowPort(this, new Point(), "end", false);
	}

	@Override
	public void draw(Graphics2D g, JOPANode selectedNode, JOPAPort selectedPort) {
		super.draw(g, selectedNode, selectedPort);
	}

}