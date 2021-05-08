package jopa.nodes;

import java.awt.Rectangle;

import jopa.main.JOPAConstant;
import jopa.main.JOPAMain;
import jopa.main.JOPAProject;

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
		project = JOPAMain.currentProject;
		for (JOPAConstant constant : project.constants) {
			createPort(constant.type, constant.name, true, false);
		}
		adjustPorts();
	}

	public void updateConstants() {
		outputs.forEach(outputPort -> {
			outputPort.destroyAllConnections();
		});
		outputs.clear();
		init();
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