package nodes;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import app.Function;
import ports.ControlPort;
import ports.Port;

//DECIDE remove
public class LoopNode extends StatementNode {

	private static final long serialVersionUID = 6933041211578604855L;

	public ControlPort loopIterationFlow;

	public LoopNode(Function function, Rectangle rect, String template) {
		super(function, rect, "LOOP", template);
	}

	public LoopNode(Function function, int x, int y, String template) {
		super(function, x, y, "LOOP", template);
	}

	@Override
	protected void init() {
		super.init();
		loopIterationFlow = new ControlPort(this, "loop iteration", true);
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
	public void draw(Graphics2D g, Node selectedNode, Port selectedPort) {
		super.draw(g, selectedNode, selectedPort);
	}

}