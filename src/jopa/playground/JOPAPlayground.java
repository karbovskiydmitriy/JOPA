package jopa.playground;

import java.io.Closeable;
import java.util.ArrayList;

import jopa.exceptions.JOPAPlaygroundException;
import jopa.types.JOPAResource;

public class JOPAPlayground implements Closeable {

	private JOPASimulationScript script;
	private JOPASimulationThread simulationThread;

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
		if (simulationThread == null) {
			try {
				setupScript(new JOPASimulationScript(JOPASimulationType.FRAGMENT_SHADER_SIMULATION));
			} catch (JOPAPlaygroundException e) {
				e.printStackTrace();
			}
			if (script == null) {
				System.out.println("Script not set up");
				// TODO

				return;
			}
			System.out.println("Playground started");

			simulationThread = new JOPASimulationThread(script);
			simulationThread.start();
		}
	}

	public synchronized void stop() {
		if (simulationThread != null) {
			JOPASimulationThread.isRunning = false;
			simulationThread = null;
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