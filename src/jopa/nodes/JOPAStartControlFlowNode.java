package jopa.nodes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import jopa.ports.JOPAControlFlowPort;
import jopa.ports.JOPAPort;

public class JOPAStartControlFlowNode extends JOPANode {

	public JOPAControlFlowPort flowStart;

	public JOPAStartControlFlowNode(Rectangle rect) {
		super(rect, "Start");
		this.flowStart = new JOPAControlFlowPort(this, new Point(), "start", true);
	}

	@Override
	public void draw(Graphics2D g, JOPANode selectedNode, JOPAPort selectedPort) {
		super.draw(g, selectedNode, selectedPort);
	}

}