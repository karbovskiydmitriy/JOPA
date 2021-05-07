package jopa.playground;

import java.io.Closeable;
import java.util.ArrayList;

import jopa.exceptions.JOPAPlaygroundException;
import jopa.types.JOPAResource;

public class JOPAPlayground implements Closeable {

	private JOPASimulationType simulationType;
	private JOPASimulationScript script;
	private JOPASimulationThread simulationThread;

	private ArrayList<JOPAResource> resources;

	public JOPAPlayground(JOPASimulationType type) {
		this.simulationType = type;
		resources = new ArrayList<JOPAResource>();
		System.out.println("Playground created");
		// TODO playground init?
	}

	public synchronized void setupScript(JOPASimulationScript script) {
		this.script = script;
	}

	public synchronized void start() {
		if (simulationThread == null) {
			try {
				JOPASimulationScript script = new JOPASimulationScript(simulationType);
				setupScript(script);
			} catch (JOPAPlaygroundException e) {
				e.printStackTrace();
			}
			if (script == null) {
				System.out.println("Script not set up");
				// TODO error handling?

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
		}
	}

	@Override
	public synchronized void close() {
		resources.clear();
		System.out.println("Playground closed");
	}

}