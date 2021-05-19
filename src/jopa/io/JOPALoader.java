package jopa.io;

import static jopa.io.JOPAIO.loadImage;
import static jopa.io.JOPAIO.loadTextFile;

import jopa.graphics.JOPAImage;

public final class JOPALoader {

	private static final String SHADERS_DIRECTORY_PATH = ".\\shaders\\";
	private static final String TEMPLATES_DIRECTORY_PATH = ".\\templates\\";
	private static final String SCRIPTS_DIRECTORY_PATH = ".\\scripts\\";
	private static final String IMAGES_DIRECTORY_PATH = ".\\images\\";

	public static String loadStandardShader(String name) {
		// System.out.println("[LOADER] Loading shader: " + name);

		return loadTextFile(SHADERS_DIRECTORY_PATH + name);
	}

	public static String loadStandardTemplate(String name) {
		// System.out.println("[LOADER] Loading template: " + name);

		return loadTextFile(TEMPLATES_DIRECTORY_PATH + name);
	}

	public static String loadStandardScript(String name) {
		// System.out.println("[LOADER] Loading script: " + name);

		return loadTextFile(SCRIPTS_DIRECTORY_PATH + name);
	}

	public static JOPAImage loadStandardImage(String name) {
		// System.out.println("[LOADER] Loading image: " + name);

		return loadImage(IMAGES_DIRECTORY_PATH + name);
	}

}