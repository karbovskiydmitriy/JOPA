package JOPA;

import java.awt.Rectangle;
import java.util.ArrayList;

public class JOPAMain {

	static Object workspaceSync;
	static JOPAWorkspace currentWorkspace;
	static JOPAUI ui;

	public static void main(String[] args) {
		if (!checkVersion()) {
			return;
		}

		workspaceSync = new Object();

		setupUI();
		createNewWorkspace();
	}

	private static boolean checkVersion() {

		return true;
	}

	private static void setupUI() {
		if (ui == null) {
			ui = new JOPAUI();
			ui.setupWindow();
			ui.createMenu();
		}
	}

	static void createNewWorkspace() {
		synchronized (workspaceSync) {
			currentWorkspace = new JOPAWorkspace(ui, "New workspace");
			createNewFunction(null);
		}

		ui.repaint();
	}

	static void openWorkspace() {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {

			}
		}
	}

	static void saveWorkspace() {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {

			}
		}
	}

	static void destroyWorkspace() {
		synchronized (workspaceSync) {
			currentWorkspace = null;
		}

		ui.repaint();
	}

	static void quit() {
		synchronized (workspaceSync) {
			saveWorkspace();
			currentWorkspace = null;
			ui.close();
		}
	}

	static void createNewNode() {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {
				JOPANode node = new JOPANode(new Rectangle(0, 0, 100, 100), "HEADER", "COMMAND", "FORMULA");
				currentWorkspace.currentFunction.nodes.add(node);
			}
		}
	}

	static void validateNodes() {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {
				boolean isValid = true;

				ArrayList<JOPANode> inputs = new ArrayList<JOPANode>();
				ArrayList<JOPANode> statements = new ArrayList<JOPANode>();
				ArrayList<JOPANode> outputs = new ArrayList<JOPANode>();
				for (JOPANode node : currentWorkspace.currentFunction.nodes) {
					if (node.inputs.size() == 0 && node.outputs.size() == 0) {
						// skip
					}
					if (node.inputs.size() == 0) {
						outputs.add(node);
						
						continue;
					}
					if (node.outputs.size() == 0) {
						inputs.add(node);
						
						continue;
					}
					statements.add(node);
				}
			}
		}
	}

	static void createNewFunction(String functionName) {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {
				if (functionName == null) {
					functionName = "function_" + (currentWorkspace.functions.size());
				}
				ui.addFunction(currentWorkspace.createFunction(functionName));
			}
		}
	}

	static void validateFunction() {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {

			}
		}
	}

	static void about() {

	}

	static void manual() {

	}

}