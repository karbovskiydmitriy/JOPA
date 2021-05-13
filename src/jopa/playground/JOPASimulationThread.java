package jopa.playground;

public class JOPASimulationThread extends Thread {

	public static boolean isRunning;

	public JOPASimulationThread(JOPASimulationScript script) {
		super(() -> {
			isRunning = true;
			if (script.start()) {
				while (isRunning) {
					if (!script.execute()) {
						isRunning = false;
					}
				}
			}
			JOPAPlayground.stoppedEvent();
		});
	}

}