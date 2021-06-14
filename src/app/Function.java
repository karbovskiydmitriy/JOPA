package app;

import static app.Main.currentProject;
import static util.TypeUtil.getNameForType;

import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import nodes.ConstantsNode;
import nodes.DefinesNode;
import nodes.EndNode;
import nodes.FunctionNode;
import nodes.GlobalsNode;
import nodes.Node;
import nodes.StartNode;
import nodes.StatementNode;
import nodes.TypesNode;
import ports.Port;
import types.GLSLType;

public class Function implements Serializable {

	private static final long serialVersionUID = -8041471689492839874L;

	public static final String TAB = "\t";
	public static final String NEW_LINE = "\n";
	public static final String TWO_LINES = "\n\n";
	public static final String BLOCK_START = "\n{\n";
	public static final String BLOCK_END = "}\n";

	public boolean isCustom;
	public GLSLType returnType;
	public String name;
	public String text;
	public ArrayList<Variable> args;
	public DefinesNode definesNode;
	public TypesNode typesNode;
	public ConstantsNode constantsNode;
	public GlobalsNode globalsNode;
	public StartNode startNode;
	public EndNode endNode;
	public ArrayList<Node> statementNodes;

	public Function(String name, GLSLType returnType, boolean isCustom, String text, Variable... args) {
		this.isCustom = isCustom;
		this.returnType = returnType;
		this.name = name;
		this.text = text;
		this.args = new ArrayList<Variable>(Arrays.asList(args));
		if (isCustom) {
			this.definesNode = new DefinesNode(this, 200, 200);
			this.typesNode = new TypesNode(this, 50, 200);
			this.constantsNode = new ConstantsNode(this, 50, 350);
			this.globalsNode = new GlobalsNode(this, 200, 350);
			this.statementNodes = new ArrayList<Node>();
			setupInitialNodes();
		}
	}

	private void setupInitialNodes() {
		switch (currentProject.projectType) {
		case FRAGMENT: {
			statementNodes.add(new StatementNode(this, 350, 50, "FRAGMENT_TEST"));
			// statementNodes.add(new BranchNode(this, 350, 200));
			this.startNode = new StartNode(this, 50, 50, "FRAGMENT_INPUT");
			this.endNode = new EndNode(this, 650, 50, "FRAGMENT_OUTPUT");
			StatementNode statement = (StatementNode) statementNodes.get(0);
			startNode.flowStart.makeConnection(statement.incomingControlFlow);
			statement.outcomingControlFlow.makeConnection(endNode.flowEnd);
			startNode.outputs.get(0).makeConnection(statement.inputs.get(0));
			startNode.outputs.get(1).makeConnection(statement.inputs.get(1));
			startNode.outputs.get(2).makeConnection(statement.inputs.get(2));
			statement.outputs.get(0).makeConnection(endNode.inputs.get(0));
		}
			break;
		case COMPUTE: {
			statementNodes.add(new StatementNode(this, 350, 50, "COMPUTE_TEST"));
			// statementNodes.add(new BranchNode(this, 350, 200));
			this.startNode = new StartNode(this, 50, 50, "COMPUTE_INPUT");
			this.endNode = new EndNode(this, 650, 50, "COMPUTE_OUTPUT");
			StatementNode statement = (StatementNode) statementNodes.get(0);
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

	public void draw(Graphics2D g, Node selectedNode, Port selectedPort) {
		definesNode.draw(g, selectedNode, selectedPort);
		typesNode.draw(g, selectedNode, selectedPort);
		constantsNode.draw(g, selectedNode, selectedPort);
		globalsNode.draw(g, selectedNode, selectedPort);
		startNode.draw(g, selectedNode, selectedPort);
		endNode.draw(g, selectedNode, selectedPort);
		statementNodes.forEach(node -> node.draw(g, selectedNode, selectedPort));
	}

	public Node getNodeOnPoint(Point p) {
		for (Node node : statementNodes) {
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

	public Port getPortOnPoint(Point p) {
		Port port;

		for (Node node : statementNodes) {
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

	public boolean removeNode(Node node) {
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
		for (Node statementNode : statementNodes) {
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
			Variable arg = args.get(i);
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

	public void updateReferencingNodes() {
		currentProject.functions.forEach(currentFunction -> {
			if (currentFunction.isCustom) {
				currentFunction.statementNodes.forEach(node -> {
					if (node.getClass().equals(FunctionNode.class)) {
						FunctionNode functionNode = (FunctionNode) node;
						functionNode.applyFunction();
					}
				});
			}
		});
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