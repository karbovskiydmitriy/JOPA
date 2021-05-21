package nodes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import app.Function;
import ports.ControlPort;
import ports.Port;

public class StartNode extends Node {

	private static final long serialVersionUID = 4818138558904580829L;

	public ControlPort flowStart;

	public StartNode(Function function, Rectangle rect, String template) {
		super(function, rect, "Start", template);
	}

	public StartNode(Function function, int x, int y, String template) {
		super(function, x, y, "Start", template);
	}

	@Override
	protected void init() {
		super.init();
		this.flowStart = new ControlPort(this, "start", true);
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
		String chainCode = flowStart.generateCode();

		return chainCode;
	}

	@Override
	public boolean remove() {
		return false;
	}

	@Override
	protected boolean flowInconsistency() {
		return flowStart.connections.size() != 1;
	}

	@Override
	public void draw(Graphics2D g, Node selectedNode, Port selectedPort) {
		super.draw(g, selectedNode, selectedPort);
		flowStart.draw(g, selectedPort);
	}

	@Override
	public void move(int x, int y) {
		super.move(x, y);
		flowStart.move(x, y);
	}

	@Override
	public Port hitPort(Point p) {
		if (flowStart.hit(p)) {
			return flowStart;
		}

		return super.hitPort(p);
	}

}