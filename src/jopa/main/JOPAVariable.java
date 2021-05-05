package jopa.main;

import static jopa.util.JOPATypeUtil.getNameForType;

import jopa.types.JOPAGLSLType;

public class JOPAVariable {

	public String name;
	public JOPAGLSLType type;
	public String modifiers;
	// public Object value;

	public JOPAVariable(String name, JOPAGLSLType type, String modifiers) {
		this.name = name;
		this.type = type;
		this.modifiers = modifiers;
	}

	@Override
	public String toString() {
		if (modifiers != null) {
			return getNameForType(type) + " " + name;
		} else {
			return modifiers + " " + getNameForType(type) + " " + name;
		}
	}

}