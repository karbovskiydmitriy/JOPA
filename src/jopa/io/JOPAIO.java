package jopa.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JOPAIO {

	public static String loadTextFile(String fileName) {
		try {
			return new String(Files.readAllBytes(Paths.get(fileName)));
		} catch (IOException e) {
			System.out.println(e.getMessage());
			// do sth with it?

			return null;
		}
	}

	public static boolean saveTextFile(String fileName, String text) {
		try {
			Files.write(Paths.get(fileName), text.getBytes());
		} catch (IOException e) {
			System.out.println(e.getMessage());

			return false;
		}

		return true;
	}

	public static byte[] loadBinaryFile(File file) {
		return null;
	}

	public static boolean saveBinaryFile(File file, byte[] data) {
		return false;
	}

}