package jopa;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import jopa.nodes.JOPAInputNode;
import jopa.nodes.JOPANode;
import jopa.nodes.JOPAOutputNode;
import jopa.nodes.JOPAPort;

public class JOPAFunction {

	public String name;
	public ArrayList<JOPANode> inputs;
	public ArrayList<JOPANode> statements;
	public ArrayList<JOPANode> outputs;
//	public JOPACanvas canvas;

	public JOPAFunction(String name) {
		this.name = name;
		this.inputs = new ArrayList<JOPANode>(List.of(new JOPAInputNode(new Rectangle(50, 50, 100, 100), "INPUT_0")));
		this.statements = new ArrayList<JOPANode>(List.of(new JOPANode(new Rectangle(250, 50, 100, 100), "STATEMENT_0"),
				new JOPANode(new Rectangle(250, 200, 100, 100), "STATEMENT_1"),
				new JOPANode(new Rectangle(250, 350, 100, 100), "STATEMENT_2")));
		this.outputs = new ArrayList<JOPANode>(
				List.of(new JOPAOutputNode(new Rectangle(450, 50, 100, 100), "OUTPUT_0")));
	}

	public void draw(Graphics2D g, JOPANode selectedNode, JOPAPort selectedPort) {
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

	public JOPAPort getPortOnPoint(Point p) {
		for (JOPANode node : statements) {
			JOPAPort port = node.hitPort(p);
			if (port != null) {
				return port;
			}
		}
		for (JOPANode node : inputs) {
			JOPAPort port = node.hitPort(p);
			if (port != null) {
				return port;
			}
		}
		for (JOPANode node : outputs) {
			JOPAPort port = node.hitPort(p);
			if (port != null) {
				return port;
			}
		}

		return null;
	}

	public boolean verifyNodes() {
		if (outputs != null) {
			for (var node : outputs) {
				if (!node.inputsConnected()) {
//					System.out.println("function " + name + " nodes not OK");

					return false;
				}
			}
		}

//		System.out.println("function " + name + " OK");

		return true;
	}

}