package jopa.main;

import static jopa.io.JOPALoader.loadStandardTemplate;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jopa.exceptions.JOPAException;

public class JOPATemplate {

	public final String formula;

	public String name;
	public String inputs[];
	public String outputs[];
	public String template;
	public boolean isPure;

	private static ArrayList<JOPATemplate> standardFormulas;

	static {
		initStandardTemplates();
	}

	public JOPATemplate(String name, String formula) throws JOPAException {
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
			this.inputs = new String[] { "stub_input_0" };
		}
		if (this.outputs == null) {
			this.outputs = new String[] { "stub_output_0" };
		}
	}

	public static JOPATemplate getFormulaByName(String name) {
		for (JOPATemplate formula : standardFormulas) {
			if (formula.name.equals(name)) {
				return formula;
			}
		}

		return null;
	}

	private static void initStandardTemplates() {
		standardFormulas = new ArrayList<JOPATemplate>();
		try {
			String standardTemplates = loadStandardTemplate("standard.json");
			JsonElement templatesElement = new JsonParser().parse(standardTemplates);
			JsonObject templatesObject = templatesElement.getAsJsonObject();
			JsonElement nodesElement = templatesObject.get("nodes");
			JsonObject nodesObject = nodesElement.getAsJsonObject();
			for (String name : nodesObject.keySet()) {
				JOPATemplate foobar = getFormulaFromTemplate(nodesObject, name);
				if (foobar != null) {
					standardFormulas.add(foobar);
				}
			}
		} catch (JOPAException e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static JOPATemplate getFormulaFromTemplate(JsonObject object, String name) throws JOPAException {
		if (object == null || name == null || name.length() == 0) {
			return null;
		}

		JsonElement element = object.get(name);
		if (element != null) {
			JsonObject formulaObject = element.getAsJsonObject();
			if (formulaObject != null) {
				return new JOPATemplate(name, formulaObject.toString());
			}
		}

		return null;
	}

}