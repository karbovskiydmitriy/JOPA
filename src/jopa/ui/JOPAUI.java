package jopa.ui;

import static jopa.main.JOPAMain.about;
import static jopa.main.JOPAMain.closePlayground;
import static jopa.main.JOPAMain.createNewFunction;
import static jopa.main.JOPAMain.createNewNode;
import static jopa.main.JOPAMain.createNewWorkspace;
import static jopa.main.JOPAMain.createPlayground;
import static jopa.main.JOPAMain.currentWorkspace;
import static jopa.main.JOPAMain.destroyWorkspace;
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
import static jopa.main.JOPAMain.workspaceSync;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Panel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jopa.graphics.JOPACanvas;
import jopa.main.JOPAFunction;
import jopa.nodes.JOPANode;
import jopa.ui.dialogs.JOPAEditConstantsDialog;
import jopa.ui.dialogs.JOPAEditFunctionDialog;
import jopa.ui.dialogs.JOPAEditNodeDialog;
import jopa.ui.dialogs.JOPAMessageWindow;
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

		window.setBounds(0, 0, 800, 600);
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
				Menu nodeMenu = new Menu("node");

				MenuItem createNewNodeMenuItem = new MenuItem("create new node");
				MenuItem validateNodeMenuItem = new MenuItem("validate nodes");

				createNewNodeMenuItem.addActionListener(e -> createNewNode());
				validateNodeMenuItem.addActionListener(e -> validateNodes());

				nodeMenu.add(createNewNodeMenuItem);
				nodeMenu.add(validateNodeMenuItem);

				menuBar.add(nodeMenu);
			}

			{
				Menu functionMenu = new Menu("function");

				MenuItem createNewFunctionMenuItem = new MenuItem("create new function");
				MenuItem validateFunctionMenuItem = new MenuItem("validate functions");

				createNewFunctionMenuItem.setShortcut(new MenuShortcut('F', true));
				createNewFunctionMenuItem.addActionListener(e -> createNewFunction(null));
				validateFunctionMenuItem.addActionListener(e -> validateFunction());

				functionMenu.add(createNewFunctionMenuItem);
				functionMenu.add(validateFunctionMenuItem);

				menuBar.add(functionMenu);
			}

			{
				Menu generateMenu = new Menu("shader");

				MenuItem generateShaderMenuItem = new MenuItem("generate shader");
				MenuItem showShaderCodeMenuItem = new MenuItem("show shader code");

				generateShaderMenuItem.addActionListener(e -> generateShader());
				showShaderCodeMenuItem.addActionListener(e -> showShaderCode());

				generateMenu.add(generateShaderMenuItem);
				generateMenu.add(showShaderCodeMenuItem);

				menuBar.add(generateMenu);
			}

			{
				Menu playgroundMenu = new Menu("playground");

				MenuItem createPlaygroundMenuItem = new MenuItem("create playground");
				MenuItem startPlaygroundMenuItem = new MenuItem("start playground");
				MenuItem stopPlaygroundMenuItem = new MenuItem("stop playground");
				MenuItem closePlaygroundMenuItem = new MenuItem("close playground");

				createPlaygroundMenuItem.addActionListener(e -> createPlayground());
				startPlaygroundMenuItem.addActionListener(e -> startPlayground());
				stopPlaygroundMenuItem.addActionListener(e -> stopPlayground());
				closePlaygroundMenuItem.addActionListener(e -> closePlayground());

				playgroundMenu.add(createPlaygroundMenuItem);
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
		 */ // FIXME!!!
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
						currentWorkspace.selectFunction(selectedIndex);
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
			canvas.setDoubleBuffered(true);
			canvas.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					synchronized (workspaceSync) {
						if (currentWorkspace != null) {
							currentWorkspace.mousePressed(e.getPoint());
							canvas.repaint();
						}
					}
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					synchronized (workspaceSync) {
						if (currentWorkspace != null) {
							currentWorkspace.mouseReleased(e.getPoint());
							canvas.repaint();
						}
					}
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					synchronized (workspaceSync) {
						if (currentWorkspace != null) {
							currentWorkspace.mouseClicked(e.getPoint());
							canvas.repaint();
						}
					}
				}
			});
			canvas.addMouseMotionListener(new MouseAdapter() {
				@Override
				public void mouseMoved(MouseEvent e) {
					synchronized (workspaceSync) {
						if (currentWorkspace != null) {
							currentWorkspace.mouseMoved(e.getPoint());
							canvas.repaint();
						}
					}
				}

				@Override
				public void mouseDragged(MouseEvent e) {
					synchronized (workspaceSync) {
						if (currentWorkspace != null) {
							currentWorkspace.mouseMoved(e.getPoint());
							canvas.repaint();
						}
					}
				}
			});
			window.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					System.out.println(e);
					synchronized (workspaceSync) {
						if (currentWorkspace != null) {
							currentWorkspace.keyTyped(e.getKeyCode());
						}
					}
				}
			});

			canvas.repaint();
		}
	}

	public synchronized void addFunction(JOPAFunction function) {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {
				// JOPACanvas canvas = createCanvas();
				// function.canvas = canvas;
				// if (openEditor) {
				// editFunctionPrototype(function);
				// }
				// Panel panel = new Panel();
				// panel.add(canvas);
				tabs.addTab(function.name, canvas);
			}
		}
	}

	public synchronized void editFunctionPrototype(JOPAFunction function) {
		new JOPAEditFunctionDialog(window, function);
	}
	
	public synchronized void editConstants(JOPAFunction function) {
		new JOPAEditConstantsDialog(window, function);
	}

	public synchronized void editNode(JOPANode node) {
		new JOPAEditNodeDialog(window, node);
	}
	
	public synchronized void showShader(String shaderCode) {
		new JOPAShowShaderDialog(window, shaderCode);
	}

	public synchronized void showMessage(String text) {
		if (window != null) {
			JOPAMessageWindow messageWindow = new JOPAMessageWindow(window, text, "message");
			messageWindow.dispose();
		} else {
			JOPAMessageWindow messageWindow = new JOPAMessageWindow(new Frame("error"), text, "message");
			messageWindow.dispose();
		}
	}

	public synchronized void notImplemented() {
		showMessage("TODO"); // TODO
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