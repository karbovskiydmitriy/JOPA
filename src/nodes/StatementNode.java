package nodes;

import static app.Function.NEW_LINE;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import app.Function;
import ports.ControlPort;
import ports.DataPort;
import ports.Port;

public class StatementNode extends Node {

	private static final long serialVersionUID = 1015604031569137995L;

	public ControlPort incomingControlFlow;
	public ControlPort outcomingControlFlow;

	public StatementNode(Function function, Rectangle rect, String header, String template) {
		super(function, rect, header, template);
	}

	public StatementNode(Function function, int x, int y, String header, String template) {
		super(function, x, y, header, template);
	}

	public StatementNode(Function function, Rectangle rect, String template) {
		super(function, rect, "STATEMENT", template);
	}

	public StatementNode(Function function, int x, int y, String template) {
		super(function, x, y, "STATEMENT", template);
	}

	@Override
	protected void init() {
		super.init();
		incomingControlFlow = new ControlPort(this, "in", false);
		outcomingControlFlow = new ControlPort(this, "out", true);
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
		String text = "";
		for (DataPort input : inputs) {
			String valueName = input.connections.get(0).getName();
			text += input.generateCode() + " = " + valueName + ";" + NEW_LINE;
		}
		String templateCode = template.template;
//		for (DataPort input : inputs) {
//			DataPort connectedPort = (DataPort) input.connections.get(0);
//			String valueName = connectedPort.getName();
//			templateCode.replaceAll(valueName, connectedPort.variable.name);
//		}
		String chainCode = outcomingControlFlow.generateCode();

		return text + templateCode + chainCode;
	}

	@Override
	public boolean remove() {
		inputs.forEach(port -> port.destroyAllConnections());
		outputs.forEach(port -> port.destroyAllConnections());
		incomingControlFlow.destroyAllConnections();
		outcomingControlFlow.destroyAllConnections();

		return true;
	}

	@Override
	protected boolean flowInconsistency() {
		if (incomingControlFlow.connections.size() == 0) {
			return true;
		}
		if (outcomingControlFlow.connections.size() != 1) {
			return true;
		}

		return false;
	}

	@Override
	public void draw(Graphics2D g, Node selectedNode, Port selectedPort) {
		super.draw(g, selectedNode, selectedPort);
		incomingControlFlow.draw(g, selectedPort);
		outcomingControlFlow.draw(g, selectedPort);
	}

	@Override
	public void move(int x, int y) {
		super.move(x, y);
		incomingControlFlow.move(x, y);
		outcomingControlFlow.move(x, y);
	}

	@Override
	public Port hitPort(Point p) {
		if (incomingControlFlow.hit(p)) {
			return incomingControlFlow;
		}
		if (outcomingControlFlow.hit(p)) {
			return outcomingControlFlow;
		}

		return super.hitPort(p);
	}

}