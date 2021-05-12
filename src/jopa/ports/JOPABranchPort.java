package jopa.ports;

import jopa.nodes.JOPANode;

public class JOPABranchPort extends JOPAControlPort {

	private static final long serialVersionUID = -4643801084153590809L;

	public String condition;

	public JOPABranchPort(JOPANode node, String condition) {
		super(node, "", true);
		this.condition = condition;
	}

}