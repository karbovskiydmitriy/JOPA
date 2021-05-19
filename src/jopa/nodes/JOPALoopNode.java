package jopa.nodes;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import jopa.main.JOPAFunction;
import jopa.ports.JOPAControlPort;
import jopa.ports.JOPAPort;

//DECIDE remove
public class JOPALoopNode extends JOPAStatementNode {

	private static final long serialVersionUID = 6933041211578604855L;

	public JOPAControlPort loopIterationFlow;

	public JOPALoopNode(JOPAFunction function, Rectangle rect, String template) {
		super(function, rect, "LOOP", template);
	}

	public JOPALoopNode(JOPAFunction function, int x, int y, String template) {
		super(function, x, y, "LOOP", template);
	}

	@Override
	protected void init() {
		super.init();
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