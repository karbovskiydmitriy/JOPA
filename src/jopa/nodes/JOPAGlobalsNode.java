package jopa.nodes;

import static jopa.main.JOPAFunction.NEW_LINE;
import static jopa.main.JOPAMain.currentProject;

import java.awt.Graphics2D;

import jopa.main.JOPAVariable;
import jopa.ports.JOPAPort;

public class JOPAGlobalsNode extends JOPANode {

	private static final long serialVersionUID = -4245064405386575712L;

	public JOPAGlobalsNode(int x, int y) {
		super(x, y, "GLOBALS");
	}

	@Override
	protected void init() {
		super.init();
	}

	@Override
	public boolean check() {
		return true;
	}

	@Override
	public String generateCode() {
		String code = "";

		for (JOPAVariable globalVariable : currentProject.globalVariables) {
			code += globalVariable.toString() + NEW_LINE;
		}

		return code;
	}

	@Override
	public boolean remove() {
		return false;
	}

	@Override
	public void draw(Graphics2D g, JOPANode selectedNode, JOPAPort selectedPort) {
		super.draw(g, selectedNode, selectedPort);
	}

	public void updateGlobals() {
		inputs.forEach(input -> input.destroyAllConnections());
		outputs.forEach(output -> output.destroyAllConnections());
		inputs.clear();
		outputs.clear();
		for (JOPAVariable globalVariable : currentProject.globalVariables) {
			createPort(globalVariable, false, false);
			createPort(globalVariable, true, false);
		}
		adjustPorts();
	}

}