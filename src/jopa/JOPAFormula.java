package jopa;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jopa.exceptions.JOPAException;

public class JOPAFormula {

	public static final String FOOBAR = "{inputs:[\"input_0\",\"input_1\",\"input_2\"],outputs:[\"output_0\"]}";

	public final String formula;

	public String name;
	public String inputs[];
	public String outputs[];
	public String template;
	public boolean isPure;

	private static ArrayList<JOPAFormula> standardFormulas;

	static {
		standardFormulas = new ArrayList<JOPAFormula>();
		try {
			standardFormulas.add(new JOPAFormula("FOOBAR", FOOBAR));
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

		try {
			JsonElement element = new JsonParser().parse(formula);
			if (element != null) {
				JsonObject object = element.getAsJsonObject();
				if (object != null) {
					JsonElement inputsElement = object.get("inputs");
					if (inputsElement != null) {
						JsonArray inputsArray = inputsElement.getAsJsonArray();
						if (inputsArray != null) {
							this.inputs = new String[inputsArray.size()];
							for (int i = 0; i < inputsArray.size(); i++) {
								String input = inputsArray.get(i).getAsString();
								inputs[i] = input;
							}
						}
					}
					JsonElement outputsElement = object.get("outputs");
					if (outputsElement != null) {
						JsonArray outputsArray = outputsElement.getAsJsonArray();
						if (outputsArray != null) {
							this.outputs = new String[outputsArray.size()];
							for (int i = 0; i < outputsArray.size(); i++) {
								String output = outputsArray.get(i).getAsString();
								outputs[i] = output;
							}
						}
					}
				}
			}
		} catch (IllegalStateException e) {
			System.out.println(e);
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}

		if (this.inputs == null) {
			this.inputs = new String[] { "input_0", "input_1" };
		}
		if (this.outputs == null) {
			this.outputs = new String[] { "output_0" };
		}
	}

	public static JOPAFormula getFormulaByName(String name) {
		for (JOPAFormula formula : standardFormulas) {
			if (formula.name.equals(name)) {
				return formula;
			}
		}

		return null;
	}

}