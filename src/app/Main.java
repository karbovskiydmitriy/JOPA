package app;

import static io.IO.loadTextFile;
import static util.OGLUtil.validateShader;

import java.io.File;

import javax.swing.filechooser.FileNameExtensionFilter;

import io.IO;
import nodes.BranchNode;
import nodes.FunctionNode;
import nodes.LoopNode;
import nodes.Node;
import nodes.StatementNode;
import types.ProjectType;
import ui.UI;

public class Main {

	private static final String SYSTEM_NOT_SUPPORTED = "Your system is not officially supported!";
	private static final String OPENGL_VERSION_WARNING = "Your system does not support OpenGL 4.3, required for compute shaders!";
	private static final String PROJECTS_DIRECTORY = ".\\projects\\";

	private static FileNameExtensionFilter projectFileFilter;
	private static FileNameExtensionFilter shaderFileFilter;
	private static Object projectSync;
	private static String helpString;
	private static String manualString;

	public static SystemInfo system;
	public static Project currentProject;
	public static Settings settings;
	public static UI gui;

	public static void main(String[] args) {
		projectSync = new Object();

		init();

		settings = new Settings();
		projectFileFilter = new FileNameExtensionFilter("Project file", "project");
		shaderFileFilter = new FileNameExtensionFilter("GLSL shader file", "glsl");

		createNewProject();
	}

	private static void init() {
		gui = new UI();
		system = SystemInfo.init();

		if (!system.checkSystem()) {
			gui.showMessage(SYSTEM_NOT_SUPPORTED);
		}

		if (!system.checkVersion()) {
			gui.showMessage(OPENGL_VERSION_WARNING);
		}

		helpString = loadTextFile("about.txt");
		manualString = loadTextFile("manual.txt");

		setupUI();
	}

	private static void setupUI() {
		gui.createWindow();
		gui.createMenu();
		gui.createTabs();
	}

	public static void createNewProject() {
		synchronized (projectSync) {
			gui.closeProjectTabs();
			currentProject = new Project("New project", ProjectType.FRAGMENT);
			createNewFunction();
			createPlayground(currentProject.projectType);
		}

		gui.repaint();
	}

	public static boolean openProject() {
		synchronized (projectSync) {
			if (currentProject != null) {
				askAsboutSavingTheProject();
			}
			File selectedFile = gui.showFileDialog(PROJECTS_DIRECTORY, projectFileFilter, null, false);
			if (selectedFile != null) {
				currentProject = Project.loadFromFile(selectedFile);

				return true;
			}
		}

		return false;
	}

	public static boolean saveProject() {
		synchronized (projectSync) {
			if (currentProject != null) {
				File selectedFile = gui.showFileDialog(PROJECTS_DIRECTORY, projectFileFilter, null, true);
				if (selectedFile != null) {
					if (Project.saveToFile(selectedFile, currentProject)) {
						return true;
					}
				}
			} else {
				projectNotCreated();
			}

			return false;
		}
	}

	public static void closeProject() {
		synchronized (projectSync) {
			currentProject = null;
			gui.closeProjectTabs();
		}
	}

	public static void quit() {
		synchronized (projectSync) {
			askAsboutSavingTheProject();
			currentProject = null;
			gui.close();
		}
	}

	public static void editProject() {
		synchronized (projectSync) {
			if (currentProject != null) {
				gui.openProjectEditor(currentProject);
			} else {
				projectNotCreated();
			}
		}
	}

	public static void verifyProject() {
		synchronized (projectSync) {
			if (currentProject != null) {
				if (currentProject.verifyFunctions()) {
					gui.showMessage("project passed validation");
				} else {
					gui.showMessage("project contains errors");
				}
			} else {
				projectNotCreated();
			}
		}
	}

	public static void createNewFunction() {
		synchronized (projectSync) {
			if (currentProject != null) {
				boolean isMain = currentProject.mainFunction == null;
				Function function = currentProject.createFunction(null, isMain);
				gui.addFunction(function);
			} else {
				projectNotCreated();
			}
		}
	}

	public static void editCurrentFunction() {
		synchronized (projectSync) {
			if (currentProject != null) {
				if (currentProject.currentFunction != currentProject.mainFunction) {
					gui.openFunctionEditor(currentProject.currentFunction);
				} else {
					gui.showMessage("you can not edit main function");
				}
			} else {
				projectNotCreated();
			}
		}
	}

	public static void showFunctionList() {
		synchronized (projectSync) {
			if (currentProject != null) {
				gui.showFunctionsList(currentProject);
			}
		}
	}

	public static void validateFunction() {
		synchronized (projectSync) {
			if (currentProject != null) {
				if (currentProject.currentFunction != null) {
					if (currentProject.verifyFunction(currentProject.currentFunction)) {
						gui.showMessage("function passed validation");
					} else {
						gui.showMessage("function contains errors");
					}
				} else {
					gui.showMessage("unction not selected!");
				}
			} else {
				projectNotCreated();
			}
		}
	}

	public static void createNewNode(Class<?> type) {
		synchronized (projectSync) {
			if (currentProject != null) {
				Node node = null;
				if (type.equals(StatementNode.class)) {
					node = new StatementNode(currentProject.currentFunction, 0, 0, "STATEMENT", "HEADER");
				} else if (type.equals(FunctionNode.class)) {
					node = new FunctionNode(currentProject.currentFunction, 0, 0, null);
				} else if (type.equals(BranchNode.class)) {
					node = new BranchNode(currentProject.currentFunction, 0, 0);
				} else if (type.equals(LoopNode.class)) {
					node = new LoopNode(currentProject.currentFunction, 0, 0, null);
				}
				if (node != null) {
					currentProject.currentFunction.statementNodes.add(node);
				}
				gui.repaint();
			} else {
				projectNotCreated();
			}
		}
	}

	public static void validateNodes() {
		synchronized (projectSync) {
			if (currentProject != null) {
				if (currentProject.currentFunction != null) {
					if (currentProject.currentFunction.verifyNodes()) {
						gui.showMessage("Nodes in current function are OK");
					} else {
						gui.showMessage("Nodes in current function are not OK");
					}
				}
			} else {
				projectNotCreated();
			}
		}
	}

	public static void generateShader() {
		synchronized (projectSync) {
			if (currentProject != null) {
				if (!currentProject.generateShader()) {
					gui.showMessage("Shader contains errors");
				}
			} else {
				projectNotCreated();
			}
		}
	}

	public static void showShaderCode() {
		synchronized (projectSync) {
			if (currentProject != null) {
				currentProject.showGeneratedShader();
			} else {
				projectNotCreated();
			}
		}
	}

	public static void validateGeneratedShader() {
		synchronized (projectSync) {
			if (validateShader(currentProject.getGeneratedShader(), currentProject.projectType)) {
				gui.showMessage("Shader is OK");
			} else {
				gui.showMessage("Shader contains errors");
			}
		}
	}

	public static void saveGeneratedShader() {
		synchronized (projectSync) {
			String shaderCode = currentProject.getGeneratedShader();
			File file = gui.showFileDialog(null, shaderFileFilter, "Save generated shader", true);
			if (file != null) {
				IO.saveTextFile(file.getAbsolutePath(), shaderCode);
			}
		}
	}

	public static void toggleShowingPortTypes() {
		synchronized (projectSync) {
			settings.showPortTypes = !settings.showPortTypes;
			gui.repaint();
		}
	}

	public static void toggleHighlightingNodes() {
		synchronized (projectSync) {
			settings.highlightNodes = !settings.highlightNodes;
			gui.repaint();
		}
	}

	public static void saveSettings() {
		synchronized (projectSync) {
			settings.writeSettings();
		}
	}

	public static void createPlayground(ProjectType type) {
		synchronized (projectSync) {
			if (currentProject != null) {
				currentProject.isCustom = false;
				currentProject.createPlayground(type);
			} else {
				projectNotCreated();
			}
		}
	}

	public static void createCustomPlayground(ProjectType type) {
		synchronized (projectSync) {
			if (currentProject != null) {
				currentProject.isCustom = true;
				currentProject.createPlayground(type);
			} else {
				projectNotCreated();
			}
		}
	}

	public static void editScript() {
		synchronized (projectSync) {
			if (currentProject != null) {
				gui.openScriptEditor(currentProject.script);
			} else {
				projectNotCreated();
			}
		}
	}

	public static void startPlayground() {
		synchronized (projectSync) {
			if (currentProject != null) {
				currentProject.startPlayground();
			} else {
				projectNotCreated();
			}
		}
	}

	public static void stopPlayground() {
		synchronized (projectSync) {
			if (currentProject != null) {
				currentProject.stopPlayground();
			} else {
				projectNotCreated();
			}
		}
	}

	public static void closePlayground() {
		synchronized (projectSync) {
			if (currentProject != null) {
				currentProject.closePlayground();
			} else {
				projectNotCreated();
			}
		}
	}

	private static void projectNotCreated() {
		gui.showMessage("Project not created!");
	}

	private static void askAsboutSavingTheProject() {
		if (gui.showQuestion("Save project?")) {
			saveProject();
		}
	}

	public static void about() {
		gui.showMessage(helpString);
	}

	public static void manual() {
		gui.showMessage(manualString);
	}

}