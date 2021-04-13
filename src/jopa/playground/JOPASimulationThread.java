package jopa.playground;

public class JOPASimulationThread extends Thread {

	public static boolean isRunning;

	public JOPASimulationThread(JOPASimulationScript script) {
		super(() -> {
			isRunning = true;
			while (isRunning) {
//				System.out.println("simulation!!!");
				if (!script.execute()) {
//					System.out.println("oops");
					isRunning = false;
				}
			}
		});
	}

}