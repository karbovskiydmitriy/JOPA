package jopa.playground;

import java.io.Closeable;
import java.util.ArrayList;

import jopa.main.JOPAMain;
import jopa.types.JOPAResource;

public class JOPAPlayground implements Closeable {

	private JOPASimulationType simulationType;
	// private JOPASimulationScript script;
	private JOPASimulationThread simulationThread;

	private ArrayList<JOPAResource> resources;

	private JOPAPlayground(JOPASimulationType type) {
		this.simulationType = type;
		resources = new ArrayList<JOPAResource>();
		System.out.println("Playground created");
	}

	public static JOPAPlayground create(JOPASimulationType type) {
		if (type == JOPASimulationType.COMPUTE) {
			if (!JOPAMain.system.checkVersion()) {
				JOPAMain.ui.showMessage("You system does not support compute shaders!");

				return null;
			}
		}

		return new JOPAPlayground(type);
	}

	public synchronized void setupScript(JOPASimulationScript script) {
		JOPAMain.currentProject.script = script;
	}

	public synchronized void start() {
		if (simulationThread == null) {
			// if (script == null) {
			// JOPASimulationScript stubScript =
			// JOPASimulationScript.create(simulationType);
			// System.out.println(stubScript);
			// setupScript(stubScript);
			// }
			if (JOPAMain.currentProject.script == null) {
				System.out.println("Script not set up");
				// TODO error handling?

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
		resources.clear();
		System.out.println("Playground closed");
	}

}