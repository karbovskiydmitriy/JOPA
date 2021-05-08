package jopa.nodes;

import jopa.main.JOPAFunction;

public class JOPAFunctionNode extends JOPANode {

	private static final long serialVersionUID = 7559559202358367590L;

	public JOPAFunctionNode(int x, int y, JOPAFunction function) {
		super(x, y, "FUNCTION", null);
	}

	@Override
	protected void init() {
		super.init();
		// TODO init
	}

	@Override
	public boolean check() {
		// TODO check

		return false;
	}

	@Override
	public String generateCode() {
		// TODO generateCode

		return null;
	}

	@Override
	public boolean remove() {
		// TODO remove

		return false;
	}

}