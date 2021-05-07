package jopa.types;

public class JOPAComplexType extends JOPAType {

	public JOPAType[] nested;

	public JOPAComplexType(String name) {
		super(name);
	}

	@Override
	public String generateCode() {
		// TODO generateCode

		return null;
	}

}