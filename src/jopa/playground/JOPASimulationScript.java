package jopa.playground;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class JOPASimulationScript {

	private final ArrayList<String> commands;
	private Iterator<String> nextCommand;

	public JOPASimulationScript(Collection<String> commands) {
		this.commands = new ArrayList<>(commands);
	}

	public void reset() {
		nextCommand = commands.iterator();
	}

	public boolean execute() {
		if (nextCommand == null) {
			reset();

			return false;
		}

		String command = nextCommand.next();
		if (command != null && command.length() > 0) {
			String[] parts = command.split(" ");
		}

		return true;
	}

}