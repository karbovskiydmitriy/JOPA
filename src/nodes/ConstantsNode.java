package nodes;

import static app.Function.NEW_LINE;
import static app.Main.currentProject;

import app.Constant;
import app.Function;
import app.Project;;

public class ConstantsNode extends Node {

	private static final long serialVersionUID = 6463101741433264603L;

	private Project project;

	public ConstantsNode(Function function, int x, int y) {
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

		for (Constant constant : project.constants) {
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
		for (Constant constant : project.constants) {
			createPort(constant, true, false);
		}
		adjustPorts();
	}

}