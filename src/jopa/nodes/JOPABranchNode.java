package jopa.nodes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import jopa.ports.JOPABranchPort;
import jopa.ports.JOPAControlPort;
import jopa.ports.JOPAPort;

public class JOPABranchNode extends JOPANode {

	private static final long serialVersionUID = -1257820350964560822L;

	public JOPAControlPort incomingControlFlow;
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
		incomingControlFlow = new JOPAControlPort(this, "in", false);
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
		if (!inputsConnected()) {
			return false;
		}

		return true;
	}

	@Override
	public String generateCode() {
		// TODO generateCode

		return null;
	}

	@Override
	protected boolean flowInconsistency() {
		if (incomingControlFlow.connections.size() == 0) {
			return true;
		}
		for (JOPABranchPort branch : branches) {
			if (branch.connections.size() > 0) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void draw(Graphics2D g, JOPANode selectedNode, JOPAPort selectedPort) {
		super.draw(g, selectedNode, selectedPort);
		incomingControlFlow.draw(g, selectedPort);
		for (JOPABranchPort branch : branches) {
			branch.draw(g, selectedPort);
		}
	}

	@Override
	public void move(int x, int y) {
		super.move(x, y);
		incomingControlFlow.move(x, y);
		for (JOPABranchPort branch : branches) {
			branch.move(x, y);
		}
	}

	@Override
	public JOPAPort hitPort(Point p) {
		if (incomingControlFlow.hit(p)) {
			return incomingControlFlow;
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