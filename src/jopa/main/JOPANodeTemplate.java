package jopa.main;

import static jopa.io.JOPALoader.loadStandardTemplate;
import static jopa.main.JOPAMain.gui;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jopa.exceptions.JOPAException;

public class JOPANodeTemplate {

	public String name;
	public String inputs[];
	public String outputs[];
	public String template;

	private static ArrayList<JOPANodeTemplate> standardFormulas;

	static {
		initStandardNodeTemplates();
	}

	public JOPANodeTemplate(String name, String formula) throws JOPAException {
		if (name == null || formula == null) {
			throw new JOPAException("Formula string must be non-null!");
		}
		if (name.length() == 0 || formula.length() == 0) {
			throw new JOPAException("Formula string can not be empty!");
		}

		this.name = name;

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
					JsonElement templateElement = object.get("template");
					if (templateElement != null) {
						if (templateElement.isJsonNull()) {
							template = "";
						} else {
							template = templateElement.getAsString();
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
	}

	public static JOPANodeTemplate getFormulaByName(String name) {
		for (JOPANodeTemplate formula : standardFormulas) {
			if (formula.name.equals(name)) {
				return formula;
			}
		}

		return null;
	}

	private static void initStandardNodeTemplates() {
		standardFormulas = new ArrayList<JOPANodeTemplate>();
		try {
			String standardTemplates = loadStandardTemplate("templates.json");
			if (standardTemplates != null) {
				JsonElement templatesElement = new JsonParser().parse(standardTemplates);
				if (templatesElement != null) {
					JsonObject templatesObject = templatesElement.getAsJsonObject();
					if (templatesObject != null) {
						JsonElement nodesElement = templatesObject.get("nodes");
						if (nodesElement != null) {
							JsonObject nodesObject = nodesElement.getAsJsonObject();
							if (nodesObject != null) {
								for (String name : nodesObject.keySet()) {
									JOPANodeTemplate foobar = getFormulaFromTemplate(nodesObject, name);
									if (foobar != null) {
										standardFormulas.add(foobar);
									}
								}
							} else {
								fileCorrupted();
							}
						} else {
							fileCorrupted();
						}
					} else {
						fileCorrupted();
					}
				} else {
					fileCorrupted();
				}
			} else {
				fileCorrupted();
			}
		} catch (JOPAException e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void fileCorrupted() {
		gui.showMessage("templates file is corrupted");
	}

	private static JOPANodeTemplate getFormulaFromTemplate(JsonObject object, String name) throws JOPAException {
		if (object == null || name == null || name.length() == 0) {
			return null;
		}

		JsonElement element = object.get(name);
		if (element != null) {
			JsonObject formulaObject = element.getAsJsonObject();
			if (formulaObject != null) {
				return new JOPANodeTemplate(name, formulaObject.toString());
			}
		}

		return null;
	}

}