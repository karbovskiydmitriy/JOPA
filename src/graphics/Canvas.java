package graphics;

import static app.Main.currentProject;
import static app.Main.gui;
import static app.Main.settings;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JPanel;

public class Canvas extends JPanel {

	private static final long serialVersionUID = -5988361450482572777L;

	private MyGraphics2D graphics2D;

	public Canvas() {
		setDoubleBuffered(true);
		setFocusable(true);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// synchronized (projectSync) {
				if (currentProject != null) {
					currentProject.mousePressed(getChangedPoint(e.getPoint()));
					repaint();
				}
				// }
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// synchronized (projectSync) {
				if (currentProject != null) {
					currentProject.mouseReleased(getChangedPoint(e.getPoint()));
					repaint();
				}
				// }
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// synchronized (projectSync) {
				if (currentProject != null) {
					currentProject.mouseClicked(getChangedPoint(e.getPoint()));
					repaint();
				}
				// }
			}
		});
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				// synchronized (projectSync) {
				if (currentProject != null) {
					currentProject.mouseMoved(getChangedPoint(e.getPoint()));
					repaint();
				}
				// }
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				// synchronized (projectSync) {
				if (currentProject != null) {
					currentProject.mouseMoved(getChangedPoint(e.getPoint()));
					repaint();
				}
				// }
			}
		});
		addMouseWheelListener(new MouseAdapter() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				float rotation = (float) e.getPreciseWheelRotation();
				if (e.getModifiers() == MouseWheelEvent.CTRL_DOWN_MASK) {
					rotation *= 2.0f;
				}
				graphics2D.scale += rotation / 10;
				if (graphics2D.scale <= 0.1f) {
					graphics2D.scale = 0.1f;
				} else if (graphics2D.scale >= 10.0f) {
					graphics2D.scale = 10.0f;
				}
				gui.repaint();
			}
		});
		graphics2D = new MyGraphics2D();
	}

	public Point getChangedPoint(Point p) {
		return new Point((int) (p.x * graphics2D.scale), (int) (p.y * graphics2D.scale));
	}

	@Override
	public void paint(Graphics g) {
		graphics2D.implementation = (Graphics2D) g;
		g.setColor(settings.defaultPalette.backgroundColor);
		g.fillRect(0, 0, getWidth(), getHeight());
		// synchronized (Main.projectSync) {
		if (currentProject != null) {
			currentProject.draw((Graphics2D) graphics2D);
		}
		// }
	}

}