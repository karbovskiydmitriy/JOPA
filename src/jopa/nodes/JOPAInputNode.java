package jopa.nodes;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import jopa.types.JOPAComplexType;
import jopa.ui.JOPAUIPort;

public class JOPAInputNode extends JOPANode {

	private static final long serialVersionUID = 4796528397625482904L;
	
	public JOPAComplexType inputType;
	
	public JOPAInputNode(Rectangle rect, String header) {
		super(rect, header);
	}
	
	@Override
	public void draw(Graphics2D g, JOPANode selectedNode, JOPAUIPort selectedPort) {
		drawFrame(g, selectedNode == this);
		outputs.forEach(port -> port.draw(g, selectedPort));
	}
	
	@Override
	public boolean inputsConnected() {
		return true;
	}

}