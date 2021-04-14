package jopa.nodes;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import jopa.types.JOPAComplexType;

public class JOPAInputNode extends JOPANode {

	public JOPAComplexType inputType;
	
	public JOPAInputNode(Rectangle rect, String header) {
		super(rect, header);
	}
	
	@Override
	public void draw(Graphics2D g, JOPANode selectedNode, JOPAPort selectedPort) {
		drawFrame(g, selectedNode == this);
		outputs.forEach(port -> port.draw(g, selectedPort));
	}
	
	@Override
	public boolean inputsConnected() {
		return true;
	}

}