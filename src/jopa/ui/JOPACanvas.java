package jopa.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;

import javax.swing.JPanel;

import jopa.main.JOPAGraphics2D;
import jopa.main.JOPAMain;

public class JOPACanvas extends JPanel {

	private static final long serialVersionUID = -5988361450482572777L;

	private float scale;
	private JOPAGraphics2D graphics2D;

	public JOPACanvas() {
		scale = 1.0f;

		addMouseWheelListener(new MouseAdapter() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				float rotation = (float) e.getPreciseWheelRotation();
				if (e.getModifiers() == MouseWheelEvent.CTRL_DOWN_MASK) {
					rotation *= 2.0f;
				}
				scale += rotation / 10;
				if (scale <= 0.1f) {
					scale = 0.1f;
				} else if (scale >= 10.0f) {
					scale = 10.0f;
				}
			}
		});

		graphics2D = new JOPAGraphics2D();
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