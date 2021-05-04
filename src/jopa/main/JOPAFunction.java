package jopa.main;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;

import jopa.nodes.JOPAEndControlFlowNode;
import jopa.nodes.JOPANode;
import jopa.nodes.JOPAStartControlFlowNode;
import jopa.nodes.JOPAStatementNode;
import jopa.ports.JOPAPort;

public class JOPAFunction {

	public String name;
	public ArrayList<JOPANode> statements;
	public JOPAStartControlFlowNode startNode;
	public JOPAEndControlFlowNode endNode;

	public JOPAFunction(String name) {
		this.name = name;
		this.startNode = new JOPAStartControlFlowNode(new Rectangle(50, 50, 100, 100), "TEST_INPUT");
		this.endNode = new JOPAEndControlFlowNode(new Rectangle(650, 50, 100, 100), "TEST_OUTPUT");
		this.statements = new ArrayList<JOPANode>(
				Arrays.asList(new JOPAStatementNode(new Rectangle(350, 50, 100, 100), "STATEMENT_0", "TEST_STATEMENT"),
						new JOPAStatementNode(new Rectangle(350, 200, 100, 100), "STATEMENT_1", "TEST_STATEMENT"),
						new JOPAStatementNode(new Rectangle(350, 350, 100, 100), "STATEMENT_2", "TEST_EMPTY")));
	}

	public void draw(Graphics2D g, JOPANode selectedNode, JOPAPort selectedPort) {
		statements.forEach(node -> node.draw(g, selectedNode, selectedPort));
		startNode.draw(g, selectedNode, selectedPort);
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

		return null;
	}

	public JOPAPort getPortOnPoint(Point p) {
		for (JOPANode node : statements) {
			JOPAPort port = node.hitPort(p);
			if (port != null) {
				return port;
			}
		}

		if (startNode != null) {
			JOPAPort port = startNode.hitPort(p);
			if (port != null) {
				return port;
			}
		}

		if (endNode != null) {
			JOPAPort port = endNode.hitPort(p);
			if (port != null) {
				return port;
			}
		}

		return null;
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