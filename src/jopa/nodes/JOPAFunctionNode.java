package jopa.nodes;

import static jopa.util.JOPATypeUtil.*;
import jopa.main.JOPAFunction;
import jopa.main.JOPAVariable;
import jopa.ports.JOPADataPort;
import jopa.ports.JOPAPort;
import jopa.types.JOPAGLSLType;

public class JOPAFunctionNode extends JOPAStatementNode {

	private static final long serialVersionUID = 7559559202358367590L;

	public JOPAFunction referencedFunction;

	public JOPAFunctionNode(JOPAFunction function, int x, int y, JOPAFunction referencedFunction) {
		super(function, x, y, "FUNCTION", null);
		this.referencedFunction = referencedFunction;
		applyFunction(false);
	}

	@Override
	public boolean check() {
		if (referencedFunction == null) {
			return false;
		}
		if (flowInconsistency()) {
			return false;
		}
		if (!inputsConnected()) {
			return false;
		}
		if (referencedFunction == null) {
			return false;
		}
		if (referencedFunction.isCustom) {
			if (!referencedFunction.verifyNodes()) {
				return false;
			}
		}

		return super.check();
	}

	@Override
	public String generateCode() {
		String functionCallCode = "";
		if (referencedFunction.returnType != JOPAGLSLType.VOID) {
			functionCallCode += getNameForType(((JOPADataPort) outputs.get(0)).variable.type);
			functionCallCode += " " + outputs.get(0).getName() + " = ";
		}
		functionCallCode += referencedFunction.name;
		functionCallCode += "(";
		for (int i = 0; i < referencedFunction.args.size(); i++) {
			JOPAPort input = inputs.get(i);
			functionCallCode += input.connections.get(0).getName();
			boolean isTheLast = i == referencedFunction.args.size() - 1;
			if (!isTheLast) {
				functionCallCode += ", ";
			}
		}
		functionCallCode += ");";
		String chainCode = outcomingControlFlow.generateCode();

		return functionCallCode + chainCode;
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

	public void applyFunction(boolean tryToKeepConnections) {
		// TODO use tryToKeepConnections
		if (inputs != null) {
			inputs.forEach(input -> input.destroyAllConnections());
			inputs.clear();
		}
		if (outputs != null) {
			outputs.forEach(output -> output.destroyAllConnections());
			outputs.clear();
		}
		if (referencedFunction != null) {
			for (JOPAVariable arg : referencedFunction.args) {
				createPort(arg, false, false);
			}
			JOPAVariable outVariable = new JOPAVariable(referencedFunction.returnType, referencedFunction.name);
			createPort(outVariable, true, true);
		}
	}

}