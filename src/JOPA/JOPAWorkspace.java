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

	public JOPAPort selectedPort;
	public JOPANode selectedNode;
	public JOPANode draggingNode;
	public boolean isDragging;
	public Point prevPoint;

	public void draw(Graphics2D g) {
		nodes.forEach(node -> node.draw(g, selectedNode, selectedPort));
	}

	public void press(Point p) {
		JOPANode node = getNodeOnPoint(p);
		if (node != null) {
			selectedNode = node;
			nodes.remove(node);
			nodes.add(node);
			isDragging = true;
			prevPoint = p;
		}
	}

	public void release(Point p) {
		isDragging = false;
		selectedNode = null;
	}

	public void click(Point p) {
		JOPAPort port = getPortOnPoint(p);
		if (port != null) {
			selectedNode = null;
			if (selectedPort == null) {
				selectedPort = port;
			} else {
				if (selectedPort.output != port.output) {
					makeConnection(selectedPort, port);
					selectedPort = null;
				}
			}
		} else {
			JOPANode node = getNodeOnPoint(p);
			if (node != null) {
				selectedPort = null;
			}
		}
	}

	public void moved(Point p) {
		if (selectedNode != null) {
			selectedPort = null;
			selectedNode.move(p.x - prevPoint.x, p.y - prevPoint.y);
			prevPoint = p;
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

	private void makeConnection(JOPAPort from, JOPAPort to) {
		from.connections.add(to);
		to.connections.add(from);
	}

}