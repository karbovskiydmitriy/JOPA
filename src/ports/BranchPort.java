package ports;

import nodes.Node;

public class BranchPort extends ControlPort {

	private static final long serialVersionUID = -4643801084153590809L;

	public String condition;

	public BranchPort(Node node, String condition) {
		super(node, "", true);
		this.condition = condition;
	}

}