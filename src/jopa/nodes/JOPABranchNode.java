package jopa.nodes;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import jopa.ports.JOPAControlPort;
import jopa.ports.JOPAPort;

public class JOPABranchNode extends JOPANode {

	private static final long serialVersionUID = -1257820350964560822L;

	public ArrayList<JOPAControlPort> branches;

	public JOPABranchNode(Rectangle rect) {
		super(rect, "BRANCH");
	}

	public JOPABranchNode(int x, int y) {
		super(x, y, "BRANCH");
	}

	@Override
	protected void init() {
		super.init();

		branches = new ArrayList<JOPAControlPort>();
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

		// TODO check

		return true;
	}

	@Override
	public String generateCode() {
		// TODO generateCode

		return null;
	}

	@Override
	protected boolean flowInconsistency() {
		// TODO flowInconsistency

		return true;
	}

	@Override
	public void draw(Graphics2D g, JOPANode selectedNode, JOPAPort selectedPort) {
		super.draw(g, selectedNode, selectedPort);
	}

}