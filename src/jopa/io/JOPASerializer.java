package jopa.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import jopa.main.JOPAMain;

public final class JOPASerializer {

	public static <T> byte[] serialize(T object) {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			try (ObjectOutputStream out = new ObjectOutputStream(bos)) {
				out.writeObject(object);
				out.flush();

				return bos.toByteArray();
			} catch (IOException e) {
				return null;
			}
		} catch (IOException e) {
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
				return null;
			}
		} catch (IOException e) {
			return null;
		}
	}

	public static <T> boolean saveToFile(String fileName, T object) {
		JOPAMain.ui.showMessage("TODO save");

		byte[] data = serialize(object);
		if (data != null) {
			return JOPAIO.saveBinaryFile(fileName, data);
		}

		return false;
	}

	public static <T> T readFromfile(String fileName) {
		JOPAMain.ui.showMessage("TODO load");

		byte[] data = JOPAIO.loadBinaryFile(fileName);
		if (data != null) {
			return deserialize(data);
		}

		return null;
	}

}