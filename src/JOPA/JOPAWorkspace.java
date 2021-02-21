package JOPA;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class JOPAWorkspace {

	public ArrayList<JOPANode> nodes = new ArrayList<JOPANode>(
			List.of(new JOPANode(new Rectangle(100, 100, 100, 100), "HEADER", "TEST", "();();foobar")));

	public JOPANode selectedNode;
	public boolean isDragging;

	public void draw(Graphics2D g) {
		nodes.forEach(node -> node.draw(g, true));
	}

	public void press(Point p) {
		JOPANode node = getNodeOnPoint(p);
		if (node != null) {
			selectedNode = node;
			isDragging = true;
		}
	}

	public void release(Point p) {
		isDragging = false;
	}

	public void click(Point p) {
		JOPANode node = getNodeOnPoint(p);
		if (node != null) {
			nodes.remove(node);
		}
	}

	private JOPANode getNodeOnPoint(Point p) {
		for (JOPANode node : nodes) {
			if (node.rect.contains(p)) {
				return node;
			}
		}

		return null;
	}

}