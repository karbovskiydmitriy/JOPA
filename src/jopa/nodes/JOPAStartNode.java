package jopa.nodes;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import jopa.ports.JOPAControlFlowPort;
import jopa.ports.JOPAPort;

public class JOPAStartNode extends JOPANode {

	public JOPAControlFlowPort flowStart;

	public JOPAStartNode(Rectangle rect, String header, JOPAControlFlowPort flowStart) {
		super(rect, header);
		this.flowStart = flowStart;
	}

	@Override
	public void draw(Graphics2D g, JOPANode selectedNode, JOPAPort selectedPort) {

	}

}