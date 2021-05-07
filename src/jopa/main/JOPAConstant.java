package jopa.main;

import jopa.types.JOPAGLSLType;

public class JOPAConstant extends JOPAVariable {

	public String value;

	public JOPAConstant(String name, JOPAGLSLType type, String value) {
		super(name, type, null);
		this.value = value;
	}

	@Override
	public String toString() {
		return super.toString() + " = " + value;
	}

}