package jopa.types;

import jopa.main.JOPACodeConvertible;

public abstract class JOPAType implements JOPACodeConvertible {

	public String name;

	public JOPAType(String name) {
		this.name = name;
	}

}