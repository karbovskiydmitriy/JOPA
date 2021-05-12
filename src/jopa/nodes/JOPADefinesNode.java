package jopa.nodes;

import static jopa.main.JOPAFunction.NEW_LINE;

import jopa.main.JOPAMain;
import jopa.main.JOPASymbol;

public class JOPADefinesNode extends JOPANode {

	private static final long serialVersionUID = 8021995277803568890L;

	public JOPADefinesNode(int x, int y) {
		super(x, y, "DEFINES");
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

		for (JOPASymbol define : JOPAMain.currentProject.defines) {
			code += "#define " + define.name + " " + define.value + NEW_LINE;
		}

		return code;
	}

}