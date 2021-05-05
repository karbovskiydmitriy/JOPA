package jopa.nodes;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import jopa.ports.JOPAPort;

public class JOPABranchNode extends JOPANode {

	public JOPABranchNode(Rectangle rect, String header) {
		super(rect, header);
	}

	@Override
	protected void init() {
		// TODO
	}

	@Override
	public boolean remove() {
		// TODO

		return false;
	}

	@Override
	protected boolean check() {
		if (flowInconsistency()) {
			return false;
		}

		return true;
	}

	@Override
	protected boolean flowInconsistency() {
		// TODO

		return true;
	}

	@Override
	public void draw(Graphics2D g, JOPANode selectedNode, JOPAPort selectedPort) {
		super.draw(g, selectedNode, selectedPort);
	}

}