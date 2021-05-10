package jopa.nodes;

import jopa.main.JOPAFunction;
import jopa.main.JOPAVariable;

public class JOPAFunctionNode extends JOPAStatementNode {

	private static final long serialVersionUID = 7559559202358367590L;

	private JOPAFunction function;

	public JOPAFunctionNode(int x, int y, JOPAFunction function) {
		super(x, y, "FUNCTION", null);
		applyFunction(function);
	}

	@Override
	public boolean check() {
		if (function == null) {
			return false;
		}
		if (flowInconsistency()) {
			return false;
		}
		if (!inputsConnected()) {
			return false;
		}
		if (!function.verifyNodes()) {
			return false;
		}

		return super.check();
	}

	@Override
	public String generateCode() {
		String connectionsCode = generateConnectionsCode();
		String functionCallCode = function.name;
		functionCallCode += "(";
		for (int i = 0; i < function.args.size(); i++) {
			JOPAVariable arg = function.args.get(i);
			functionCallCode += arg.name;
			boolean isTheLast = i == function.args.size() - 1;
			if (!isTheLast) {
				functionCallCode += ", ";
			}
		}
		functionCallCode += ");";
		String chainCode = outcomingControlFlow.connections.get(0).node.generateCode();

		return connectionsCode + functionCallCode + chainCode;
	}

	@Override
	public boolean remove() {
		if (inputs != null) {
			inputs.forEach(input -> input.destroyAllConnections());
			inputs.clear();
		}
		if (outputs != null) {
			outputs.forEach(output -> output.destroyAllConnections());
			outputs.clear();
		}

		return true;
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
		this.function = function;
		if (function != null) {
			for (JOPAVariable arg : function.args) {
				createPort(arg, false, false);
			}
			JOPAVariable outVariable = new JOPAVariable(function.returnType, function.name);
			createPort(outVariable, true, true);
		}
	}

}