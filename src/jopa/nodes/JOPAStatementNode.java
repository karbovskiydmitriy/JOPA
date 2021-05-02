package jopa.nodes;

import java.awt.Rectangle;

public class JOPAStatementNode extends JOPANode {

	public JOPAStatementNode(Rectangle rect, String header) {
		super(rect, header);
	}
	
	public JOPAStatementNode(Rectangle rect, String header, String template) {
		super(rect, header, template);
	}

}