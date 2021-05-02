package jopa;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;

import jopa.nodes.JOPAInputNode;
import jopa.nodes.JOPANode;
import jopa.nodes.JOPAOutputNode;
import jopa.nodes.JOPAStatementNode;
import jopa.ports.JOPAControlFlowPort;
import jopa.ports.JOPADataPort;

public class JOPAFunction {

	public String name;
	public ArrayList<JOPANode> inputs;
	public ArrayList<JOPANode> statements;
	public ArrayList<JOPANode> outputs;

	public JOPAFunction(String name) {
		this.name = name;
		this.inputs = new ArrayList<JOPANode>(
				Arrays.asList(new JOPAInputNode(new Rectangle(50, 50, 100, 100), "INPUT_0", "INPUT")));
		this.statements = new ArrayList<JOPANode>(
				Arrays.asList(new JOPAStatementNode(new Rectangle(350, 50, 100, 100), "STATEMENT_0", "FOOBAR"),
						new JOPAStatementNode(new Rectangle(350, 200, 100, 100), "STATEMENT_1", "FOOBAR"),
						new JOPAStatementNode(new Rectangle(350, 350, 100, 100), "STATEMENT_2", "FOOBAR")));
		this.outputs = new ArrayList<JOPANode>(
				Arrays.asList(new JOPAOutputNode(new Rectangle(650, 50, 100, 100), "OUTPUT_0", "OUTPUT")));
	}

	public void draw(Graphics2D g, JOPANode selectedNode, JOPADataPort selectedPort) {
		inputs.forEach(node -> node.draw(g, selectedNode, selectedPort));
		statements.forEach(node -> node.draw(g, selectedNode, selectedPort));
		outputs.forEach(node -> node.draw(g, selectedNode, selectedPort));
	}

	public JOPANode getNodeOnPoint(Point p) {
		for (JOPANode node : statements) {
			if (node.hit(p)) {
				return node;
			}
		}
		for (JOPANode node : inputs) {
			if (node.hit(p)) {
				return node;
			}
		}
		for (JOPANode node : outputs) {
			if (node.hit(p)) {
				return node;
			}
		}

		return null;
	}

	public JOPADataPort getDataPortOnPoint(Point p) {
		for (JOPANode node : statements) {
			JOPADataPort port = node.hitPort(p);
			if (port != null) {
				return port;
			}
		}
		for (JOPANode node : inputs) {
			JOPADataPort port = node.hitPort(p);
			if (port != null) {
				return port;
			}
		}
		for (JOPANode node : outputs) {
			JOPADataPort port = node.hitPort(p);
			if (port != null) {
				return port;
			}
		}

		return null;
	}

	public JOPAControlFlowPort getControlFlowPortOnPoint(Point p) {
		return null; // TODO
	}

	public boolean verifyNodes() {
		if (outputs != null) {
			for (JOPANode node : outputs) {
				if (!node.inputsConnected()) {
					System.out.println("function " + name + " nodes not OK");

					return false;
				}
			}
		}

		System.out.println("function " + name + " OK");

		return true;
	}

}