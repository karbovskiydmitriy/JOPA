package jopa.main;

import static jopa.io.JOPAIO.loadTextFile;
import static jopa.io.JOPAIO.saveTextFile;

import java.awt.Color;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jopa.graphics.JOPAPalette;

public class JOPASettings {

	private static final String CONFIG_DIRECTORY_PATH = ".\\configs\\";
	private static final String CONFIG_FILE_PATH = CONFIG_DIRECTORY_PATH + "config.json";
	private static final String PALETTE_FILE_PATH = CONFIG_DIRECTORY_PATH + "palette.json";

	public boolean showPortTypes = true;
	public boolean highlightNodes = true;
	public boolean copyTempValues = true;
	public JOPAPalette defaultPalette;

	public JOPASettings() {
		readSettings();
	}

	public void readSettings() {
		String text = loadTextFile(CONFIG_FILE_PATH);

		JsonElement element = new JsonParser().parse(text);
		JsonObject configObject = element.getAsJsonObject();
		if (configObject != null) {
			JsonElement showPortTypesElement = configObject.get("showPortTypes");
			if (showPortTypesElement != null) {
				showPortTypes = showPortTypesElement.getAsBoolean();
			}
			JsonElement highlightNodesElement = configObject.get("highlightNodes");
			if (highlightNodesElement != null) {
				highlightNodes = highlightNodesElement.getAsBoolean();
			}
		}
		defaultPalette = loadPalette(PALETTE_FILE_PATH);
	}

	public void writeSettings() {
		JsonObject configObject = new JsonObject();
		configObject.addProperty("showPortTypes", showPortTypes);
		configObject.addProperty("highlightNodes", highlightNodes);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String text = gson.toJson(configObject);

		saveTextFile(CONFIG_FILE_PATH, text);

		savePalette(defaultPalette, PALETTE_FILE_PATH);
	}

	private static JOPAPalette loadPalette(String filePath) {
		String text = loadTextFile(filePath);

		JsonElement element = new JsonParser().parse(text);
		JsonObject paletteObject = element.getAsJsonObject();

		JOPAPalette palette = new JOPAPalette();
		palette.backgroundColor = deserializeColor(paletteObject.get("backgroundColor"));
		palette.nodeColor = deserializeColor(paletteObject.get("nodeColor"));
		palette.selectedNodeColor = deserializeColor(paletteObject.get("selectedNodeColor"));
		palette.portColor = deserializeColor(paletteObject.get("portColor"));
		palette.selectedPortColor = deserializeColor(paletteObject.get("selectedPortColor"));
		palette.boolTypeColor = deserializeColor(paletteObject.get("boolTypeColor"));
		palette.intTypeColor = deserializeColor(paletteObject.get("intTypeColor"));
		palette.uintTypeColor = deserializeColor(paletteObject.get("uintTypeColor"));
		palette.floatTypeColor = deserializeColor(paletteObject.get("floatTypeColor"));
		palette.vectorTypeColor = deserializeColor(paletteObject.get("vectorTypeColor"));
		palette.matrixTypeColor = deserializeColor(paletteObject.get("matrixTypeColor"));
		palette.graphicsTypeColor = deserializeColor(paletteObject.get("graphicsTypeColor"));

		return palette;
	}

	private static void savePalette(JOPAPalette palette, String filePath) {
		JsonObject obj = new JsonObject();

		obj.add("backgroundColor", serializeObject(palette.backgroundColor));
		obj.add("nodeColor", serializeObject(palette.nodeColor));
		obj.add("selectedNodeColor", serializeObject(palette.selectedNodeColor));
		obj.add("portColor", serializeObject(palette.portColor));
		obj.add("selectedPortColor", serializeObject(palette.selectedPortColor));
		obj.add("boolTypeColor", serializeObject(palette.boolTypeColor));
		obj.add("intTypeColor", serializeObject(palette.intTypeColor));
		obj.add("uintTypeColor", serializeObject(palette.uintTypeColor));
		obj.add("floatTypeColor", serializeObject(palette.floatTypeColor));
		obj.add("vectorTypeColor", serializeObject(palette.vectorTypeColor));
		obj.add("matrixTypeColor", serializeObject(palette.matrixTypeColor));
		obj.add("graphicsTypeColor", serializeObject(palette.graphicsTypeColor));

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String text = gson.toJson(obj);

		saveTextFile(filePath, text);
	}

	private static JsonObject serializeObject(Color color) {
		JsonObject obj = new JsonObject();

		if (color != null) {
			obj.addProperty("red", color.getRed());
			obj.addProperty("green", color.getGreen());
			obj.addProperty("blue", color.getBlue());
		} else {
			obj.addProperty("red", 0);
			obj.addProperty("green", 0);
			obj.addProperty("blue", 0);
		}

		return obj;
	}

	private static Color deserializeColor(JsonElement elem) {
		int red;
		int green;
		int blue;

		if (elem != null) {
			JsonObject obj = elem.getAsJsonObject();

			red = obj.get("red").getAsInt();
			green = obj.get("green").getAsInt();
			blue = obj.get("blue").getAsInt();
		} else {
			red = 0;
			green = 0;
			blue = 0;
		}

		return new Color(red, green, blue, 255);
	}

}