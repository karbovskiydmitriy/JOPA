package jopa;

import java.awt.Rectangle;
import java.util.ArrayList;

import jopa.ui.JOPAUI;

public class JOPAMain {

	public static Object workspaceSync;
	public static JOPAWorkspace currentWorkspace;
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
			ui.createTabs();
		}
	}

	public static void createNewWorkspace() {
		synchronized (workspaceSync) {
			currentWorkspace = new JOPAWorkspace(ui, "New workspace");
			createNewFunction(null);
		}

		ui.repaint();
	}

	public static void openWorkspace() {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {

			}
		}
	}

	public static void saveWorkspace() {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {

			}
		}
	}

	public static void destroyWorkspace() {
		synchronized (workspaceSync) {
			currentWorkspace = null;
		}

		ui.repaint();
	}

	public static void quit() {
		synchronized (workspaceSync) {
			saveWorkspace();
			currentWorkspace = null;
			ui.close();
		}
	}

	public static void createNewNode() {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {
				JOPANode node = new JOPANode(new Rectangle(0, 0, 100, 100), "HEADER", "COMMAND", "FORMULA");
				currentWorkspace.currentFunction.statements.add(node);
			}
		}
	}

	public static void validateNodes() {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {
//				boolean isValid = true;

				ArrayList<JOPANode> inputs = new ArrayList<JOPANode>();
				ArrayList<JOPANode> statements = new ArrayList<JOPANode>();
				ArrayList<JOPANode> outputs = new ArrayList<JOPANode>();
				for (JOPANode node : currentWorkspace.currentFunction.statements) {
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

	public static void createNewFunction(String functionName) {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {
				if (functionName == null) {
					functionName = "function_" + (currentWorkspace.functions.size());
				}
				ui.addFunction(currentWorkspace.createFunction(functionName));
			}
		}
	}

	public static void validateFunction() {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {

			}
		}
	}

	public static void about() {

	}

	public static void manual() {

	}

}