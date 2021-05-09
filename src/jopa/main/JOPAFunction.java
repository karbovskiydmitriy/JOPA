package jopa.main;

import static jopa.util.JOPATypeUtil.getNameForType;

import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import jopa.nodes.JOPAConstantsNode;
import jopa.nodes.JOPAEndNode;
import jopa.nodes.JOPANode;
import jopa.nodes.JOPAStartNode;
import jopa.nodes.JOPAStatementNode;
import jopa.ports.JOPAPort;
import jopa.types.JOPAGLSLType;

public class JOPAFunction implements Serializable, JOPACodeConvertible {

	private static final long serialVersionUID = -8041471689492839874L;

	public static final String TAB = "\t";
	public static final String NEW_LINE = "\n";
	public static final String TWO_LINES = "\n\n";
	public static final String BLOCK_START = "\n{\n";
	public static final String BLOCK_END = "}\n";

	public String name;
	public JOPAGLSLType returnType;
	public ArrayList<JOPAVariable> args;
	public JOPAStartNode startNode;
	public JOPAEndNode endNode;
	public JOPAConstantsNode constantsNode;
	public ArrayList<JOPANode> statementNodes;

	public JOPAFunction(String name) {
		this.name = name;
		this.returnType = JOPAGLSLType.JOPA_VOID;
		this.args = new ArrayList<JOPAVariable>();
		this.startNode = new JOPAStartNode(50, 50, "FRAGMENT_INPUT");
		this.endNode = new JOPAEndNode(650, 50, "FRAGMENT_OUTPUT");
		this.constantsNode = new JOPAConstantsNode(50, 200, "CONSTANTS");
		this.statementNodes = new ArrayList<JOPANode>(Arrays.asList(new JOPAStatementNode(350, 50, "FRAGMENT_TEST")));
		init();
	}

	private void init() {
		JOPAStatementNode statement = (JOPAStatementNode) statementNodes.get(0);
		startNode.flowStart.makeConnection(statement.incomingControlFlow);
		statement.outcomingControlFlow.makeConnection(endNode.flowEnd);
		startNode.outputs.get(0).makeConnection(statement.inputs.get(0));
		startNode.outputs.get(2).makeConnection(statement.inputs.get(1));
		startNode.outputs.get(1).makeConnection(statement.inputs.get(2));
		statement.outputs.get(0).makeConnection(endNode.inputs.get(0));
	}

	public void draw(Graphics2D g, JOPANode selectedNode, JOPAPort selectedPort) {
		statementNodes.forEach(node -> node.draw(g, selectedNode, selectedPort));
		startNode.draw(g, selectedNode, selectedPort);
		constantsNode.draw(g, selectedNode, selectedPort);
		endNode.draw(g, selectedNode, selectedPort);
	}

	public JOPANode getNodeOnPoint(Point p) {
		for (JOPANode node : statementNodes) {
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

		for (JOPANode node : statementNodes) {
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
			if (statementNodes.remove(node)) {
				return true;
			}
		}

		return false;
	}

	public boolean verifyNodes() {
		if (!startNode.check()) {
			return false;
		}
		if (!endNode.check()) {
			return false;
		}
		for (JOPANode statementNode : statementNodes) {
			if (!statementNode.check()) {
				return false;
			}
		}

		return true;
	}

	public String getPrototype() {
		if (returnType == null) {
			return null;
		}
		if (name == null || name.length() == 0) {
			return null;
		}

		String prototypeText = getNameForType(returnType) + " " + name + "(";
		for (int i = 0; i < args.size(); i++) {
			JOPAVariable arg = args.get(i);
			prototypeText += arg.toString();
			boolean notTheLast = i < args.size() - 1;
			if (notTheLast) {
				prototypeText += ", ";
			}
		}
		prototypeText += ")";

		return prototypeText;
	}

	public String generateCode() {
		if (!verifyNodes()) {
			return null;
		}

		String code = getPrototype();
		code += BLOCK_START;
		code += format(startNode.generateCode());
		code += BLOCK_END;

		return code;
	}

	private static String getTabs(int count) {
		char[] tabs = new char[count];
		Arrays.fill(tabs, '\t');

		return new String(tabs);
	}

	private static String format(String text) {
		String[] lines = text.replaceAll(";", ";" + NEW_LINE).toString().split(NEW_LINE);
		String code = "";
		for (String line : lines) {
			code += getTabs(1) + line + NEW_LINE;
		}

		return code.toString();
	}

}