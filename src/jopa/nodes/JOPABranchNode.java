package jopa.nodes;

import static jopa.main.JOPAFunction.NEW_LINE;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import jopa.main.JOPAVariable;
import jopa.ports.JOPABranchPort;
import jopa.ports.JOPAControlPort;
import jopa.ports.JOPAPort;
import jopa.types.JOPAGLSLType;

public class JOPABranchNode extends JOPANode {

	private static final long serialVersionUID = -1257820350964560822L;

	public JOPAControlPort incomingControlFlow;
	public JOPAControlPort outcomingControlFlow;
	public ArrayList<JOPABranchPort> branches;

	public JOPABranchNode(Rectangle rect) {
		super(rect, "BRANCH");
	}

	public JOPABranchNode(int x, int y) {
		super(x, y, "BRANCH");
	}

	@Override
	protected void init() {
		super.init();
		JOPAVariable input = new JOPAVariable(JOPAGLSLType.INT, "value");
		createPort(input, false, true);
		incomingControlFlow = new JOPAControlPort(this, "in", false);
		outcomingControlFlow = new JOPAControlPort(this, "out", true);
		branches = new ArrayList<JOPABranchPort>();
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
		for (JOPABranchPort branch : branches) {
			code += "case " + branch.condition + ":" + NEW_LINE;
			JOPANode branchNode = branch.connections.get(0).node;
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
	public void draw(Graphics2D g, JOPANode selectedNode, JOPAPort selectedPort) {
		super.draw(g, selectedNode, selectedPort);
		incomingControlFlow.draw(g, selectedPort);
		outcomingControlFlow.draw(g, selectedPort);
		for (JOPABranchPort branch : branches) {
			branch.draw(g, selectedPort);
		}
	}

	@Override
	public void move(int x, int y) {
		super.move(x, y);
		incomingControlFlow.move(x, y);
		outcomingControlFlow.move(x, y);
		for (JOPABranchPort branch : branches) {
			branch.move(x, y);
		}
	}

	@Override
	public JOPAPort hitPort(Point p) {
		if (incomingControlFlow.hit(p)) {
			return incomingControlFlow;
		}
		if (outcomingControlFlow.hit(p)) {
			return outcomingControlFlow;
		}
		for (JOPABranchPort branch : branches) {
			if (branch.hit(p)) {
				return branch;
			}
		}

		return super.hitPort(p);
	}

	public void updateBranches() {

	}

}