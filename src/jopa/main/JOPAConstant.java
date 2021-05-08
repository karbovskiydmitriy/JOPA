package jopa.main;

import jopa.types.JOPAGLSLType;

public class JOPAConstant extends JOPAVariable {

	private static final long serialVersionUID = -8118337120653895786L;
	
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