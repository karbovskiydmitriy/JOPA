package jopa.nodes;

import java.awt.Rectangle;

import jopa.JOPAVariable;

public class JOPAVariableNode extends JOPANode {

	public JOPAVariable variable;

	public JOPAVariableNode(Rectangle rect, String header) {
		super(rect, header);
	}

}