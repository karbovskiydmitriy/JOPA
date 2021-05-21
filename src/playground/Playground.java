package playground;

import static app.Main.currentProject;

import java.io.Closeable;

public class Playground implements Closeable {

	private SimulationThread simulationThread;

	public Playground() {
		System.out.println("[PLAYGROUND] Playground created");
	}

	public synchronized void start() {
		if (simulationThread == null) {
			if (currentProject.script == null) {
				System.err.println("[PLAYGROUND] Script not set up");

				return;
			}
			System.out.println("[PLAYGROUND] Playground started");

			simulationThread = new SimulationThread(currentProject.script);
			simulationThread.start();
		}
	}

	public synchronized void stop() {
		if (simulationThread != null) {
			SimulationThread.isRunning = false;
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