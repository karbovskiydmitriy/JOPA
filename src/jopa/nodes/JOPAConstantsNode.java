package jopa.nodes;

import java.awt.Rectangle;

public class JOPAConstantsNode extends JOPANode {

	public JOPAConstantsNode(Rectangle rect, String template) {
		super(rect, "CONSTANTS", template);
	}

	public JOPAConstantsNode(int x, int y, String template) {
		super(x, y, "CONSTANTS", template);
	}

	@Override
	protected void init() {
		// TODO
	}
	
	@Override
	public boolean check() {
		return true;
	}

	@Override
	public boolean remove() {
		return false;
	}

}