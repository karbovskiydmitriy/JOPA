package jopa.io;

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

}