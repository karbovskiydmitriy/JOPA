package jopa.main;

import jopa.types.JOPAGLSLType;

public class JOPAConstant extends JOPAVariable {

	private static final long serialVersionUID = -8118337120653895786L;

	public String value;

	public JOPAConstant(JOPAGLSLType type, String name, String value) {
		super(type, name);
		this.value = value;
	}

	@Override
	public String toString() {
		return super.toString() + " = " + value;
	}

}