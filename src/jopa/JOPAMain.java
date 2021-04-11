package jopa;

import java.awt.Rectangle;

import jopa.nodes.JOPANode;
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
			ui.createWindow();
			ui.createMenu();
			ui.createTabs();
		}
	}

	public static void createNewWorkspace() {
		synchronized (workspaceSync) {
			currentWorkspace = new JOPAWorkspace("New workspace");
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
				JOPANode node = new JOPANode(new Rectangle(0, 0, 100, 100), "HEADER");
				currentWorkspace.currentFunction.statements.add(node);
			}
		}
	}

	public static void validateNodes() {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {
				if (currentWorkspace.currentFunction != null) {
					if (currentWorkspace.currentFunction.verifyNodes()) {
						// TODO gui
					}
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
				if (currentWorkspace.verifyFunctions()) {
					// TODO gui
				}
			}
		}
	}

	public static void generateShader() {
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