package jopa;

import java.util.ArrayList;

public class JOPAFormula {

	public String name;
	public final String inputs[];
	public final String outputs[];
	public String template;

	public final String formula;

	private static ArrayList<JOPAFormula> standardFormulas;

	static {
		standardFormulas = new ArrayList<JOPAFormula>();
		try {
			standardFormulas.add(new JOPAFormula("FOOBAR", "FOOBAR")); // FIXME!!!
		} catch (JOPAException e) {
			System.err.println(e.getMessage());
		}
	}

	public JOPAFormula(String name, String formula) throws JOPAException {
		if (name == null || formula == null) {
			throw new JOPAException("Formula string must be non-null!");
		}
		if (name.length() == 0 || formula.length() == 0) {
			throw new JOPAException("Formula string can not be empty!");
		}

		this.name = name;
		this.formula = formula;

		this.inputs = new String[] {
				"input_0", "input_1"
		};
		this.outputs = new String[] {
				"output_0"
		};
	}

	public static JOPAFormula getFormulaByName(String name) {
		for (var formula : standardFormulas) {
			if (formula.name.equals(name)) {
				return formula;
			}
		}

		return null;
	}

}