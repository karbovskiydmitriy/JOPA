package JOPA;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

public class JOPAPort {

	public boolean output;
	public JOPAPort connection;
	public JOPANode node;
	public Point position;
	public int radius;
	public String name;
	public JOPAType datatype;

	public JOPAPort(JOPANode node, Point position, int radius, String name, boolean isOutput) {
		this.node = node;
		this.position = position;
		this.radius = radius;
		this.name = name;
		this.output = isOutput;
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fillOval(node.rect.x + position.x - radius, node.rect.y + position.y - radius, radius * 2, radius * 2);
		FontRenderContext frc = g.getFontRenderContext();
		Font font = g.getFont();
		g.setColor(Color.BLACK);
		g.drawOval(node.rect.x + position.x - radius, node.rect.y + position.y - radius, radius * 2, radius * 2);
		if (!output) {
			Rectangle2D r = font.getStringBounds(name, frc);
			g.drawString(name, node.rect.x + position.x - (int) r.getWidth() - radius * 2,
					node.rect.y + position.y + radius);
		} else {
			g.drawString(name, node.rect.x + position.x + radius * 2, node.rect.y + position.y + radius);
		}
	}

	public boolean hit(Point p) {
		return (p.distance(node.rect.x + position.x, node.rect.y + position.y) <= radius);
	}

}