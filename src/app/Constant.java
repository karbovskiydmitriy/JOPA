package app;

import types.GLSLType;

public class Constant extends Variable {

	private static final long serialVersionUID = -8118337120653895786L;

	public String value;

	public Constant(GLSLType type, String name, String value) {
		super(type, name);
		this.value = value;
	}

	@Override
	public String toString() {
		return "const " + super.toString() + " = " + value;
	}

}