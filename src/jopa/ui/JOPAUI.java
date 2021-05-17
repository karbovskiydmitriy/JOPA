package jopa.ui;

import static jopa.main.JOPAMain.about;
import static jopa.main.JOPAMain.closePlayground;
import static jopa.main.JOPAMain.closeProject;
import static jopa.main.JOPAMain.createCustomPlayground;
import static jopa.main.JOPAMain.createNewFunction;
import static jopa.main.JOPAMain.createNewNode;
import static jopa.main.JOPAMain.createNewProject;
import static jopa.main.JOPAMain.createPlayground;
import static jopa.main.JOPAMain.currentProject;
import static jopa.main.JOPAMain.editCurrentFunction;
import static jopa.main.JOPAMain.editProject;
import static jopa.main.JOPAMain.editScript;
import static jopa.main.JOPAMain.generateShader;
import static jopa.main.JOPAMain.manual;
import static jopa.main.JOPAMain.openProject;
import static jopa.main.JOPAMain.quit;
import static jopa.main.JOPAMain.saveProject;
import static jopa.main.JOPAMain.saveSettings;
import static jopa.main.JOPAMain.showFunctionList;
import static jopa.main.JOPAMain.showShaderCode;
import static jopa.main.JOPAMain.startPlayground;
import static jopa.main.JOPAMain.stopPlayground;
import static jopa.main.JOPAMain.toggleHighlightingNodes;
import static jopa.main.JOPAMain.toggleShowingPortTypes;
import static jopa.main.JOPAMain.validateFunction;
import static jopa.main.JOPAMain.validateGeneratedShader;
import static jopa.main.JOPAMain.validateNodes;
import static jopa.main.JOPAMain.verifyProject;
import static jopa.util.JOPAOGLUtil.getScreenSize;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import jopa.graphics.JOPACanvas;
import jopa.main.JOPAFunction;
import jopa.main.JOPATemplate;
import jopa.main.JOPAProject;
import jopa.nodes.JOPABranchNode;
import jopa.nodes.JOPAFunctionNode;
import jopa.nodes.JOPALoopNode;
import jopa.nodes.JOPAStatementNode;
import jopa.playground.JOPASimulationScript;
import jopa.types.JOPACustomType;
import jopa.types.JOPAProjectType;
import jopa.ui.dialogs.JOPAEditBranchNodeDialog;
import jopa.ui.dialogs.JOPAEditConstantsDialog;
import jopa.ui.dialogs.JOPAEditDefinesDialog;
import jopa.ui.dialogs.JOPAEditFunctionDialog;
import jopa.ui.dialogs.JOPAEditFunctionNodeDialog;
import jopa.ui.dialogs.JOPAEditGlobalsDialog;
import jopa.ui.dialogs.JOPAEditLoopNodeDialog;
import jopa.ui.dialogs.JOPAEditProjectDialog;
import jopa.ui.dialogs.JOPAEditResourcesDialog;
import jopa.ui.dialogs.JOPAEditScriptDialog;
import jopa.ui.dialogs.JOPAEditStatementNodeDialog;
import jopa.ui.dialogs.JOPAEditTemplateDialog;
import jopa.ui.dialogs.JOPAEditTypeDialog;
import jopa.ui.dialogs.JOPAEditTypesListDialog;
import jopa.ui.dialogs.JOPAFindTemplateDialog;
import jopa.ui.dialogs.JOPAMessageDialog;
import jopa.ui.dialogs.JOPAQuestionDialog;
import jopa.ui.dialogs.JOPAEditFunctionListDialog;
import jopa.ui.dialogs.JOPAShowShaderDialog;
import jopa.ui.dialogs.JOPAShowTemplateListDialog;

public class JOPAUI {

	private JFrame window;
	private MenuBar menuBar;
	private JOPAMenuPanel menuPanel;
	private JTabbedPane tabs;
	private HashMap<String, JOPACanvas> canvases;
	private JOPACanvas currentCanvas;

	public JOPAUI() {
		canvases = new HashMap<String, JOPACanvas>(1);
	}

	public synchronized void createWindow() {
		String title = "Java and OpenGL parallel algorithms application (JOPA) v1.0 by Karbovskiy Dmitriy (2020-2021)";
		window = new JFrame(title);
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				windowEvent.getWindow().dispose();
			}
		});

		int[] screenSize = getScreenSize();
		window.setSize(screenSize[0], screenSize[1]);
		window.setEnabled(true);
		window.setVisible(true);
		window.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println("[UI] KeyPressed event: " + e);
				currentProject.keyPressed(e.getKeyCode());
			}
		});

		JPanel contentPane = new JPanel(new BorderLayout(), true);
		window.add(contentPane);
		window.setContentPane(contentPane);
	}

	public synchronized void createMenu() {
		if (menuBar == null) {
			menuBar = new MenuBar();

			{
				Menu fileMenu = new Menu("file");

				MenuItem newMenuItem = new MenuItem("new");
				MenuItem openMenuItem = new MenuItem("open");
				MenuItem saveMenuItem = new MenuItem("save");
				MenuItem closeMenuItem = new MenuItem("close");
				MenuItem quitMenuItem = new MenuItem("quit");

				newMenuItem.setShortcut(new MenuShortcut('N'));
				openMenuItem.setShortcut(new MenuShortcut('O'));
				saveMenuItem.setShortcut(new MenuShortcut('S'));
				closeMenuItem.setShortcut(new MenuShortcut('W'));
				quitMenuItem.setShortcut(new MenuShortcut('Q'));

				newMenuItem.addActionListener(e -> createNewProject());
				openMenuItem.addActionListener(e -> openProject());
				saveMenuItem.addActionListener(e -> saveProject());
				closeMenuItem.addActionListener(e -> closeProject());
				quitMenuItem.addActionListener(e -> quit());

				fileMenu.add(newMenuItem);
				fileMenu.add(openMenuItem);
				fileMenu.add(saveMenuItem);
				fileMenu.add(closeMenuItem);
				fileMenu.addSeparator();
				fileMenu.add(quitMenuItem);

				menuBar.add(fileMenu);
			}

			{
				Menu projectMenu = new Menu("project");

				MenuItem editMenuItem = new MenuItem("edit");
				MenuItem verifyMenuItem = new MenuItem("verify");

				editMenuItem.addActionListener(e -> editProject());
				verifyMenuItem.addActionListener(e -> verifyProject());

				projectMenu.add(editMenuItem);
				projectMenu.add(verifyMenuItem);

				menuBar.add(projectMenu);
			}

			{
				Menu functionMenu = new Menu("function");

				MenuItem createMenuItem = new MenuItem("new");
				MenuItem editMenuItem = new MenuItem("edit");
				MenuItem showListMenuItem = new MenuItem("show list");
				MenuItem validateMenuItem = new MenuItem("validate");

				createMenuItem.setShortcut(new MenuShortcut('F'));
				showListMenuItem.setShortcut(new MenuShortcut('L'));

				createMenuItem.addActionListener(e -> createNewFunction());
				editMenuItem.addActionListener(e -> editCurrentFunction());
				showListMenuItem.addActionListener(e -> showFunctionList());
				validateMenuItem.addActionListener(e -> validateFunction());

				functionMenu.add(createMenuItem);
				functionMenu.add(editMenuItem);
				functionMenu.add(showListMenuItem);
				functionMenu.add(validateMenuItem);

				menuBar.add(functionMenu);
			}

			{
				Menu nodeMenu = new Menu("node");

				MenuItem validateNodesMenuItem = new MenuItem("validate nodes");

				validateNodesMenuItem.addActionListener(e -> validateNodes());

				{
					Menu createNewNodeMenu = new Menu("create new node");

					MenuItem statementNodeMenuItem = new MenuItem("statement node");
					MenuItem functionNodeMenuItem = new MenuItem("function node");
					MenuItem branchNodeMenuItem = new MenuItem("brach node");
					MenuItem loopNodeMenuItem = new MenuItem("loop node");

					statementNodeMenuItem.addActionListener(e -> createNewNode(JOPAStatementNode.class));
					functionNodeMenuItem.addActionListener(e -> createNewNode(JOPAFunctionNode.class));
					branchNodeMenuItem.addActionListener(e -> createNewNode(JOPABranchNode.class));
					loopNodeMenuItem.addActionListener(e -> createNewNode(JOPALoopNode.class));

					createNewNodeMenu.add(statementNodeMenuItem);
					createNewNodeMenu.add(functionNodeMenuItem);
					createNewNodeMenu.add(branchNodeMenuItem);
					createNewNodeMenu.add(loopNodeMenuItem);

					nodeMenu.add(createNewNodeMenu);
				}

				nodeMenu.add(validateNodesMenuItem);

				menuBar.add(nodeMenu);
			}

			{
				Menu shaderMenu = new Menu("shader");

				MenuItem generateShaderMenuItem = new MenuItem("generate shader");
				MenuItem showShaderCodeMenuItem = new MenuItem("show shader code");
				MenuItem validateShaderMenuITem = new MenuItem("validate shader");

				generateShaderMenuItem.setShortcut(new MenuShortcut('G'));
				showShaderCodeMenuItem.setShortcut(new MenuShortcut('S', true));
				validateShaderMenuITem.setShortcut(new MenuShortcut('V', true));

				generateShaderMenuItem.addActionListener(e -> generateShader());
				showShaderCodeMenuItem.addActionListener(e -> showShaderCode());
				validateShaderMenuITem.addActionListener(e -> validateGeneratedShader());

				shaderMenu.add(generateShaderMenuItem);
				shaderMenu.add(showShaderCodeMenuItem);
				shaderMenu.add(validateShaderMenuITem);

				menuBar.add(shaderMenu);
			}

			{
				Menu settingsMenu = new Menu("settings");

				MenuItem showPortTypesMenuItem = new MenuItem("show port types");
				MenuItem hightlightNodesMenuItem = new MenuItem("highlight nodes");
				MenuItem saveSettingsMenuItem = new MenuItem("save settings");

				showPortTypesMenuItem.setShortcut(new MenuShortcut('T', true));
				hightlightNodesMenuItem.setShortcut(new MenuShortcut('H', true));

				showPortTypesMenuItem.addActionListener(e -> toggleShowingPortTypes());
				hightlightNodesMenuItem.addActionListener(e -> toggleHighlightingNodes());
				saveSettingsMenuItem.addActionListener(e -> saveSettings());

				settingsMenu.add(showPortTypesMenuItem);
				settingsMenu.add(hightlightNodesMenuItem);

				menuBar.add(settingsMenu);
			}

			{
				Menu playgroundMenu = new Menu("playground");

				MenuItem editScriptMenuItem = new MenuItem("edit script");
				MenuItem startPlaygroundMenuItem = new MenuItem("start playground");
				MenuItem stopPlaygroundMenuItem = new MenuItem("stop playground");
				MenuItem closePlaygroundMenuItem = new MenuItem("close playground");

				editScriptMenuItem.setShortcut(new MenuShortcut('E'));
				startPlaygroundMenuItem.setShortcut(new MenuShortcut('P'));

				editScriptMenuItem.addActionListener(e -> editScript());
				startPlaygroundMenuItem.addActionListener(e -> startPlayground());
				stopPlaygroundMenuItem.addActionListener(e -> stopPlayground());
				closePlaygroundMenuItem.addActionListener(e -> closePlayground());

				{
					Menu createPlaygroundMenuItem = new Menu("create playground");

					MenuItem defaultFragmentShaderMenuItem = new MenuItem("default fragment shader");
					MenuItem defaultComputeShaderMenuItem = new MenuItem("default compute shader");
					MenuItem customShaderMenuItem = new MenuItem("custom shader");

					defaultFragmentShaderMenuItem.addActionListener(e -> createPlayground(JOPAProjectType.FRAGMENT));
					defaultComputeShaderMenuItem.addActionListener(e -> createPlayground(JOPAProjectType.COMPUTE));
					customShaderMenuItem.addActionListener(e -> createCustomPlayground(JOPAProjectType.FRAGMENT));

					createPlaygroundMenuItem.add(defaultFragmentShaderMenuItem);
					createPlaygroundMenuItem.add(defaultComputeShaderMenuItem);
					createPlaygroundMenuItem.add(customShaderMenuItem);

					playgroundMenu.add(createPlaygroundMenuItem);
				}

				playgroundMenu.add(editScriptMenuItem);
				playgroundMenu.add(startPlaygroundMenuItem);
				playgroundMenu.add(stopPlaygroundMenuItem);
				playgroundMenu.add(closePlaygroundMenuItem);

				menuBar.add(playgroundMenu);
			}

			{
				Menu helpMenu = new Menu("help");

				MenuItem aboutMenuItem = new MenuItem("about");
				MenuItem manualMenuItem = new MenuItem("manual");

				aboutMenuItem.addActionListener(e -> about());
				manualMenuItem.addActionListener(e -> manual());

				helpMenu.add(aboutMenuItem);
				helpMenu.add(manualMenuItem);

				menuBar.add(helpMenu);
			}

			window.setMenuBar(menuBar);
		}

		if (menuPanel == null) {
			menuPanel = new JOPAMenuPanel();
		}
		window.getContentPane().add(menuPanel, BorderLayout.NORTH);
		menuPanel.setPreferredSize(new Dimension(window.getWidth(), 40));
		menuPanel.initMenu();
	}

	public synchronized void createTabs() {
		if (tabs == null) {
			tabs = new JTabbedPane();
			tabs.setDoubleBuffered(true);
			tabs.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					Component tab = tabs.getComponents()[tabs.getSelectedIndex()];
					if (tab.getClass().equals(JOPACanvas.class)) {
						JOPACanvas canvas = (JOPACanvas) tab;
						String functionName = tabs.getTitleAt(tabs.getSelectedIndex());
						System.out.println("[UI] New active function: " + functionName);
						currentProject.selectFunction(functionName);
						currentCanvas = canvas;
					}
				}
			});
			int offset = menuPanel.getHeight();
			window.getContentPane().add(tabs, BorderLayout.CENTER);
			tabs.setLocation(0, offset);
			Dimension tabsSize = new Dimension(window.getWidth(), window.getHeight() - offset);
			tabs.setPreferredSize(tabsSize);
		}
	}

	public synchronized void addFunction(JOPAFunction function) {
		JOPACanvas canvas = new JOPACanvas();
		tabs.addTab(function.name, canvas);
		canvases.put(function.name, canvas);
		tabs.setSelectedComponent(canvas);
		currentCanvas.setPreferredSize(tabs.getSize());
		currentCanvas = canvas;
	}

	public synchronized void openProjectEditor(JOPAProject project) {
		JOPAEditProjectDialog editProjectDialog = new JOPAEditProjectDialog(window, project);
		editProjectDialog.dispose();
	}

	public synchronized void openDefinesEditor(JOPAProject project) {
		JOPAEditDefinesDialog editDefinesDialog = new JOPAEditDefinesDialog(window, currentProject);
		editDefinesDialog.dispose();
	}

	public synchronized void openTypesListEditor(JOPAProject project) {
		JOPAEditTypesListDialog editTypesDialog = new JOPAEditTypesListDialog(window, project);
		editTypesDialog.dispose();
	}

	public synchronized void openTypeEditor(JOPACustomType type) {
		JOPAEditTypeDialog editTypeDialog = new JOPAEditTypeDialog(window, type);
		editTypeDialog.dispose();
	}

	public synchronized void openResourcesEditor(JOPAProject project) {
		JOPAEditResourcesDialog editResourcesDialog = new JOPAEditResourcesDialog(window, currentProject);
		editResourcesDialog.dispose();
	}

	public synchronized void openConstantsEditor(JOPAProject project) {
		JOPAEditConstantsDialog editConstantsDialog = new JOPAEditConstantsDialog(window, project);
		editConstantsDialog.dispose();
	}

	public synchronized void openGlobalsEditor(JOPAFunction function) {
		JOPAEditGlobalsDialog editGlobalsDialog = new JOPAEditGlobalsDialog(window, currentProject);
		editGlobalsDialog.dispose();
	}

	public synchronized void openFunctionEditor(JOPAFunction function) {
		JOPAEditFunctionDialog editFunctionDialog = new JOPAEditFunctionDialog(window, function);
		editFunctionDialog.dispose();
	}

	public synchronized void openScriptEditor(JOPASimulationScript script) {
		JOPAEditScriptDialog editScriptDialog = new JOPAEditScriptDialog(window, script);
		editScriptDialog.dispose();
	}

	public synchronized void openFunctionNodeEditor(JOPAFunctionNode node) {
		JOPAEditFunctionNodeDialog editFunctionNodeDialog = new JOPAEditFunctionNodeDialog(window, node);
		editFunctionNodeDialog.dispose();
	}

	public synchronized void openStatementNodeEditor(JOPAStatementNode node) {
		JOPAEditStatementNodeDialog editStatementNodeDialog = new JOPAEditStatementNodeDialog(window, node);
		editStatementNodeDialog.dispose();
	}

	public synchronized void openBranchNodeEditor(JOPABranchNode node) {
		JOPAEditBranchNodeDialog editBranchNodeDialog = new JOPAEditBranchNodeDialog(window, node);
		editBranchNodeDialog.dispose();
	}

	public synchronized void openLoopNodeEditor(JOPALoopNode node) {
		JOPAEditLoopNodeDialog editLoopNodeDialog = new JOPAEditLoopNodeDialog(window, node);
		editLoopNodeDialog.dispose();
	}

	public synchronized void showFunctionsList(JOPAProject project) {
		JOPAEditFunctionListDialog showFunctionListDialog = new JOPAEditFunctionListDialog(window, project);
		showFunctionListDialog.dispose();
	}

	public synchronized void showTemplatesList(JOPAProject project) {
		JOPAShowTemplateListDialog showTemplateListDialog = new JOPAShowTemplateListDialog(window, project);
		showTemplateListDialog.dispose();
	}

	public synchronized void openTemplateEditor(JOPATemplate template) {
		JOPAEditTemplateDialog editTemplateDialog = new JOPAEditTemplateDialog(window, template);
		editTemplateDialog.dispose();
	}

	public synchronized void showShader(String shaderCode) {
		JOPAShowShaderDialog showShaderDialog = new JOPAShowShaderDialog(window, shaderCode);
		showShaderDialog.dispose();
	}

	public synchronized JOPATemplate selectTemplate() {
		JOPAFindTemplateDialog findTemplateDialog = new JOPAFindTemplateDialog(window, currentProject.templates);
		findTemplateDialog.dispose();

		return findTemplateDialog.selectedTemplate;
	}

	public synchronized void showMessage(String text) {
		if (window != null) {
			JOPAMessageDialog messageWindow = new JOPAMessageDialog(window, text, "message");
			messageWindow.dispose();
		} else {
			JOPAMessageDialog messageWindow = new JOPAMessageDialog(new Frame("error"), text, "message");
			messageWindow.dispose();
		}
	}

	public synchronized boolean showQuestion(String question) {
		JOPAQuestionDialog questionDialog = new JOPAQuestionDialog(window, question);
		questionDialog.dispose();

		return questionDialog.getAnswer();
	}

	public synchronized File showFileDialog(String path, FileNameExtensionFilter filter, String title, boolean save) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(false);
		if (title != null) {
			fileChooser.setDialogTitle(title);
		}
		if (filter != null) {
			fileChooser.setFileFilter(filter);
		}
		if (path != null) {
			File startingPath = new File(path);
			if (startingPath.exists() && startingPath.isDirectory()) {
				fileChooser.setCurrentDirectory(startingPath);
			}
		}
		if (!save) {
			int returnValue = fileChooser.showOpenDialog(window);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				return fileChooser.getSelectedFile();
			}
		} else {
			int returnValue = fileChooser.showSaveDialog(window);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				if (selectedFile != null) {
					String selectedPath = selectedFile.getAbsolutePath();
					String extension = "." + filter.getExtensions()[0];
					if (!selectedPath.endsWith(extension)) {
						selectedPath += extension;
						selectedFile = new File(selectedPath);
					}
				}

				return selectedFile;
			}
		}

		return null;
	}

	public synchronized void closeProjectTabs() {
		tabs.removeAll();
		tabs.revalidate();
		tabs.repaint();
	}

	public synchronized void repaint() {
		if (currentCanvas != null) {
			currentCanvas.repaint();
		}
	}

	public synchronized void close() {
		if (window != null) {
			window.dispose();
		}
	}

}