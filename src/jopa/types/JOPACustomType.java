package jopa.types;

public class JOPACustomType {

	public String name;
	public String template;

	public JOPACustomType(String name) {
		this.name = name;
	}

	public String generateCode() {
		return template;
	}

}