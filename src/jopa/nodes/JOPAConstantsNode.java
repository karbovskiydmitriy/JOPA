package jopa.nodes;

import static jopa.main.JOPAFunction.NEW_LINE;
import static jopa.main.JOPAMain.currentProject;

import java.awt.Rectangle;

import jopa.main.JOPAConstant;
import jopa.main.JOPAProject;;

public class JOPAConstantsNode extends JOPANode {

	private static final long serialVersionUID = 6463101741433264603L;

	private JOPAProject project;

	public JOPAConstantsNode(Rectangle rect, String template) {
		super(rect, "CONSTANTS", template);
	}

	public JOPAConstantsNode(int x, int y, String template) {
		super(x, y, "CONSTANTS", template);
	}

	@Override
	protected void init() {
		super.init();
		project = currentProject;
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

	@Override
	public boolean check() {
		return true;
	}

	@Override
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

}