package nodes;

import static app.Function.NEW_LINE;

import app.Function;
import app.Main;
import app.Symbol;

public class DefinesNode extends Node {

	private static final long serialVersionUID = 8021995277803568890L;

	public DefinesNode(Function function, int x, int y) {
		super(function, x, y, "DEFINES");
	}

	@Override
	public boolean check() {
		return true;
	}

	@Override
	public boolean remove() {
		return false;
	}

	@Override
	public String generateCode() {
		String code = "";

		for (Symbol define : Main.currentProject.defines) {
			code += "#define " + define.name + " " + define.value + NEW_LINE;
		}

		return code;
	}

}