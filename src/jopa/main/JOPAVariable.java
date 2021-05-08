package jopa.main;

import static jopa.util.JOPATypeUtil.getNameForType;
import static jopa.util.JOPATypeUtil.getTypeForName;

import java.io.Serializable;

import jopa.types.JOPAGLSLType;

public class JOPAVariable implements Serializable {

	private static final long serialVersionUID = 8693545636379587528L;

	public JOPAGLSLType type;
	public String name;
	public String modifiers;

	public JOPAVariable(JOPAGLSLType type, String name, String modifiers) {
		this.type = type;
		this.name = name;
		this.modifiers = modifiers;
	}

	public JOPAVariable(JOPAGLSLType type, String name) {
		this.type = type;
		this.name = name;
		this.modifiers = null;
	}

	public static JOPAVariable create(String template) {
		if (template.contains(":")) {
			String[] parts = template.split(":");
			if (parts.length == 2) {
				JOPAGLSLType type = getTypeForName(parts[0]);
				if (type != JOPAGLSLType.JOPA_NONE) {
					return new JOPAVariable(type, parts[1]);
				}
			} else if (parts.length == 3) {
				JOPAGLSLType type = getTypeForName(parts[1]);
				if (type != JOPAGLSLType.JOPA_NONE) {
					return new JOPAVariable(type, parts[2], parts[0]);
				} else {
					type = getTypeForName(parts[0]);
					if (type != JOPAGLSLType.JOPA_NONE) {
						return new JOPAConstant(type, parts[1], parts[2]);
					}
				}
			}
		}

		return null;
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