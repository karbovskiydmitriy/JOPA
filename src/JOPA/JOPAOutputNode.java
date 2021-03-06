package JOPA;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public class JOPAOutputNode extends JOPANode {

	public JOPAComplexType outputType;
	
	public JOPAOutputNode(Rectangle rect, String header, String command, String formula) {
		super(rect, header, command, formula);
	}
	
	@Override
	public void draw(Graphics2D g, JOPANode selectedNode, JOPAPort selectedPort) {
		drawFrame(g, selectedNode == this);
		inputs.forEach(port -> port.draw(g, selectedPort));
	}

}