package nodes;

import static app.Function.NEW_LINE;
import static app.Main.currentProject;

import java.awt.Graphics2D;

import app.Function;
import app.Variable;
import ports.Port;

public class GlobalsNode extends Node {

	private static final long serialVersionUID = -4245064405386575712L;

	public GlobalsNode(Function function, int x, int y) {
		super(function, x, y, "GLOBALS");
	}

	@Override
	protected void init() {
		super.init();
	}

	@Override
	public boolean check() {
		return true;
	}

	public String generateCode() {
		String code = "";

		for (Variable globalVariable : currentProject.globalVariables) {
			code += globalVariable.toString() + NEW_LINE;
		}

		return code;
	}

	@Override
	public boolean remove() {
		return false;
	}

	@Override
	public void draw(Graphics2D g, Node selectedNode, Port selectedPort) {
		super.draw(g, selectedNode, selectedPort);
	}

	public void updateGlobals() {
		inputs.forEach(input -> input.destroyAllConnections());
		outputs.forEach(output -> output.destroyAllConnections());
		inputs.clear();
		outputs.clear();
		for (Variable globalVariable : currentProject.globalVariables) {
			createPort(globalVariable, false, false);
			createPort(globalVariable, true, false);
		}
		adjustPorts();
	}

}