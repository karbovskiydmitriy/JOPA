package jopa.nodes;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import jopa.main.JOPACodeConvertible;
import jopa.ports.JOPAPort;

public class JOPABranchNode extends JOPANode implements JOPACodeConvertible {

	private static final long serialVersionUID = -1257820350964560822L;

	public JOPABranchNode(Rectangle rect) {
		super(rect, "BRANCH");
	}

	public JOPABranchNode(int x, int y) {
		super(x, y, "BRANCH");
	}

	@Override
	protected void init() {
		super.init();
		// TODO init
	}

	@Override
	public boolean remove() {
		// TODO remove

		return false;
	}

	@Override
	public boolean check() {
		if (flowInconsistency()) {
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
		// TODO flowInconsistency

		return true;
	}

	@Override
	public void draw(Graphics2D g, JOPANode selectedNode, JOPAPort selectedPort) {
		super.draw(g, selectedNode, selectedPort);
	}

}