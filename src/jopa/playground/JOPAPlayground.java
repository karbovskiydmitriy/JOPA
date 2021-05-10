package jopa.playground;

import java.io.Closeable;

import jopa.main.JOPAMain;

public class JOPAPlayground implements Closeable {

	// private JOPASimulationScript script;
	private JOPASimulationThread simulationThread;

//	private ArrayList<JOPAResource> resources;

	private JOPAPlayground() {
		// this.simulationType = type;
//		resources = new ArrayList<JOPAResource>();
		System.out.println("Playground created");
	}

	public static JOPAPlayground create(JOPASimulationType type) {
		if (type == JOPASimulationType.COMPUTE) {
			if (!JOPAMain.system.checkVersion()) {
				JOPAMain.ui.showMessage("You system does not support compute shaders!");

				return null;
			}
		}

		return new JOPAPlayground();
	}

	public synchronized void start() {
		if (simulationThread == null) {
			if (JOPAMain.currentProject.script == null) {
				System.out.println("Script not set up");
				// DECIDE error handling?

				return;
			}
			System.out.println("Playground started");

			simulationThread = new JOPASimulationThread(JOPAMain.currentProject.script);
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
//		resources.clear();
		System.out.println("Playground closed");
	}

}