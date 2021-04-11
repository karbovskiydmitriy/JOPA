package jopa;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import jopa.types.JOPAComplexType;

public class JOPAInputNode extends JOPANode {

	public JOPAComplexType inputType;
	
	public JOPAInputNode(Rectangle rect, String header, String command, String formula) {
		super(rect, header, command, formula);
	}
	
	@Override
	public void draw(Graphics2D g, JOPANode selectedNode, JOPAPort selectedPort) {
		drawFrame(g, selectedNode == this);
		outputs.forEach(port -> port.draw(g, selectedPort));
	}

}