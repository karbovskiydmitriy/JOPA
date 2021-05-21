package nodes;

import static app.Function.TWO_LINES;
import static app.Main.currentProject;

import app.Function;
import types.CustomType;

public class TypesNode extends Node {

	private static final long serialVersionUID = -6075227936804404765L;

	public TypesNode(Function function, int x, int y) {
		super(function, x, y, "TYPES");
	}

	@Override
	public String generateCode() {
		String typesCode = "";

		for (CustomType customType : currentProject.types) {
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