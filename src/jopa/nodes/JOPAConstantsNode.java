package jopa.nodes;

import java.awt.Rectangle;

import jopa.main.JOPAConstant;
import jopa.main.JOPAMain;
import jopa.main.JOPAWorkspace;

public class JOPAConstantsNode extends JOPANode {

	private JOPAWorkspace project;

	public JOPAConstantsNode(Rectangle rect, String template) {
		super(rect, "CONSTANTS", template);
	}

	public JOPAConstantsNode(int x, int y, String template) {
		super(x, y, "CONSTANTS", template);
	}

	@Override
	protected void init() {
		project = JOPAMain.currentWorkspace;
		for (JOPAConstant constant : project.constants) {
			createPort(constant.type, constant.name, true, false);
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
			constantsCode += constant.toString() + ";\n";
		}

		return constantsCode;
	}

	@Override
	public boolean remove() {
		return false;
	}

}