package app;

import static util.TypeUtil.getNameForType;
import static util.TypeUtil.getTypeForName;

import java.io.Serializable;

import types.GLSLType;

public class Variable implements Serializable {

	private static final long serialVersionUID = 8693545636379587528L;

	public GLSLType type;
	public String name;
	public String modifiers;

	public Variable(GLSLType type, String name, String modifiers) {
		this.type = type;
		this.name = name;
		this.modifiers = modifiers;
	}

	public Variable(GLSLType type, String name) {
		this.type = type;
		this.name = name;
		this.modifiers = null;
	}

	public static Variable create(String template) {
		if (template.contains(":")) {
			String[] parts = template.split(":");
			if (parts.length == 2) {
				GLSLType type = getTypeForName(parts[0]);
				if (type != GLSLType.NONE) {
					return new Variable(type, parts[1]);
				}
			} else if (parts.length == 3) {
				GLSLType type = getTypeForName(parts[1]);
				if (type != GLSLType.NONE) {
					return new Variable(type, parts[2], parts[0]);
				} else {
					type = getTypeForName(parts[0]);
					if (type != GLSLType.NONE) {
						return new Constant(type, parts[1], parts[2]);
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