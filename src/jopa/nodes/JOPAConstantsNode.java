package jopa.nodes;

import static jopa.main.JOPAFunction.NEW_LINE;
import static jopa.main.JOPAMain.currentProject;

import jopa.main.JOPAConstant;
import jopa.main.JOPAFunction;
import jopa.main.JOPAProject;;

public class JOPAConstantsNode extends JOPANode {

	private static final long serialVersionUID = 6463101741433264603L;

	private JOPAProject project;

	public JOPAConstantsNode(JOPAFunction function, int x, int y) {
		super(function, x, y, "CONSTANTS");
	}

	@Override
	protected void init() {
		super.init();
		project = currentProject;
	}

	@Override
	public boolean check() {
		return true;
	}

	public String generateCode() {
		String constantsCode = "";

		for (JOPAConstant constant : project.constants) {
			constantsCode += constant.toString() + ";" + NEW_LINE;
		}

		return constantsCode;
	}

	@Override
	public boolean remove() {
		return false;
	}

	public void updateConstants() {
		outputs.forEach(outputPort -> {
			outputPort.destroyAllConnections();
		});
		outputs.clear();
		init();
		for (JOPAConstant constant : project.constants) {
			createPort(constant, true, false);
		}
		adjustPorts();
	}

}