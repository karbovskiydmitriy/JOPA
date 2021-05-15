package jopa.nodes;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import jopa.ports.JOPAControlPort;
import jopa.ports.JOPAPort;

//DECIDE remove
public class JOPALoopNode extends JOPAStatementNode {

	private static final long serialVersionUID = 6933041211578604855L;
	
	public JOPAControlPort loopIterationFlow;

	public JOPALoopNode(Rectangle rect, String template) {
		super(rect, "LOOP", template);
	}
	
	public JOPALoopNode(int x, int y, String template) {
		super(x, y, "LOOP", template);
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