package jopa.playground;

public class JOPASimulationThread extends Thread {

	public static boolean isRunning;

	public JOPASimulationThread(JOPASimulationScript script) {
		super(() -> {
			isRunning = true;
			while (isRunning) {
				if (!script.execute()) {
					isRunning = false;
				}
			}
			System.out.println("Playground stopped");
		});
	}

}