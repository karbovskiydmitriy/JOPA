package app;

import static app.Main.gui;
import static io.Loader.loadStandardTemplate;
import static util.TypeUtil.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import types.GLSLType;

public class Template {

	public String name;
	public String inputs[];
	public String outputs[];
	public String type;
	public String template;

	private Template(String name) {
		this.name = name;
	}

	public static Template create(String name, String formula) {
		if (name == null || formula == null) {
			return null;
		}
		if (name.length() == 0 || formula.length() == 0) {
			return null;
		}

		Template template = new Template(name);

		try {
			JsonElement element = new JsonParser().parse(formula);
			if (element != null) {
				JsonObject object = element.getAsJsonObject();
				if (object != null) {
					JsonElement typeElement = object.get("type");
					if (typeElement != null) {
						String typeString = typeElement.getAsString();
						if (typeString != null) {
							template.type = typeString;
						}
					}
					JsonElement inputsElement = object.get("inputs");
					if (inputsElement != null) {
						JsonArray inputsArray = inputsElement.getAsJsonArray();
						if (inputsArray != null) {
							template.inputs = new String[inputsArray.size()];
							for (int i = 0; i < inputsArray.size(); i++) {
								String input = inputsArray.get(i).getAsString();
								template.inputs[i] = input;
							}
						}
					}
					JsonElement outputsElement = object.get("outputs");
					if (outputsElement != null) {
						JsonArray outputsArray = outputsElement.getAsJsonArray();
						if (outputsArray != null) {
							template.outputs = new String[outputsArray.size()];
							for (int i = 0; i < outputsArray.size(); i++) {
								String output = outputsArray.get(i).getAsString();
								template.outputs[i] = output;
							}
						}
					}
					JsonElement templateElement = object.get("template");
					if (templateElement != null) {
						if (templateElement.isJsonNull()) {
							template.template = "";
						} else {
							template.template = templateElement.getAsString();
						}
					}
				}
			}
		} catch (IllegalStateException e) {
			System.err.println(e);
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}

		return template;
	}

	public static Template getFormulaByName(String name) {
		for (Template formula : Main.currentProject.templates) {
			if (formula.name.equals(name)) {
				return formula;
			}
		}

		return null;
	}

	public static void initStandardTemplates(Project project) {
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
									Template template = loadTemplate(nodesObject, name);
									if (template != null) {
										project.templates.add(template);
									}
								}
							} else {
								fileCorrupted();
							}
						} else {
							fileCorrupted();
						}
						JsonElement functionsElement = templatesObject.get("functions");
						if (functionsElement != null) {
							JsonObject functionsObject = functionsElement.getAsJsonObject();
							if (functionsObject != null) {
								for (String name : functionsObject.keySet()) {
									Template template = loadTemplate(functionsObject, name);
									if (template != null) {
										if (template.outputs.length == 1) {
											project.templates.add(template);
											GLSLType returnType = getTypeForName(template.outputs[0]);
											Variable[] args = new Variable[template.inputs.length];
											for (int i = 0; i < template.inputs.length; i++) {
												String input = template.inputs[i];
												Variable variable = Variable.create(input);
												args[i] = variable;
											}
											Function function = new Function(template.name, returnType, false,
													template.template, args);
											project.functions.add(function);
										}
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void fileCorrupted() {
		gui.showMessage("templates file is corrupted");
	}

	private static Template loadTemplate(JsonObject object, String name) {
		if (object == null || name == null || name.length() == 0) {
			return null;
		}

		JsonElement element = object.get(name);
		if (element != null) {
			JsonObject formulaObject = element.getAsJsonObject();
			if (formulaObject != null) {
				return Template.create(name, formulaObject.toString());
			}
		}

		return null;
	}

	@Override
	public String toString() {
		JsonObject obj = new JsonObject();

		JsonArray inputsArray = new JsonArray();
		for (String input : inputs) {
			inputsArray.add(input);
		}
		JsonArray outputsArray = new JsonArray();
		for (String output : outputs) {
			outputsArray.add(output);
		}

		obj.add("inputs", inputsArray);
		obj.add("outputs", outputsArray);
		obj.add("type", new JsonPrimitive(type));
		obj.add("template", new JsonPrimitive(template));

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String text = gson.toJson(obj);

		return text;
	}

}