package jopa.nodes;

import static jopa.main.JOPAFunction.TWO_LINES;

import jopa.main.JOPAMain;
import jopa.types.JOPAType;

public class JOPATypesNode extends JOPANode {

	private static final long serialVersionUID = -6075227936804404765L;

	public JOPATypesNode(int x, int y) {
		super(x, y, "TYPES");
	}

	@Override
	public String generateCode() {
		String typesCode = "";

		for (JOPAType customType : JOPAMain.currentProject.types) {
			typesCode += customType.generateCode() + TWO_LINES;
		}

		return typesCode;
	}

	@Override
	public boolean check() {
		return true;
	}

	@Override
	public boolean remove() {
		return false;
	}

}