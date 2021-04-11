package jopa;

public class JOPAFormula {

	public final String inputs[];
	public final String outputs[];
	public String template;

	public final String formula;

	public JOPAFormula(String formula) throws JOPAException {
		if (formula == null) {
			throw new JOPAException("Formula string must be non-null!");
		}
		if (formula.length() == 0) {
			throw new JOPAException("Formula string can not be empty!");
		}
		this.formula = formula;

		this.inputs = new String[] {
				"input_0", "input_1"
		};
		this.outputs = new String[] {
				"output_0"
		};
	}

}