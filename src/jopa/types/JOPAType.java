package jopa.types;

public abstract class JOPAType {

	public String name;

	public JOPAType(String name) {
		this.name = name;
	}

	public abstract String generateCode();

}