package jopa.io;

import static jopa.io.JOPAIO.loadTextFile;

public final class JOPALoader {

	private static final String SHADERS_DIRECTORY_PATH = ".\\shaders\\";
	private static final String TEMPLATES_DIRECTORY_PATH = ".\\templates\\";

	public static String loadStandardShader(String name) {
		return loadTextFile(SHADERS_DIRECTORY_PATH + name);
	}

	public static String loadStandardTemplate(String name) {
		return loadTextFile(TEMPLATES_DIRECTORY_PATH + name);
	}

}