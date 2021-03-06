package JOPA;

import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JTabbedPane;

public class JOPAMain {

	public static Object workspaceSync;
	public static JOPAWorkspace currentWorkspace;

	private static JOPACanvas canvas;
	private static Frame window;

	public static void main(String[] args) {
		if (!checkVersion()) {
			return;
		}

		workspaceSync = new Object();

		setupWindow();
		createNewWorkspace();
		setupUI();
	}

	private static boolean checkVersion() {

		return true;
	}

	private static void setupWindow() {
		String title = "Java and OpenGL parallel application (JOPA) v1.0 by Karbovskiy Dmitriy (2020-2021)";
		window = new Frame(title);
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				windowEvent.getWindow().dispose();
			}
		});

		window.setBounds(0, 0, 800, 600);
		window.setEnabled(true);
		window.setVisible(true);
	}

	private static void setupUI() {
		setupMenu();
		setupCanvas();
	}

	private static void setupMenu() {
		MenuBar menuBar = new MenuBar();

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

			newFileMenuItem.addActionListener((e) -> {
				createNewWorkspace();
			});

			openFileMenuItem.addActionListener((e) -> {
				openWorkspace();
			});

			saveFileMenuItem.addActionListener((e) -> {
				saveWorkspace();
			});

			closeFileMenuItem.addActionListener((E) -> {
				destroyWorkspace();
			});

			quitFileMenuItem.addActionListener((e) -> {
				quit();
			});

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

			createNewNodeMenuItem.addActionListener((e) -> {
				createNewNode();
			});

			validateNodeMenuItem.addActionListener((e) -> {
				validateNodes();
			});

			nodeMenu.add(createNewNodeMenuItem);
			nodeMenu.add(validateNodeMenuItem);

			menuBar.add(nodeMenu);
		}

		{
			Menu functionMenu = new Menu("function");

			MenuItem createNewFunctionMenuItem = new MenuItem("create new function");
			MenuItem validateFunctionMenuItem = new MenuItem("validate function");

			createNewFunctionMenuItem.addActionListener((e) -> {
				createNewFunction();
			});

			validateFunctionMenuItem.addActionListener((e) -> {
				validateFunction();
			});

			functionMenu.add(createNewFunctionMenuItem);
			functionMenu.add(validateFunctionMenuItem);

			menuBar.add(functionMenu);
		}

		{
			Menu helpMenu = new Menu("help");

			MenuItem aboutHelpMenuItem = new MenuItem("about");
			MenuItem manualHelpMenuItem = new MenuItem("manual");

			aboutHelpMenuItem.addActionListener((e) -> {
				about();
			});

			manualHelpMenuItem.addActionListener((e) -> {
				manual();
			});

			helpMenu.add(aboutHelpMenuItem);
			helpMenu.add(manualHelpMenuItem);

			menuBar.add(helpMenu);
		}

		window.setMenuBar(menuBar);
	}

	private static void setupCanvas() {
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
		canvas.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				synchronized (workspaceSync) {
					if (currentWorkspace != null) {
						currentWorkspace.keyTyped(e.getKeyCode());
					}
				}
			}
		});

		JTabbedPane tabs = new JTabbedPane();
		tabs.addTab("main", canvas);
		tabs.addTab("null", null);
		window.add(tabs);

		canvas.setSize(window.getSize());
		canvas.repaint();
	}

	private static void createNewWorkspace() {
		synchronized (workspaceSync) {
			currentWorkspace = new JOPAWorkspace("New workspace");
		}

		if (canvas != null) {
			canvas.repaint();
		}
	}

	private static void openWorkspace() {
		synchronized (workspaceSync) {

		}
	}

	private static void saveWorkspace() {
		synchronized (workspaceSync) {

		}
	}

	private static void destroyWorkspace() {
		synchronized (workspaceSync) {
			currentWorkspace = null;
		}

		if (canvas != null) {
			canvas.repaint();
		}
	}

	private static void quit() {
		synchronized (workspaceSync) {
			saveWorkspace();
			currentWorkspace = null;
			window.dispose();
		}
	}

	private static void createNewNode() {

	}

	private static void validateNodes() {

	}

	private static void createNewFunction() {

	}

	private static void validateFunction() {

	}

	private static void about() {

	}

	private static void manual() {

	}

}