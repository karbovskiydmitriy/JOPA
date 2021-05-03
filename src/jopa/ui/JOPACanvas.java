package jopa.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import jopa.main.JOPAMain;

public class JOPACanvas extends JPanel {

	private static final long serialVersionUID = -5988361450482572777L;

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		synchronized (JOPAMain.workspaceSync) {
			if (JOPAMain.currentWorkspace != null) {
				JOPAMain.currentWorkspace.draw((Graphics2D) (g));
			}
		}
	}

}