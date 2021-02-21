package JOPA;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class JOPACanvas extends Canvas {

	private static final long serialVersionUID = -5988361450482572777L;

	public JOPAWorkspace workspace;

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		workspace.draw((Graphics2D) (g));
	}

}