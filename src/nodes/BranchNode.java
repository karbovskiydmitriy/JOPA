package nodes;

import static app.Function.NEW_LINE;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import app.Function;
import app.Variable;
import ports.BranchPort;
import ports.ControlPort;
import ports.Port;
import types.GLSLType;

public class BranchNode extends Node {

	private static final long serialVersionUID = -1257820350964560822L;

	public ControlPort incomingControlFlow;
	public ControlPort outcomingControlFlow;
	public ArrayList<BranchPort> branches;

	public BranchNode(Function function, Rectangle rect) {
		super(function, rect, "BRANCH");
	}

	public BranchNode(Function function, int x, int y) {
		super(function, x, y, "BRANCH");
	}

	@Override
	protected void init() {
		super.init();
		Variable input = new Variable(GLSLType.INT, "value");
		createPort(input, false, true);
		incomingControlFlow = new ControlPort(this, "in", false);
		outcomingControlFlow = new ControlPort(this, "out", true);
		branches = new ArrayList<BranchPort>();
	}

	@Override
	public boolean remove() {
		inputs.forEach(input -> input.destroyAllConnections());
		outputs.forEach(output -> output.destroyAllConnections());
		branches.forEach(branch -> branch.destroyAllConnections());
		inputs.clear();
		outputs.clear();
		branches.clear();

		return true;
	}

	@Override
	public boolean check() {
		if (flowInconsistency()) {
			return false;
		}
		if (inputs.size() != 1) {
			return false;
		}
		if (!inputsConnected()) {
			return false;
		}

		return true;
	}

	@Override
	public String generateCode() {
		String code = "";

		code += "switch(" + inputs.get(0).variable.name + ")" + NEW_LINE;
		code += "{" + NEW_LINE;
		for (BranchPort branch : branches) {
			code += "case " + branch.condition + ":" + NEW_LINE;
			Node branchNode = branch.connections.get(0).node;
			if (branchNode != null) {
				code += branchNode.generateCode() + NEW_LINE;
			}
			code += "break;" + NEW_LINE;
		}
		code += "}" + NEW_LINE;
		code += outcomingControlFlow.connections.get(0).node.generateCode();

		return code;
	}

	@Override
	protected boolean flowInconsistency() {
		if (incomingControlFlow.connections.size() == 0) {
			return true;
		}
		if (outcomingControlFlow.connections.size() != 1) {
			return true;
		}

		return true;
	}

	@Override
	public void draw(Graphics2D g, Node selectedNode, Port selectedPort) {
		super.draw(g, selectedNode, selectedPort);
		incomingControlFlow.draw(g, selectedPort);
		outcomingControlFlow.draw(g, selectedPort);
		for (BranchPort branch : branches) {
			branch.draw(g, selectedPort);
		}
	}

	@Override
	public void move(int x, int y) {
		super.move(x, y);
		incomingControlFlow.move(x, y);
		outcomingControlFlow.move(x, y);
		for (BranchPort branch : branches) {
			branch.move(x, y);
		}
	}

	@Override
	public Port hitPort(Point p) {
		if (incomingControlFlow.hit(p)) {
			return incomingControlFlow;
		}
		if (outcomingControlFlow.hit(p)) {
			return outcomingControlFlow;
		}
		for (BranchPort branch : branches) {
			if (branch.hit(p)) {
				return branch;
			}
		}

		return super.hitPort(p);
	}

	public void updateBranches() {

	}

}