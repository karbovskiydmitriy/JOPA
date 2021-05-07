package jopa.graphics;

import static jopa.main.JOPAMain.currentWorkspace;
import static jopa.main.JOPAMain.workspaceSync;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JPanel;

import jopa.main.JOPAMain;

public class JOPACanvas extends JPanel {

	private static final long serialVersionUID = -5988361450482572777L;

	private JOPAGraphics2D graphics2D;

	public JOPACanvas() {
		setDoubleBuffered(true);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				synchronized (workspaceSync) {
					if (currentWorkspace != null) {
						currentWorkspace.mousePressed(getChangedPoint(e.getPoint()));
						repaint();
					}
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				synchronized (workspaceSync) {
					if (currentWorkspace != null) {
						currentWorkspace.mouseReleased(getChangedPoint(e.getPoint()));
						repaint();
					}
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				synchronized (workspaceSync) {
					if (currentWorkspace != null) {
						currentWorkspace.mouseClicked(getChangedPoint(e.getPoint()));
						repaint();
					}
				}
			}
		});
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				synchronized (workspaceSync) {
					if (currentWorkspace != null) {
						currentWorkspace.mouseMoved(getChangedPoint(e.getPoint()));
						repaint();
					}
				}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				synchronized (workspaceSync) {
					if (currentWorkspace != null) {
						currentWorkspace.mouseMoved(getChangedPoint(e.getPoint()));
						repaint();
					}
				}
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
				JOPAMain.ui.repaint();
			}
		});

		graphics2D = new JOPAGraphics2D();
	}

	public Point getChangedPoint(Point p) {
		return new Point((int) (p.x * graphics2D.scale), (int) (p.y * graphics2D.scale));
	}

	@Override
	public void paint(Graphics g) {
		graphics2D.implementation = (Graphics2D) g;
		g.setColor(JOPAMain.settings.defaultPalette.backgroundColor);
		g.fillRect(0, 0, getWidth(), getHeight());
		synchronized (JOPAMain.workspaceSync) {
			if (JOPAMain.currentWorkspace != null) {
				JOPAMain.currentWorkspace.draw((Graphics2D) graphics2D);
			}
		}
	}

}