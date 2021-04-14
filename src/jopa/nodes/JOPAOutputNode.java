package jopa.nodes;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public class JOPAOutputNode extends JOPANode {
	
	public JOPAOutputNode(Rectangle rect, String header) {
		super(rect, header);
	}
	
	@Override
	public void draw(Graphics2D g, JOPANode selectedNode, JOPAPort selectedPort) {
		drawFrame(g, selectedNode == this);
		inputs.forEach(port -> port.draw(g, selectedPort));
	}

}