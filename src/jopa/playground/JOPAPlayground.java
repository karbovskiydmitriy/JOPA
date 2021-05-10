package jopa.playground;

import static jopa.main.JOPAMain.currentProject;
import static jopa.main.JOPAMain.gui;
import static jopa.main.JOPAMain.system;

import java.io.Closeable;

public class JOPAPlayground implements Closeable {

	private JOPASimulationThread simulationThread;

	private JOPAPlayground() {
		System.out.println("Playground created");
	}

	public static JOPAPlayground create(JOPASimulationType type) {
		if (type == JOPASimulationType.COMPUTE) {
			if (!system.checkVersion()) {
				gui.showMessage("You system does not support compute shaders!");

				return null;
			}
		}
		currentProject.script = JOPASimulationScript.create(type);

		return new JOPAPlayground();
	}

	public synchronized void start() {
		if (simulationThread == null) {
			if (currentProject.script == null) {
				System.out.println("Script not set up");
				// DECIDE error handling?

				return;
			}
			System.out.println("Playground started");

			simulationThread = new JOPASimulationThread(currentProject.script);
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
		System.out.println("Playground closed");
	}

}