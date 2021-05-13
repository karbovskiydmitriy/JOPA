package jopa.playground;

import static jopa.main.JOPAMain.currentProject;

import java.io.Closeable;

public class JOPAPlayground implements Closeable {

	private JOPASimulationThread simulationThread;

	public JOPAPlayground() {
		System.out.println("[PLAYGROUND] Playground created");
	}

	public synchronized void start() {
		if (simulationThread == null) {
			if (currentProject.script == null) {
				System.err.println("[PLAYGROUND] Script not set up");

				return;
			}
			System.out.println("[PLAYGROUND] Playground started");

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

	public static void stoppedEvent() {
		System.out.println("[PLAYGROUND] Playground stopped");
	}

	@Override
	public synchronized void close() {
		System.out.println("[PLAYGROUND] Playground closed");
	}

}