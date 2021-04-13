package jopa.playground;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import jopa.exceptions.JOPAPlaygroundException;

public class JOPASimulationScript {

	private ArrayList<String> commands;
	private Iterator<String> nextCommand;
	private JOPASimulationType simulationType;

	public JOPASimulationScript(JOPASimulationType simulationType) throws JOPAPlaygroundException {
		if (simulationType == null) {
			throw new JOPAPlaygroundException("simulation type is null");
		}
		if (simulationType == JOPASimulationType.NONE) {
			throw new JOPAPlaygroundException("simulation type is NONE");
		}

		this.simulationType = simulationType;
	}

	public JOPASimulationScript(Collection<String> commands) throws JOPAPlaygroundException {
		if (commands == null) {
			throw new JOPAPlaygroundException("commands is null");
		}

		this.commands = new ArrayList<>(commands);
		this.simulationType = JOPASimulationType.CUSTOM_SIMULATION;
	}

	public void reset() {
		nextCommand = commands.iterator();
	}

	public boolean execute() {
		switch (simulationType) {
		case NONE:
			return false;
		case FRAGMENT_SHADER_SIMULATION:
			executeSingleFragmentShaderSimulation();

			return true;
		case COMPUTE_SHADER_SIMULATION:
			executeSingleComputeShaderSimulation();

			return true;
		case CUSTOM_SIMULATION:
			if (nextCommand == null) {
				reset();

				return false;
			}

			String command = nextCommand.next();
			if (command != null && command.length() > 0) {
				if (command.contains(" ")) {
					String[] parts = command.split(" ");
					if (parts.length > 0) {
						
					}
				} else {

				}

			}

			return true;
		default:
			return false;
		}
	}

	private void executeSingleFragmentShaderSimulation() {
		// TODO copy from tests
	}

	private void executeSingleComputeShaderSimulation() {
		// TODO copy from tests
	}

}