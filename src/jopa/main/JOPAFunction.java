package jopa.main;

import static jopa.main.JOPAMain.currentProject;
import static jopa.util.JOPATypeUtil.getNameForType;

import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import jopa.nodes.JOPAConstantsNode;
import jopa.nodes.JOPADefinesNode;
import jopa.nodes.JOPAEndNode;
import jopa.nodes.JOPAGlobalsNode;
import jopa.nodes.JOPANode;
import jopa.nodes.JOPAStartNode;
import jopa.nodes.JOPAStatementNode;
import jopa.nodes.JOPATypesNode;
import jopa.ports.JOPAPort;
import jopa.types.JOPAGLSLType;

public class JOPAFunction implements Serializable {

	private static final long serialVersionUID = -8041471689492839874L;

	public static final String TAB = "\t";
	public static final String NEW_LINE = "\n";
	public static final String TWO_LINES = "\n\n";
	public static final String BLOCK_START = "\n{\n";
	public static final String BLOCK_END = "}\n";

	public boolean isCustom;
	public JOPAGLSLType returnType;
	public String name;
	public String text;
	public ArrayList<JOPAVariable> args;
	public JOPADefinesNode definesNode;
	public JOPATypesNode typesNode;
	public JOPAConstantsNode constantsNode;
	public JOPAGlobalsNode globalsNode;
	public JOPAStartNode startNode;
	public JOPAEndNode endNode;
	public ArrayList<JOPANode> statementNodes;

	public JOPAFunction(String name, JOPAGLSLType returnType, boolean isCustom, String text, JOPAVariable... args) {
		this.isCustom = isCustom;
		this.returnType = returnType;
		this.name = name;
		this.text = text;
		this.args = new ArrayList<JOPAVariable>(Arrays.asList(args));
		if (isCustom) {
			this.definesNode = new JOPADefinesNode(200, 200);
			this.typesNode = new JOPATypesNode(50, 200);
			this.constantsNode = new JOPAConstantsNode(50, 350);
			this.globalsNode = new JOPAGlobalsNode(200, 350);
			this.statementNodes = new ArrayList<JOPANode>();
			setupInitialNodes();
		}
	}

	private void setupInitialNodes() {
		switch (currentProject.projectType) {
		case FRAGMENT: {
			statementNodes.add(new JOPAStatementNode(350, 50, "FRAGMENT_TEST"));
			// statementNodes.add(new JOPABranchNode(350, 200));
			this.startNode = new JOPAStartNode(50, 50, "FRAGMENT_INPUT");
			this.endNode = new JOPAEndNode(650, 50, "FRAGMENT_OUTPUT");
			JOPAStatementNode statement = (JOPAStatementNode) statementNodes.get(0);
			startNode.flowStart.makeConnection(statement.incomingControlFlow);
			statement.outcomingControlFlow.makeConnection(endNode.flowEnd);
			startNode.outputs.get(0).makeConnection(statement.inputs.get(0));
			startNode.outputs.get(1).makeConnection(statement.inputs.get(1));
			startNode.outputs.get(2).makeConnection(statement.inputs.get(2));
			statement.outputs.get(0).makeConnection(endNode.inputs.get(0));
		}
			break;
		case COMPUTE: {
			statementNodes.add(new JOPAStatementNode(350, 50, "COMPUTE_TEST"));
			// statementNodes.add(new JOPABranchNode(350, 200));
			this.startNode = new JOPAStartNode(50, 50, "COMPUTE_INPUT");
			this.endNode = new JOPAEndNode(650, 50, "COMPUTE_OUTPUT");
			JOPAStatementNode statement = (JOPAStatementNode) statementNodes.get(0);
			startNode.flowStart.makeConnection(statement.incomingControlFlow);
			statement.outcomingControlFlow.makeConnection(endNode.flowEnd);
			startNode.outputs.get(0).makeConnection(statement.inputs.get(0));
			startNode.outputs.get(1).makeConnection(statement.inputs.get(1));
			startNode.outputs.get(2).makeConnection(statement.inputs.get(2));
			statement.outputs.get(0).makeConnection(endNode.inputs.get(0));
		}
			break;
		default:
			break;
		}
	}

	public void draw(Graphics2D g, JOPANode selectedNode, JOPAPort selectedPort) {
		definesNode.draw(g, selectedNode, selectedPort);
		typesNode.draw(g, selectedNode, selectedPort);
		constantsNode.draw(g, selectedNode, selectedPort);
		globalsNode.draw(g, selectedNode, selectedPort);
		startNode.draw(g, selectedNode, selectedPort);
		endNode.draw(g, selectedNode, selectedPort);
		statementNodes.forEach(node -> node.draw(g, selectedNode, selectedPort));
	}

	public JOPANode getNodeOnPoint(Point p) {
		for (JOPANode node : statementNodes) {
			if (node.hit(p)) {
				return node;
			}
		}
		if (definesNode.hit(p)) {
			return definesNode;
		}
		if (typesNode.hit(p)) {
			return typesNode;
		}
		if (constantsNode.hit(p)) {
			return constantsNode;
		}
		if (globalsNode.hit(p)) {
			return globalsNode;
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
		if (isCustom) {
			if (!verifyNodes()) {
				return null;
			}
		}

		String code = getPrototype();
		code += BLOCK_START;
		if (!isCustom) {
			code += format(text);
		} else {
			code += format(startNode.generateCode());
		}
		code += BLOCK_END;

		return code;
	}

	public void updateFunction() {
		// TODO updateFunction
	}

	private static String getTabs(int count) {
		char[] tabs = new char[count];
		Arrays.fill(tabs, '\t');

		return new String(tabs);
	}

	private static String format(String text) {
		text = text.replaceAll(NEW_LINE, "");
		text = text.replaceAll(";", ";" + NEW_LINE);
		String[] lines = text.split(NEW_LINE);
		String code = "";

		for (String line : lines) {
			code += getTabs(1) + line + NEW_LINE;
		}

		return code.toString();
	}

}