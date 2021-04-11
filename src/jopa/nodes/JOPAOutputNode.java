package jopa.nodes;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import jopa.ui.JOPAUIPort;

public class JOPAOutputNode extends JOPANode {
	
	private static final long serialVersionUID = 2993276087027109384L;

	public JOPAOutputNode(Rectangle rect, String header) {
		super(rect, header);
	}
	
	@Override
	public void draw(Graphics2D g, JOPANode selectedNode, JOPAUIPort selectedPort) {
		drawFrame(g, selectedNode == this);
		inputs.forEach(port -> port.draw(g, selectedPort));
	}

}