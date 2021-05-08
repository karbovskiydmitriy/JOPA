package jopa.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class JOPASerializer {

	public static <T> byte[] serialize(T object) {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			try (ObjectOutputStream out = new ObjectOutputStream(bos)) {
				out.writeObject(object);
				out.flush();

				return bos.toByteArray();
			} catch (IOException e) {
				System.err.println(e.getMessage());

				return null;
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());

			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T deserialize(byte[] data) {
		try (ByteArrayInputStream bis = new ByteArrayInputStream(data)) {
			try (ObjectInput in = new ObjectInputStream(bis)) {
				T object = (T) in.readObject();

				return object;
			} catch (IOException | ClassNotFoundException e) {
				System.err.println(e.getMessage());

				return null;
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());

			return null;
		}
	}

	public static <T> boolean saveToFile(File file, T object) {
		byte[] data = serialize(object);
		if (data != null) {
			return JOPAIO.saveBinaryFile(file, data);
		}

		return false;
	}

	public static <T> T readFromfile(File file) {
		byte[] data = JOPAIO.loadBinaryFile(file);
		if (data != null) {
			return deserialize(data);
		}

		return null;
	}

}