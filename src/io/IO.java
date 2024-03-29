package io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import graphics.Image;

public class IO {

	public static String loadTextFile(String fileName) {
		try {
			return new String(Files.readAllBytes(Paths.get(fileName)));
		} catch (IOException e) {
			System.err.println(e.getMessage());

			return null;
		}
	}

	public static boolean saveTextFile(String fileName, String text) {
		try {
			Files.write(Paths.get(fileName), text.getBytes());
		} catch (IOException e) {
			System.err.println(e.getMessage());

			return false;
		}

		return true;
	}

	public static byte[] loadBinaryFile(File file) {
		if (file != null && file.exists()) {
			Path path = file.toPath();
			if (path != null) {
				try {
					return Files.readAllBytes(path);
				} catch (IOException e) {
					System.err.println(e.getMessage());

					return null;
				}
			}
		}

		return null;
	}

	public static boolean saveBinaryFile(File file, byte[] data) {
		if (file != null) {
			if (data != null && data.length > 0) {
				Path path = file.toPath();
				try {
					Files.write(path, data);

					return true;
				} catch (IOException e) {
					System.err.println(e.getMessage());

					return false;
				}
			}
		}

		return false;
	}

	public static Image loadImage(String fileName) {
		Image image = new Image(fileName);
		if (image.handle != 0) {
			return image;
		}

		return null;
	}

}