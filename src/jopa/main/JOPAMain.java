package jopa.main;

import java.io.File;

import javax.swing.filechooser.FileNameExtensionFilter;

import jopa.nodes.JOPANode;
import jopa.nodes.JOPAStatementNode;
import jopa.playground.JOPASimulationType;
import jopa.ui.JOPAUI;

public class JOPAMain {

	private static final String OPENGL_VERSION_WARNING = "Your system does not support OpenGL 4.3, required for compute shaders!";
	private static final String SYSTEM_NOT_SUPPORTED = "Your system is not officially supported!";
	private static final String TEST_PROJECT_NAME = ".\\projects\\";

	private static FileNameExtensionFilter projectFileFilter;
	private static JOPASystem system;
	private static Object projectSync;

	public static JOPAProject currentProject;
	public static JOPASettings settings;
	public static JOPAUI ui;

	public static void main(String[] args) {
		init();

		settings = new JOPASettings();
		projectSync = new Object();
		projectFileFilter = new FileNameExtensionFilter("JOPA project file", ".jopa");

		createNewWorkspace();
	}

	private static void init() {
		system = JOPASystem.init();

		setupUI();

		if (!system.checkSystem()) {
			ui.showMessage(SYSTEM_NOT_SUPPORTED);
		}

		if (!system.checkVersion()) {
			ui.showMessage(OPENGL_VERSION_WARNING);
		}
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
		synchronized (projectSync) {
			currentProject = new JOPAProject("New workspace", JOPAProjectType.FRAGMENT);
			createNewFunction();
		}

		ui.repaint();
	}

	public static boolean openWorkspace() {
		synchronized (projectSync) {
			if (currentProject != null) {
				// TODO ask
			}
			File selectedFile = ui.showFileDialog(TEST_PROJECT_NAME, projectFileFilter, null, false);
			if (selectedFile != null) {
				currentProject = JOPAProject.loadFromFile(selectedFile);

				return true;
			}
		}

		return false;
	}

	public static boolean saveWorkspace() {
		synchronized (projectSync) {
			if (currentProject != null) {
				File selectedFile = ui.showFileDialog(TEST_PROJECT_NAME, projectFileFilter, null, true);
				if (selectedFile != null) {
					if (JOPAProject.saveToFile(selectedFile, currentProject)) {
						return true;
					}
				}
			} else {
				workspaceNotCreated();
			}

			return false;
		}
	}

	public static void destroyWorkspace() {
		synchronized (projectSync) {
			currentProject = null;
			ui.closeProject();
		}

		ui.repaint();
	}

	public static void quit() {
		synchronized (projectSync) {
			saveWorkspace();
			currentProject = null;
			ui.close();
		}
	}

	public static void editProject() {
		synchronized (projectSync) {
			if (currentProject != null) {
				// TODO edition GUI
			} else {
				workspaceNotCreated();
			}
		}
	}

	public static void verifyProject() {
		synchronized (projectSync) {
			if (currentProject != null) {
				if (currentProject.verifyFunctions()) {
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
		synchronized (projectSync) {
			if (currentProject != null) {
				ui.addFunction(currentProject.createFunction(null));
			} else {
				workspaceNotCreated();
			}
		}
	}

	public static void validateFunction() {
		synchronized (projectSync) {
			if (currentProject != null) {
				if (currentProject.currentFunction != null) {
					if (currentProject.verifyFunction(currentProject.currentFunction)) {
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
		synchronized (projectSync) {
			if (currentProject != null) {
				JOPANode node = new JOPAStatementNode(0, 0, "STATEMENT", "HEADER");
				currentProject.currentFunction.statementNodes.add(node);
			} else {
				workspaceNotCreated();
			}
		}
	}

	public static void validateNodes() {
		synchronized (projectSync) {
			if (currentProject != null) {
				if (currentProject.currentFunction != null) {
					if (currentProject.currentFunction.verifyNodes()) {
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
		synchronized (projectSync) {
			if (currentProject != null) {
				currentProject.generateShader();
			} else {
				workspaceNotCreated();
			}
		}
	}

	public static void showShaderCode() {
		synchronized (projectSync) {
			if (currentProject != null) {
				currentProject.showGeneratedShader();
			} else {
				workspaceNotCreated();
			}
		}
	}

	public static void createPlayground(JOPASimulationType type) {
		synchronized (projectSync) {
			if (currentProject != null) {
				currentProject.createPlayground(type);
			} else {
				workspaceNotCreated();
			}
		}
	}

	public static void startPlayground() {
		synchronized (projectSync) {
			if (currentProject != null) {
				currentProject.startPlayground();
			} else {
				workspaceNotCreated();
			}
		}
	}

	public static void stopPlayground() {
		synchronized (projectSync) {
			if (currentProject != null) {
				currentProject.stopPlayground();
			} else {
				workspaceNotCreated();
			}
		}
	}

	public static void closePlayground() {
		synchronized (projectSync) {
			if (currentProject != null) {
				currentProject.closePlayground();
			} else {
				workspaceNotCreated();
			}
		}
	}

	private static void workspaceNotCreated() {
		ui.showMessage("Project not created!");
	}

	public static void about() {
		ui.showMessage("JOPA");
	}

	public static void manual() {
		ui.showMessage("MANUAL");
	}

}