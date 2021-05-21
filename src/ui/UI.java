package ui;

import static app.Main.about;
import static app.Main.closePlayground;
import static app.Main.closeProject;
import static app.Main.createCustomPlayground;
import static app.Main.createNewFunction;
import static app.Main.createNewNode;
import static app.Main.createNewProject;
import static app.Main.createPlayground;
import static app.Main.currentProject;
import static app.Main.editCurrentFunction;
import static app.Main.editProject;
import static app.Main.editScript;
import static app.Main.generateShader;
import static app.Main.manual;
import static app.Main.openProject;
import static app.Main.quit;
import static app.Main.saveProject;
import static app.Main.saveSettings;
import static app.Main.showFunctionList;
import static app.Main.showShaderCode;
import static app.Main.startPlayground;
import static app.Main.stopPlayground;
import static app.Main.toggleHighlightingNodes;
import static app.Main.toggleShowingPortTypes;
import static app.Main.validateFunction;
import static app.Main.validateGeneratedShader;
import static app.Main.validateNodes;
import static app.Main.verifyProject;

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

import app.Function;
import app.Project;
import app.Template;
import graphics.Canvas;
import graphics.Image;
import nodes.BranchNode;
import nodes.FunctionNode;
import nodes.LoopNode;
import nodes.StatementNode;
import playground.SimulationScript;
import types.Buffer;
import types.CustomType;
import types.ProjectType;
import ui.dialogs.EditBranchNodeDialog;
import ui.dialogs.EditBufferDialog;
import ui.dialogs.EditConstantsDialog;
import ui.dialogs.EditDefinesDialog;
import ui.dialogs.EditFunctionDialog;
import ui.dialogs.EditFunctionListDialog;
import ui.dialogs.EditFunctionNodeDialog;
import ui.dialogs.EditGlobalsDialog;
import ui.dialogs.EditLoopNodeDialog;
import ui.dialogs.EditProjectDialog;
import ui.dialogs.EditResourcesDialog;
import ui.dialogs.EditScriptDialog;
import ui.dialogs.EditStatementNodeDialog;
import ui.dialogs.EditTemplateDialog;
import ui.dialogs.EditTextureDialog;
import ui.dialogs.EditTypeDialog;
import ui.dialogs.EditTypesListDialog;
import ui.dialogs.FindTemplateDialog;
import ui.dialogs.MessageDialog;
import ui.dialogs.QuestionDialog;
import ui.dialogs.ShowShaderDialog;
import ui.dialogs.ShowTemplateListDialog;

public class UI {

	private JFrame window;
	private MenuBar menuBar;
	private MenuPanel menuPanel;
	private JTabbedPane tabs;
	private HashMap<String, Canvas> canvases;
	private Canvas currentCanvas;

	public UI() {
		canvases = new HashMap<String, Canvas>(1);
	}

	public synchronized void createWindow() {
		String title = "Java and OpenGL parallel algorithms application v1.0 by Karbovskiy Dmitriy (2020-2021)";
		window = new JFrame(title);
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				windowEvent.getWindow().dispose();
			}
		});

		window.setSize(800, 600);
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

				newMenuItem.addActionListener(l -> createNewProject());
				openMenuItem.addActionListener(l -> openProject());
				saveMenuItem.addActionListener(l -> saveProject());
				closeMenuItem.addActionListener(l -> closeProject());
				quitMenuItem.addActionListener(l -> quit());

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

				editMenuItem.addActionListener(l -> editProject());
				verifyMenuItem.addActionListener(l -> verifyProject());

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

				createMenuItem.addActionListener(l -> createNewFunction());
				editMenuItem.addActionListener(l -> editCurrentFunction());
				showListMenuItem.addActionListener(l -> showFunctionList());
				validateMenuItem.addActionListener(l -> validateFunction());

				functionMenu.add(createMenuItem);
				functionMenu.add(editMenuItem);
				functionMenu.add(showListMenuItem);
				functionMenu.add(validateMenuItem);

				menuBar.add(functionMenu);
			}

			{
				Menu nodeMenu = new Menu("node");

				MenuItem validateNodesMenuItem = new MenuItem("validate nodes");

				validateNodesMenuItem.addActionListener(l -> validateNodes());

				{
					Menu createNewNodeMenu = new Menu("create new node");

					MenuItem statementNodeMenuItem = new MenuItem("statement node");
					MenuItem functionNodeMenuItem = new MenuItem("function node");
					MenuItem branchNodeMenuItem = new MenuItem("branch node");
					// MenuItem loopNodeMenuItem = new MenuItem("loop node");

					statementNodeMenuItem.addActionListener(l -> createNewNode(StatementNode.class));
					functionNodeMenuItem.addActionListener(l -> createNewNode(FunctionNode.class));
					branchNodeMenuItem.addActionListener(l -> createNewNode(BranchNode.class));
					// loopNodeMenuItem.addActionListener(l -> createNewNode(LoopNode.class));

					createNewNodeMenu.add(statementNodeMenuItem);
					createNewNodeMenu.add(functionNodeMenuItem);
					createNewNodeMenu.add(branchNodeMenuItem);
					// createNewNodeMenu.add(loopNodeMenuItem);

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

				generateShaderMenuItem.addActionListener(l -> generateShader());
				showShaderCodeMenuItem.addActionListener(l -> showShaderCode());
				validateShaderMenuITem.addActionListener(l -> validateGeneratedShader());

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

				showPortTypesMenuItem.addActionListener(l -> toggleShowingPortTypes());
				hightlightNodesMenuItem.addActionListener(l -> toggleHighlightingNodes());
				saveSettingsMenuItem.addActionListener(l -> saveSettings());

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

				editScriptMenuItem.addActionListener(l -> editScript());
				startPlaygroundMenuItem.addActionListener(l -> startPlayground());
				stopPlaygroundMenuItem.addActionListener(l -> stopPlayground());
				closePlaygroundMenuItem.addActionListener(l -> closePlayground());

				{
					Menu createPlaygroundMenuItem = new Menu("create playground");

					MenuItem defaultFragmentShaderMenuItem = new MenuItem("default fragment shader");
					MenuItem defaultComputeShaderMenuItem = new MenuItem("default compute shader");
					MenuItem customShaderMenuItem = new MenuItem("custom shader");

					defaultFragmentShaderMenuItem.addActionListener(l -> createPlayground(ProjectType.FRAGMENT));
					defaultComputeShaderMenuItem.addActionListener(l -> createPlayground(ProjectType.COMPUTE));
					customShaderMenuItem.addActionListener(l -> createCustomPlayground(ProjectType.FRAGMENT));

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

				aboutMenuItem.addActionListener(l -> about());
				manualMenuItem.addActionListener(l -> manual());

				helpMenu.add(aboutMenuItem);
				helpMenu.add(manualMenuItem);

				menuBar.add(helpMenu);
			}

			window.setMenuBar(menuBar);
		}

		if (menuPanel == null) {
			menuPanel = new MenuPanel();
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
					if (tab.getClass().equals(Canvas.class)) {
						Canvas canvas = (Canvas) tab;
						String functionName = tabs.getTitleAt(tabs.getSelectedIndex());
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

	public synchronized void addFunction(Function function) {
		Canvas canvas = new Canvas();
		tabs.addTab(function.name, canvas);
		canvases.put(function.name, canvas);
		tabs.setSelectedComponent(canvas);
		currentCanvas.setPreferredSize(tabs.getSize());
		currentCanvas = canvas;
	}

	public synchronized void openProjectEditor(Project project) {
		EditProjectDialog editProjectDialog = new EditProjectDialog(window, project);
		editProjectDialog.dispose();
	}

	public synchronized void openDefinesEditor(Project project) {
		EditDefinesDialog editDefinesDialog = new EditDefinesDialog(window, currentProject);
		editDefinesDialog.dispose();
	}

	public synchronized void openTypesListEditor(Project project) {
		EditTypesListDialog editTypesDialog = new EditTypesListDialog(window, project);
		editTypesDialog.dispose();
	}

	public synchronized void openTypeEditor(CustomType type) {
		EditTypeDialog editTypeDialog = new EditTypeDialog(window, type);
		editTypeDialog.dispose();
	}

	public synchronized void openResourcesEditor(Project project) {
		EditResourcesDialog editResourcesDialog = new EditResourcesDialog(window, currentProject);
		editResourcesDialog.dispose();
	}

	public synchronized void openTextureEditor(Image image) {
		EditTextureDialog editTextureDialog = new EditTextureDialog(window, image);
		editTextureDialog.dispose();
	}

	public synchronized void openBufferEditor(Buffer buffer) {
		EditBufferDialog editBufferDialog = new EditBufferDialog(window, buffer);
		editBufferDialog.dispose();
	}

	public synchronized void openConstantsEditor(Project project) {
		EditConstantsDialog editConstantsDialog = new EditConstantsDialog(window, project);
		editConstantsDialog.dispose();
	}

	public synchronized void openGlobalsEditor(Function function) {
		EditGlobalsDialog editGlobalsDialog = new EditGlobalsDialog(window, currentProject);
		editGlobalsDialog.dispose();
	}

	public synchronized void openFunctionEditor(Function function) {
		EditFunctionDialog editFunctionDialog = new EditFunctionDialog(window, function);
		editFunctionDialog.dispose();
	}

	public synchronized void openScriptEditor(SimulationScript script) {
		EditScriptDialog editScriptDialog = new EditScriptDialog(window, script);
		editScriptDialog.dispose();
	}

	public synchronized void openFunctionNodeEditor(FunctionNode node) {
		EditFunctionNodeDialog editFunctionNodeDialog = new EditFunctionNodeDialog(window, node);
		editFunctionNodeDialog.dispose();
	}

	public synchronized void openStatementNodeEditor(StatementNode node) {
		EditStatementNodeDialog editStatementNodeDialog = new EditStatementNodeDialog(window, node);
		editStatementNodeDialog.dispose();
	}

	public synchronized void openBranchNodeEditor(BranchNode node) {
		EditBranchNodeDialog editBranchNodeDialog = new EditBranchNodeDialog(window, node);
		editBranchNodeDialog.dispose();
	}

	public synchronized void openLoopNodeEditor(LoopNode node) {
		EditLoopNodeDialog editLoopNodeDialog = new EditLoopNodeDialog(window, node);
		editLoopNodeDialog.dispose();
	}

	public synchronized void showFunctionsList(Project project) {
		EditFunctionListDialog showFunctionListDialog = new EditFunctionListDialog(window, project);
		showFunctionListDialog.dispose();
	}

	public synchronized void showTemplatesList(Project project) {
		ShowTemplateListDialog showTemplateListDialog = new ShowTemplateListDialog(window, project);
		showTemplateListDialog.dispose();
	}

	public synchronized void openTemplateEditor(Template template) {
		EditTemplateDialog editTemplateDialog = new EditTemplateDialog(window, template);
		editTemplateDialog.dispose();
	}

	public synchronized void showShader(String shaderCode) {
		ShowShaderDialog showShaderDialog = new ShowShaderDialog(window, shaderCode);
		showShaderDialog.dispose();
	}

	public synchronized Template selectTemplate() {
		FindTemplateDialog findTemplateDialog = new FindTemplateDialog(window, currentProject.templates);
		findTemplateDialog.dispose();

		return findTemplateDialog.selectedTemplate;
	}

	public synchronized void showMessage(String text) {
		if (window != null) {
			MessageDialog messageWindow = new MessageDialog(window, text, "message");
			messageWindow.dispose();
		} else {
			MessageDialog messageWindow = new MessageDialog(new Frame("error"), text, "message");
			messageWindow.dispose();
		}
	}

	public synchronized boolean showQuestion(String question) {
		QuestionDialog questionDialog = new QuestionDialog(window, question);
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