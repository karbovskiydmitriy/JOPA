package jopa;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import jopa.ui.JOPACanvas;

public class JOPAFunction {

	public String name;
	public ArrayList<JOPANode> inputs;
	public ArrayList<JOPANode> statements;
	public ArrayList<JOPANode> outputs;
	public JOPACanvas canvas;

	public JOPAFunction(String name) {
		this.name = name;
		this.inputs = new ArrayList<JOPANode>(
				List.of(new JOPAInputNode(new Rectangle(50, 50, 100, 100), "INPUT", "INPUT_0", "();foobar")));
		this.statements = new ArrayList<JOPANode>(
				List.of(new JOPANode(new Rectangle(250, 50, 100, 100), "EXECUTION", "STATEMENT_0", "();foobar;()"),
						new JOPANode(new Rectangle(250, 200, 100, 100), "EXECUTION", "STATEMENT_1", "();foobar;()"),
						new JOPANode(new Rectangle(250, 350, 100, 100), "EXECUTION", "STATEMENT_2", "();foobar;()")));
		this.outputs = new ArrayList<JOPANode>(
				List.of(new JOPAOutputNode(new Rectangle(450, 50, 100, 100), "OUTPUT", "OUTPUT_0", "();foobar")));
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

}