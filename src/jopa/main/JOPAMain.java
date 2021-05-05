package jopa.main;

import jopa.nodes.JOPANode;
import jopa.nodes.JOPAStatementNode;
import jopa.playground.JOPASimulationType;
import jopa.ui.JOPAUI;

public class JOPAMain {

	private static final String TEST_PROJECT_NAME = ".\\projects\\test.jopa";

	public static Object workspaceSync;
	public static JOPAWorkspace currentWorkspace;
	public static JOPASettings settings;
	public static JOPAUI ui;

	public static void main(String[] args) {
		if (!checkVersion()) {
			// TODO gui

			return;
		}

		settings = new JOPASettings();

		workspaceSync = new Object();

		setupUI();
		createNewWorkspace();
	}

	private static boolean checkVersion() {
		return true; // TODO
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
			createNewFunction();
		}

		ui.repaint();
	}

	public static void openWorkspace() {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {
				// TODO
			}
			// TODO
			currentWorkspace = JOPAWorkspace.loadFromFile(TEST_PROJECT_NAME);
		}
	}

	public static void saveWorkspace() {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {
				// TODO
				JOPAWorkspace.saveToFile(TEST_PROJECT_NAME, currentWorkspace);
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

	public static void editProject() {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {
				// TODO
			} else {
				workspaceNotCreated();
			}
		}
	}

	public static void verifyProject() {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {
				if (currentWorkspace.verifyFunctions()) {
					ui.showMessage("project passed validation");
				} else {
					ui.showMessage("project contains errors");
				}
			} else {
				workspaceNotCreated();
			}
		}
	}

	public static void createNewFunction() {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {
				ui.addFunction(currentWorkspace.createFunction(null));
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
						ui.showMessage("function passed validation");
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

	public static void createNewNode() {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {
				JOPANode node = new JOPAStatementNode(0, 0, "STATEMENT", "HEADER");
				currentWorkspace.currentFunction.statementNodes.add(node);
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

	public static void generateShader() {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {
				currentWorkspace.generateShader();
			} else {
				workspaceNotCreated();
			}
		}
	}

	public static void showShaderCode() {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {
				currentWorkspace.showGeneratedShader();
			} else {
				workspaceNotCreated();
			}
		}
	}

	public static void createPlayground(JOPASimulationType type) {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {
				currentWorkspace.createPlayground(type);
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