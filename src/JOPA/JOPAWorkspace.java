package JOPA;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public class JOPAWorkspace {

	public String name;
	public ArrayList<JOPAFunction> functions;

	public JOPAFunction currentFunction;
	public JOPAPort selectedPort;
	public JOPANode selectedNode;
	public JOPANode draggingNode;
	public boolean isDragging;
	public Point prevPoint;

	public JOPAWorkspace(String name) {
		this.name = name;
		this.functions = new ArrayList<JOPAFunction>();
		this.currentFunction = new JOPAFunction("main");
		this.functions.add(currentFunction);
	}

	public void draw(Graphics2D g) {
		functions.forEach(function -> function.draw(g, selectedNode, selectedPort));
	}

	public void mousePressed(Point p) {
		JOPANode node = getNodeOnPoint(p);
		if (node != null) {
			selectedNode = node;
			isDragging = true;
			prevPoint = p;
		}
	}

	public void mouseReleased(Point p) {
		isDragging = false;
		selectedNode = null;
	}

	public void mouseClicked(Point p) {
		System.out.println("clicked");
		JOPAPort port = getPortOnPoint(p);
		if (port != null) {
			System.out.println("port");
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

	public void mouseMoved(Point p) {
		if (selectedNode != null) {
			selectedPort = null;
			selectedNode.move(p.x - prevPoint.x, p.y - prevPoint.y);
			prevPoint = p;
		}
	}

	public void keyTyped(int keyCode) {
		switch (keyCode) {
		case 9:
			if (selectedNode != null) {
				functions.forEach(function -> function.nodes.remove(selectedNode));
			}
			break;
		default:
			break;
		}
	}

	private JOPANode getNodeOnPoint(Point p) {
		if (currentFunction != null) {
			return currentFunction.getNodeOnPoint(p);
		}

		return null;
	}

	private JOPAPort getPortOnPoint(Point p) {
		if (currentFunction != null) {
			return currentFunction.getPortOnPoint(p);
		}

		return null;
	}

	private void makeConnection(JOPAPort from, JOPAPort to) {
		from.connections.add(to);
		to.connections.add(from);
	}

}