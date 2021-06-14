package nodes;

import static util.TypeUtil.*;

import app.Function;
import app.Variable;
import ports.DataPort;
import ports.Port;
import types.GLSLType;

public class FunctionNode extends StatementNode {

	private static final long serialVersionUID = 7559559202358367590L;

	public Function referencedFunction;

	public FunctionNode(Function function, int x, int y, Function referencedFunction) {
		super(function, x, y, "FUNCTION", null);
		this.referencedFunction = referencedFunction;
		applyFunction();
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
		if (referencedFunction.returnType != GLSLType.VOID) {
			functionCallCode += getNameForType(((DataPort) outputs.get(0)).variable.type);
			functionCallCode += " " + outputs.get(0).getName() + " = ";
		}
		functionCallCode += referencedFunction.name;
		functionCallCode += "(";
		for (int i = 0; i < referencedFunction.args.size(); i++) {
			Port input = inputs.get(i);
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

	public void applyFunction() {
		if (inputs != null) {
			inputs.forEach(input -> input.destroyAllConnections());
			inputs.clear();
		}
		if (outputs != null) {
			outputs.forEach(output -> output.destroyAllConnections());
			outputs.clear();
		}
		if (referencedFunction != null) {
			for (Variable arg : referencedFunction.args) {
				createPort(arg, false, false);
			}
			Variable outVariable = new Variable(referencedFunction.returnType, referencedFunction.name);
			createPort(outVariable, true, true);
		}
	}

}