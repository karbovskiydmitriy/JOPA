package jopa.playground;

import java.io.Closeable;
import java.util.ArrayList;

import jopa.types.JOPAResource;

public class JOPAPlayground implements Closeable {

	private boolean isRunning;
	private JOPASimulationScript script;

	private ArrayList<JOPAResource> resources;

	public JOPAPlayground() {
		resources = new ArrayList<JOPAResource>();
		System.out.println("Playground created");
		// TODO
	}

	public synchronized void setupScript(JOPASimulationScript script) {
		this.script = script;
	}

	public synchronized void start() {
		if (!isRunning) {
			isRunning = true;
			System.out.println("Playground started");
			if (script == null) {
				System.out.println("Script not set up");
				// TODO

				return;
			}
			// TODO
		}
	}

	public synchronized void stop() {
		if (isRunning) {
			isRunning = false;
			System.out.println("Playground stopped");
			// TODO
		}
	}

	@Override
	public synchronized void close() {
		resources.clear();
		System.out.println("Playground closed");
		// TODO
	}

}