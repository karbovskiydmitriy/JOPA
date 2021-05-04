package jopa.main;

import java.awt.Rectangle;

import jopa.nodes.JOPANode;
import jopa.ui.JOPAUI;

public class JOPAMain {

	public static Object workspaceSync;
	public static JOPAWorkspace currentWorkspace;
	public static JOPASettings settings;

	static JOPAUI ui;

	public static void main(String[] args) {
		if (!checkVersion()) {
			// TODO gui

			return;
		}

		settings = new JOPASettings();

		workspaceSync = new Object();

		setupUI();
		createNewWorkspace();

		// currentWorkspace.createPlayground();
		// currentWorkspace.startPlayground();
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
			ui.createCanvas();
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

			} else {
				workspaceNotCreated();
			}
		}
	}

	public static void destroyWorkspace() {
		synchronized (workspaceSync) {
			// TODO
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
			} else {
				workspaceNotCreated();
			}
		}
	}

	public static void validateNodes() {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {
				if (currentWorkspace.currentFunction != null) {
					if (currentWorkspace.currentFunction.verifyNodes()) {
						ui.showMessage("Nodes in current function are OK");
					} else {
						ui.showMessage("Nodes in current function are not OK");
					}
				}
			} else {
				workspaceNotCreated();
			}
		}
	}

	public static void createNewFunction(String functionName) {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {
				if (functionName == null) {
					functionName = "function_" + (currentWorkspace.functions.size());
				}
				boolean openEditor = currentWorkspace.functions.size() > 0;
				ui.addFunction(currentWorkspace.createFunction(functionName), openEditor);
			} else {
				workspaceNotCreated();
			}
		}
	}

	public static void validateFunction() {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {
				if (currentWorkspace.currentFunction != null) {
					if (currentWorkspace.verifyFunction(currentWorkspace.currentFunction)) {
						ui.showMessage("functions passed validation");
					} else {
						ui.showMessage("function contains errors");
					}
				} else {
					ui.showMessage("Function not selected!");
				}
			} else {
				workspaceNotCreated();
			}
		}
	}

	public static void generateShader() {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {
				ui.notImplemented();
			} else {
				workspaceNotCreated();
			}
		}
	}
	
	public static void showShaderCode() {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {
				ui.notImplemented();
			} else {
				workspaceNotCreated();
			}
		}
	}

	public static void createPlayground() {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {
				currentWorkspace.createPlayground();
			} else {
				workspaceNotCreated();
			}
		}
	}

	public static void startPlayground() {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {
				currentWorkspace.startPlayground();
			} else {
				workspaceNotCreated();
			}
		}
	}

	public static void stopPlayground() {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {
				currentWorkspace.stopPlayground();
			} else {
				workspaceNotCreated();
			}
		}
	}

	public static void closePlayground() {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {
				currentWorkspace.closePlayground();
			} else {
				workspaceNotCreated();
			}
		}
	}

	public static void about() {
		ui.showMessage("JOPA");
	}

	public static void manual() {
		ui.showMessage("MANUAL");
	}

	private static void workspaceNotCreated() {
		ui.showMessage("Project not created!");
	}

}