package JOPA;

import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
		setupMenu();
		setupCanvas();
	}

	private static boolean checkVersion() {

//		if (!glProfile.isGL4()) {
//			System.out.println("OpenGL 4.0 is required to run compute shaders!");
//
//			if (!glProfile.isGL2()) {
//				System.out.println("OpenGL 2.0 is required to access extensions!");
//
//				return false;
//			}
//		}

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

	private static void setupMenu() {
		MenuBar menuBar = new MenuBar();

		{
			Menu fileMenu = new Menu("file");

			MenuItem newFileMenuItem = new MenuItem("new");
			MenuItem openFileMenuItem = new MenuItem("open");
			MenuItem saveFileMenuItem = new MenuItem("save");
			MenuItem saveAsFileMenuItem = new MenuItem("save as");
			MenuItem closeFileMenuItem = new MenuItem("close");
			MenuItem quitFileMenuItem = new MenuItem("quit");

			newFileMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					createNewWorkspace();
				}
			});

			closeFileMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					destroyWorkspace();
				}
			});

			quitFileMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					window.dispose();
				}
			});

			fileMenu.add(newFileMenuItem);
			fileMenu.add(openFileMenuItem);
			fileMenu.add(saveFileMenuItem);
			fileMenu.add(saveAsFileMenuItem);
			fileMenu.add(closeFileMenuItem);
			fileMenu.addSeparator();
			fileMenu.add(quitFileMenuItem);

			menuBar.add(fileMenu);
		}

//		{
//			Menu editMenu = new Menu("edit");
//
//			MenuItem undoEditMenuItem = new MenuItem("undo");
//			MenuItem redoEditMenuItem = new MenuItem("redo");
//
//			editMenu.add(undoEditMenuItem);
//			editMenu.add(redoEditMenuItem);
//
//			menuBar.add(editMenu);
//		}

		{
			Menu nodesMenu = new Menu("nodes");

			MenuItem createNewNodeMenuItem = new MenuItem("create new node");
			MenuItem validateNodesMenuItem = new MenuItem("validate nodes");

			nodesMenu.add(createNewNodeMenuItem);
			nodesMenu.add(validateNodesMenuItem);

			menuBar.add(nodesMenu);
		}

		{
			Menu helpMenu = new Menu("help");

			MenuItem aboutHelpMenuItem = new MenuItem("about");

			helpMenu.add(aboutHelpMenuItem);

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

	private static void destroyWorkspace() {
		synchronized (workspaceSync) {
			currentWorkspace = null;
		}

		if (canvas != null) {
			canvas.repaint();
		}
	}

}