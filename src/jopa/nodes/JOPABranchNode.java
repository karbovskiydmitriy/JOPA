package jopa.nodes;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import jopa.ports.JOPAPort;

public class JOPABranchNode extends JOPANode {

	public JOPABranchNode(Rectangle rect, String header) {
		super(rect, header);
	}
	
	@Override
	protected void init() {
		// TODO	
	}

	@Override
	public void draw(Graphics2D g, JOPANode selectedNode, JOPAPort selectedPort) {
		super.draw(g, selectedNode, selectedPort);
	}

}