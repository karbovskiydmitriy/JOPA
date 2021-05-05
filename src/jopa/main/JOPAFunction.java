package jopa.main;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

import jopa.nodes.JOPAConstantsNode;
import jopa.nodes.JOPAEndControlNode;
import jopa.nodes.JOPANode;
import jopa.nodes.JOPAStartControlNode;
import jopa.nodes.JOPAStatementNode;
import jopa.ports.JOPAPort;

public class JOPAFunction {

	public String name;
	public JOPAStartControlNode startNode;
	public JOPAEndControlNode endNode;
	public JOPAConstantsNode constantsNode;
	public ArrayList<JOPANode> statements;

	public JOPAFunction(String name) {
		this.name = name;
		this.startNode = new JOPAStartControlNode(50, 50, "FRAGMENT_INPUT");
		this.endNode = new JOPAEndControlNode(650, 50, "FRAGMENT_OUTPUT");
		this.constantsNode = new JOPAConstantsNode(50, 200, "CONSTANTS");
		this.statements = new ArrayList<JOPANode>(Arrays.asList(new JOPAStatementNode(350, 50, "FRAGMENT_TEST")));
	}

	public void draw(Graphics2D g, JOPANode selectedNode, JOPAPort selectedPort) {
		statements.forEach(node -> node.draw(g, selectedNode, selectedPort));
		startNode.draw(g, selectedNode, selectedPort);
		constantsNode.draw(g, selectedNode, selectedPort);
		endNode.draw(g, selectedNode, selectedPort);
	}

	public JOPANode getNodeOnPoint(Point p) {
		for (JOPANode node : statements) {
			if (node.hit(p)) {
				return node;
			}
		}
		if (startNode.hit(p)) {
			return startNode;
		}
		if (endNode.hit(p)) {
			return endNode;
		}
		if (constantsNode.hit(p)) {
			return constantsNode;
		}

		return null;
	}

	public JOPAPort getPortOnPoint(Point p) {
		JOPAPort port;

		for (JOPANode node : statements) {
			port = node.hitPort(p);
			if (port != null) {
				return port;
			}
		}
		port = startNode.hitPort(p);
		if (port != null) {
			return port;
		}
		port = endNode.hitPort(p);
		if (port != null) {
			return port;
		}
		port = constantsNode.hitPort(p);
		if (port != null) {
			return port;
		}

		return null;
	}

	public boolean removeNode(JOPANode node) {
		if (node.remove()) {
			if (statements.remove(node)) {
				return true;
			}
		}

		return false;
	}

	public boolean verifyNodes() {
		if (endNode != null) {
			if (!endNode.inputsConnected()) {
				return false;
			}
		}

		return true;
	}

}