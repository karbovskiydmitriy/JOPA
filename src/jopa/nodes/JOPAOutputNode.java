package jopa.nodes;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import jopa.ports.JOPADataPort;

public class JOPAOutputNode extends JOPANode {
	
	public JOPAOutputNode(Rectangle rect, String header) {
		super(rect, header);
	}
	
	public JOPAOutputNode(Rectangle rect, String header, String template) {
		super(rect, header, template);
	}
	
	@Override
	public void draw(Graphics2D g, JOPANode selectedNode, JOPADataPort selectedPort) {
		drawFrame(g, selectedNode == this);
		inputs.forEach(port -> port.draw(g, selectedPort));
	}

}