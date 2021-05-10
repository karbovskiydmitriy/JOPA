package jopa.types;

import jopa.main.JOPACodeConvertible;

public class JOPACustomType implements JOPACodeConvertible {

	public String name;
	public String template;

	public JOPACustomType(String name) {
		this.name = name;
	}

	@Override
	public String generateCode() {
		// TODO generateCode

		return null;
	}

}