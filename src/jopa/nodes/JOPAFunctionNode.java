package jopa.nodes;

import jopa.main.JOPACodeConvertible;
import jopa.main.JOPAFunction;
import jopa.main.JOPAVariable;

public class JOPAFunctionNode extends JOPANode implements JOPACodeConvertible {

	private static final long serialVersionUID = 7559559202358367590L;

	public JOPAFunctionNode(int x, int y, JOPAFunction function) {
		super(x, y, "FUNCTION", null);
		applyFunction(function);
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

	public void applyFunction(JOPAFunction function) {
		if (inputs != null) {
			inputs.forEach(input -> input.destroyAllConnections());
			inputs.clear();
		}
		if (outputs != null) {
			outputs.forEach(output -> output.destroyAllConnections());
			outputs.clear();
		}
		if (function != null) {
			for (JOPAVariable arg : function.args) {
				createPort(arg, false, false);
			}
			JOPAVariable outVariable = new JOPAVariable(function.returnType, function.name);
			createPort(outVariable, true, true);
		}
	}

}