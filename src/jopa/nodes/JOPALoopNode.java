package jopa.nodes;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import jopa.ports.JOPAControlPort;
import jopa.ports.JOPAPort;

public class JOPALoopNode extends JOPAStatementNode {

	public JOPAControlPort loopIterationFlow;

	public JOPALoopNode(Rectangle rect, String header, String template) {
		super(rect, header, template);
	}

	public JOPALoopNode(Rectangle rect, String template) {
		super(rect, "LOOP", template);
	}

	@Override
	protected void init() {
		loopIterationFlow = new JOPAControlPort(this, "loop iteration", true);
	}

	@Override
	public boolean check() {
		if (flowInconsistency()) {
			return false;
		}

		return super.check();
	}

	@Override
	protected boolean flowInconsistency() {
		return loopIterationFlow.connections.size() != 1;
	}

	@Override
	public void draw(Graphics2D g, JOPANode selectedNode, JOPAPort selectedPort) {
		super.draw(g, selectedNode, selectedPort);
	}

}