package JOPA;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class JOPAWorkspace {

	public ArrayList<JOPANode> nodes = new ArrayList<JOPANode>(
			List.of(new JOPANode(new Rectangle(100, 100, 100, 100), "HEADER", "TEST_0", "();();foobar"),
					new JOPANode(new Rectangle(300, 100, 100, 100), "HEADER", "TEST_1", "();();foobar")));

	public JOPANode selectedNode;
	public boolean isDragging;
	public Point pressPoint;

	public void draw(Graphics2D g) {
		nodes.forEach(node -> node.draw(g, node == selectedNode));
	}

	public void press(Point p) {
		JOPANode node = getNodeOnPoint(p);
		if (node != null) {
			selectedNode = node;
			nodes.remove(node);
			nodes.add(node);
			isDragging = true;
			pressPoint = new Point(node.rect.x - p.x, node.rect.y - p.y);
		}
	}

	public void release(Point p) {
		isDragging = false;
		selectedNode = null;
	}

	public void click(Point p) {
		JOPAPort port = getPortOnPoint(p);
		if (port != null) {
			System.out.println("Port");
		} else {
			JOPANode node = getNodeOnPoint(p);
			if (node != null) {
				System.out.println("Node");
			}
		}
	}

	public void moved(Point p) {
		if (selectedNode != null) {
			selectedNode.rect.x = p.x + pressPoint.x;
			selectedNode.rect.y = p.y + pressPoint.y;
		}
	}

	private JOPANode getNodeOnPoint(Point p) {
		for (JOPANode node : nodes) {
			if (node.hit(p)) {
				return node;
			}
		}

		return null;
	}

	private JOPAPort getPortOnPoint(Point p) {
		for (JOPANode node : nodes) {
			JOPAPort port = node.hitPort(p);
			if (port != null) {
				return port;
			}
		}

		return null;
	}

}