package nodes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import app.Function;
import ports.ControlPort;
import ports.DataPort;
import ports.Port;

public class EndNode extends Node {

	private static final long serialVersionUID = -3030917953915734770L;

	public ControlPort flowEnd;

	public EndNode(Function function, Rectangle rect, String template) {
		super(function, rect, "End", template);
	}

	public EndNode(Function function, int x, int y, String template) {
		super(function, x, y, "End", template);
	}

	@Override
	protected void init() {
		super.init();
		this.flowEnd = new ControlPort(this, "end", false);
	}

	@Override
	public boolean check() {
		if (flowInconsistency()) {
			return false;
		}
		if (inputs.size() < 1) {
			return false;
		}
		if (!inputsConnected()) {
			return false;
		}

		return true;
	}

	@Override
	public String generateCode() {
		String text = "";

		for (DataPort input : inputs) {
			String valueName = ((DataPort) input.connections.get(0)).variable.name;
			if (input.variable.name.startsWith("gl_")) {
				text += input.variable.name + " = " + valueName + ";";
			} else {
				text += input.generateCode() + " = " + valueName + ";";
			}
		}

		return text;
	}

	@Override
	public boolean remove() {
		return false;
	}

	@Override
	protected boolean flowInconsistency() {
		return flowEnd.connections.size() != 1;
	}

	@Override
	public void draw(Graphics2D g, Node selectedNode, Port selectedPort) {
		super.draw(g, selectedNode, selectedPort);
		flowEnd.draw(g, selectedPort);
	}

	@Override
	public void move(int x, int y) {
		super.move(x, y);
		flowEnd.move(x, y);
	}

	@Override
	public Port hitPort(Point p) {
		if (flowEnd.hit(p)) {
			return flowEnd;
		}

		return super.hitPort(p);
	}

}