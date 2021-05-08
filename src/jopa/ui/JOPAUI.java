package jopa.ui;

import static jopa.main.JOPAMain.about;
import static jopa.main.JOPAMain.closePlayground;
import static jopa.main.JOPAMain.createNewFunction;
import static jopa.main.JOPAMain.createNewNode;
import static jopa.main.JOPAMain.createNewWorkspace;
import static jopa.main.JOPAMain.createPlayground;
import static jopa.main.JOPAMain.currentProject;
import static jopa.main.JOPAMain.destroyWorkspace;
import static jopa.main.JOPAMain.editProject;
import static jopa.main.JOPAMain.editScript;
import static jopa.main.JOPAMain.generateShader;
import static jopa.main.JOPAMain.manual;
import static jopa.main.JOPAMain.openWorkspace;
import static jopa.main.JOPAMain.quit;
import static jopa.main.JOPAMain.saveWorkspace;
import static jopa.main.JOPAMain.showShaderCode;
import static jopa.main.JOPAMain.startPlayground;
import static jopa.main.JOPAMain.stopPlayground;
import static jopa.main.JOPAMain.validateFunction;
import static jopa.main.JOPAMain.validateNodes;
import static jopa.main.JOPAMain.verifyProject;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import jopa.graphics.JOPACanvas;
import jopa.main.JOPAFunction;
import jopa.main.JOPAProject;
import jopa.nodes.JOPABranchNode;
import jopa.nodes.JOPAFunctionNode;
import jopa.nodes.JOPALoopNode;
import jopa.nodes.JOPANode;
import jopa.nodes.JOPAStatementNode;
import jopa.playground.JOPASimulationScript;
import jopa.playground.JOPASimulationType;
import jopa.ui.dialogs.JOPAEditBranchNodeGialog;
import jopa.ui.dialogs.JOPAEditConstantsDialog;
import jopa.ui.dialogs.JOPAEditFunctionDialog;
import jopa.ui.dialogs.JOPAEditLoopNodeDialog;
import jopa.ui.dialogs.JOPAEditProjectDialog;
import jopa.ui.dialogs.JOPAEditScriptDialog;
import jopa.ui.dialogs.JOPAEditStatementNodeDialog;
import jopa.ui.dialogs.JOPAMessageDialog;
import jopa.ui.dialogs.JOPAShowShaderDialog;

public class JOPAUI {

	private JFrame window;
	private MenuBar menuBar;
	private Panel menuPanel;
	private JTabbedPane tabs;
	private JOPACanvas canvas;

	public synchronized void createWindow() {
		String title = "Java and OpenGL parallel algorithms application (JOPA) v1.0 by Karbovskiy Dmitriy (2020-2021)";
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
		window.setLayout(new BorderLayout());
	}

	public synchronized void createMenu() {
		if (menuBar == null) {
			menuBar = new MenuBar();

			{
				Menu fileMenu = new Menu("file");

				MenuItem newFileMenuItem = new MenuItem("new");
				MenuItem openFileMenuItem = new MenuItem("open");
				MenuItem saveFileMenuItem = new MenuItem("save");
				MenuItem closeFileMenuItem = new MenuItem("close");
				MenuItem quitFileMenuItem = new MenuItem("quit");

				newFileMenuItem.setShortcut(new MenuShortcut('N'));
				openFileMenuItem.setShortcut(new MenuShortcut('O'));
				saveFileMenuItem.setShortcut(new MenuShortcut('S'));
				closeFileMenuItem.setShortcut(new MenuShortcut('W'));
				quitFileMenuItem.setShortcut(new MenuShortcut('Q'));

				newFileMenuItem.addActionListener(e -> createNewWorkspace());
				openFileMenuItem.addActionListener(e -> openWorkspace());
				saveFileMenuItem.addActionListener(e -> saveWorkspace());
				closeFileMenuItem.addActionListener(e -> destroyWorkspace());
				quitFileMenuItem.addActionListener(e -> quit());

				fileMenu.add(newFileMenuItem);
				fileMenu.add(openFileMenuItem);
				fileMenu.add(saveFileMenuItem);
				fileMenu.add(closeFileMenuItem);
				fileMenu.addSeparator();
				fileMenu.add(quitFileMenuItem);

				menuBar.add(fileMenu);
			}

			{
				Menu projectMenu = new Menu("project");

				MenuItem editProjectMenuItem = new MenuItem("edit");
				MenuItem verifyProjectMenuItem = new MenuItem("verify");

				editProjectMenuItem.addActionListener(e -> editProject());
				verifyProjectMenuItem.addActionListener(e -> verifyProject());

				projectMenu.add(editProjectMenuItem);
				projectMenu.add(verifyProjectMenuItem);

				menuBar.add(projectMenu);
			}

			{
				Menu functionMenu = new Menu("function");

				MenuItem createNewFunctionMenuItem = new MenuItem("create new function");
				MenuItem validateFunctionMenuItem = new MenuItem("validate functions");

				createNewFunctionMenuItem.setShortcut(new MenuShortcut('F', true));

				createNewFunctionMenuItem.addActionListener(e -> createNewFunction());
				validateFunctionMenuItem.addActionListener(e -> validateFunction());

				functionMenu.add(createNewFunctionMenuItem);
				functionMenu.add(validateFunctionMenuItem);

				menuBar.add(functionMenu);
			}

			{
				Menu nodeMenu = new Menu("node");

				MenuItem validateNodeMenuItem = new MenuItem("validate nodes");

				validateNodeMenuItem.addActionListener(e -> validateNodes());

				{
					Menu createNewNodeMenu = new Menu("create new node");

					MenuItem statementNode = new MenuItem("statement node");
					MenuItem functionNode = new MenuItem("function node");
					MenuItem branchNode = new MenuItem("brach node");
					MenuItem loopNode = new MenuItem("loop node");

					statementNode.addActionListener(e -> createNewNode(JOPAStatementNode.class));
					functionNode.addActionListener(e -> createNewNode(JOPAFunctionNode.class));
					branchNode.addActionListener(e -> createNewNode(JOPABranchNode.class));
					loopNode.addActionListener(e -> createNewNode(JOPALoopNode.class));

					createNewNodeMenu.add(statementNode);
					createNewNodeMenu.add(functionNode);
					createNewNodeMenu.add(branchNode);
					createNewNodeMenu.add(loopNode);

					nodeMenu.add(createNewNodeMenu);
				}

				nodeMenu.add(validateNodeMenuItem);

				menuBar.add(nodeMenu);
			}

			{
				Menu shaderMenu = new Menu("shader");

				MenuItem generateShaderMenuItem = new MenuItem("generate shader");
				MenuItem showShaderCodeMenuItem = new MenuItem("show shader code");

				generateShaderMenuItem.setShortcut(new MenuShortcut('G', true));

				generateShaderMenuItem.addActionListener(e -> generateShader());
				showShaderCodeMenuItem.addActionListener(e -> showShaderCode());

				shaderMenu.add(generateShaderMenuItem);
				shaderMenu.add(showShaderCodeMenuItem);

				menuBar.add(shaderMenu);
			}

			{
				Menu playgroundMenu = new Menu("playground");

				MenuItem editScriptMenuItem = new MenuItem("edit script");
				MenuItem startPlaygroundMenuItem = new MenuItem("start playground");
				MenuItem stopPlaygroundMenuItem = new MenuItem("stop playground");
				MenuItem closePlaygroundMenuItem = new MenuItem("close playground");

				editScriptMenuItem.addActionListener(e -> editScript());
				startPlaygroundMenuItem.addActionListener(e -> startPlayground());
				stopPlaygroundMenuItem.addActionListener(e -> stopPlayground());
				closePlaygroundMenuItem.addActionListener(e -> closePlayground());

				{
					Menu createPlaygroundMenuItem = new Menu("create playground");

					MenuItem defaultFragmentShaderMenuItem = new MenuItem("default fragment shader");
					MenuItem defaultComputeShaderMenuItem = new MenuItem("default compute shader");
					MenuItem customShaderMenuItem = new MenuItem("custom shader");

					defaultFragmentShaderMenuItem.addActionListener(e -> createPlayground(JOPASimulationType.FRAGMENT));
					defaultComputeShaderMenuItem.addActionListener(e -> createPlayground(JOPASimulationType.COMPUTE));
					customShaderMenuItem.addActionListener(e -> createPlayground(JOPASimulationType.CUSTOM));

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

				MenuItem aboutHelpMenuItem = new MenuItem("about");
				MenuItem manualHelpMenuItem = new MenuItem("manual");

				aboutHelpMenuItem.addActionListener(e -> about());
				manualHelpMenuItem.addActionListener(e -> manual());

				helpMenu.add(aboutHelpMenuItem);
				helpMenu.add(manualHelpMenuItem);

				menuBar.add(helpMenu);
			}

			window.setMenuBar(menuBar);
		}

		/*
		 * if (menuPanel == null) { menuPanel = new JOPAMenuPanel(); // TODO stuff
		 * window.add(menuPanel); menuPanel.setBackground(new Color(0.3f, 0.8f, 0.9f));
		 * menuPanel.setSize(window.getWidth(), 40); // menuPanel.init(); }
		 */ // FIXME panel menu
	}

	public synchronized void createTabs() {
		if (tabs == null) {
			tabs = new JTabbedPane();
			tabs.setDoubleBuffered(true);
			tabs.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					// if (currentCanvas != null) {
					// currentCanvas.setEnabled(false); // TODO the switch
					// }
					int selectedIndex = tabs.getSelectedIndex();
					if (selectedIndex != -1) {
						currentProject.selectFunction(selectedIndex);
					}
					// if (function != null) {
					// currentCanvas = function.canvas;
					// currentCanvas.setEnabled(true);
					// } else {
					// currentCanvas = null;
					// }
				}
			});
			window.add(tabs);
			int y;
			if (menuPanel != null) {
				y = menuPanel.getLocation().y + menuPanel.getHeight();
			} else {
				y = 0;
			}
			// System.out.println(y);
			tabs.setLocation(0, y);
			tabs.setSize(window.getWidth(), window.getHeight() - y);
			// System.out.println(tabs.getLocation());
			// menuPanel.setVisible(false);
		}
	}

	public synchronized void createCanvas() {
		if (canvas == null) {
			canvas = new JOPACanvas();
			canvas.repaint();
		}
	}

	public synchronized void addFunction(JOPAFunction function) {
		// synchronized (projectSync) {
		// if (currentProject != null) {
		// JOPACanvas canvas = createCanvas();
		// function.canvas = canvas;
		// if (openEditor) {
		// editFunctionPrototype(function);
		// }
		// Panel panel = new Panel();
		// panel.add(canvas);
		tabs.addTab(function.name, canvas);
		// }
		// }
	}

	public synchronized void openProjectEditor(JOPAProject project) {
		new JOPAEditProjectDialog(window, project);
	}

	public synchronized void openScriptEditor(JOPASimulationScript script) {
		new JOPAEditScriptDialog(window, script);
	}

	public synchronized void openFunctionEditor(JOPAFunction function) {
		new JOPAEditFunctionDialog(window, function);
	}

	public synchronized void openConstantsEditor(JOPAFunction function) {
		new JOPAEditConstantsDialog(window, currentProject);
	}

	public synchronized void openNodeEditor(JOPANode node) {
		Class<?> type = node.getClass();
		if (type.equals(JOPAStatementNode.class)) {
			new JOPAEditStatementNodeDialog(window, (JOPAStatementNode) node);
		} else if (type.equals(JOPABranchNode.class)) {
			new JOPAEditBranchNodeGialog(window, (JOPABranchNode) node);
		} else if (type.equals(JOPALoopNode.class)) {
			new JOPAEditLoopNodeDialog(window, (JOPALoopNode) node);
		}
	}

	public synchronized void showShader(String shaderCode) {
		new JOPAShowShaderDialog(window, shaderCode);
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
				return fileChooser.getSelectedFile();
			}
		}

		return null;
	}

	public synchronized void closeProject() {
		tabs.removeAll();
		tabs.revalidate();
		tabs.repaint();
	}

	public synchronized void repaint() {
		if (canvas != null) {
			canvas.repaint();
		}
	}

	public synchronized void close() {
		if (window != null) {
			window.dispose();
		}
	}

}