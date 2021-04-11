package jopa.playground;

import java.io.Closeable;

public class JOPAPlayground implements Closeable {

	private boolean isRunning;

	public JOPAPlayground() {
		System.out.println("Playground created");
		// TODO
	}

	public void setupScript(String script) {
		String[] lines = script.split("\r\n");
		for (String line : lines) {
			System.out.println(line);
		}
	}

	public void start() {
		if (!isRunning) {
			isRunning = true;
			System.out.println("Playground started");
			// TODO
		}
	}

	public void stop() {
		if (isRunning) {
			isRunning = false;
			System.out.println("Playground stopped");
			// TODO
		}
	}

	@Override
	public void close() {
		System.out.println("Playground closed");
		// TODO
	}

}