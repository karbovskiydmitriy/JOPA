package playground;

public class SimulationThread extends Thread {

	public static boolean isRunning;

	public SimulationThread(SimulationScript script) {
		super(() -> {
			isRunning = true;
			if (script.start()) {
				while (isRunning) {
					if (!script.execute()) {
						isRunning = false;
					}
				}
			}
			Playground.stoppedEvent();
		});
	}

}