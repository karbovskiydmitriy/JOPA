package JOPA;

import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.jogamp.opengl.GLProfile;

public class JOPAMain {

	public static void main(String[] args) {
		final GLProfile glProfile = GLProfile.get(GLProfile.GL4);

		if (!glProfile.isGL4()) {
			System.out.println("OpenGL 4.0 is required to run compute shaders!");

			if (!glProfile.isGL2()) {
				System.out.println("OpenGL 2.0 is required to access extensions!");

				return;
			}
		}

		final JOPAWorkspace workspace = new JOPAWorkspace();

		String title = "Java and OpenGL parallel application (JOPA) v1.0 by Karbovskiy Dmitriy (2020-2021)";
		Frame window = new Frame(title);
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				windowEvent.getWindow().dispose();
			}
		});

		window.setBounds(window.getGraphicsConfiguration().getBounds());
		window.setEnabled(true);
		window.setVisible(true);

		final JOPACanvas canvas = new JOPACanvas();
		canvas.workspace = workspace;
		canvas.setDoubleBuffered(true);
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				workspace.press(e.getPoint());
				canvas.repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				workspace.release(e.getPoint());
				canvas.repaint();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				workspace.click(e.getPoint());
				canvas.repaint();
			}
		});
		canvas.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				workspace.moved(e.getPoint());
				canvas.repaint();
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				workspace.moved(e.getPoint());
				canvas.repaint();
			}
		});
		window.add(canvas);
		canvas.setSize(window.getSize());
		canvas.repaint();

		MenuBar menuBar = new MenuBar();

		{
			Menu fileMenu = new Menu("file");

			MenuItem newFileMenuItem = new MenuItem("new");
			MenuItem openFileMenuItem = new MenuItem("open");
			MenuItem saveFileMenuItem = new MenuItem("save");
			MenuItem saveAsFileMenuItem = new MenuItem("save as");
			MenuItem closeFileMenuItem = new MenuItem("close");
			MenuItem quitFileMenuItem = new MenuItem("quit");

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

		{
			Menu editMenu = new Menu("edit");

			MenuItem undoEditMenuItem = new MenuItem("undo");
			MenuItem redoEditMenuItem = new MenuItem("redo");

			editMenu.add(undoEditMenuItem);
			editMenu.add(redoEditMenuItem);

			menuBar.add(editMenu);
		}

		{
			Menu helpMenu = new Menu("help");

			MenuItem aboutHelpMenuItem = new MenuItem("about");

			helpMenu.add(aboutHelpMenuItem);

			menuBar.add(helpMenu);
		}

		window.setMenuBar(menuBar);
	}

}