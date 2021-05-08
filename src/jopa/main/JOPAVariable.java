package jopa.main;

import static jopa.util.JOPATypeUtil.getNameForType;

import java.io.Serializable;

import jopa.types.JOPAGLSLType;

public class JOPAVariable implements Serializable {

	private static final long serialVersionUID = 8693545636379587528L;
	
	public String name;
	public JOPAGLSLType type;
	public String modifiers;

	public JOPAVariable(String name, JOPAGLSLType type, String modifiers) {
		this.name = name;
		this.type = type;
		this.modifiers = modifiers;
	}

	@Override
	public String toString() {
		if (modifiers == null) {
			return getNameForType(type) + " " + name;
		} else {
			return modifiers + " " + getNameForType(type) + " " + name;
		}
	}

}