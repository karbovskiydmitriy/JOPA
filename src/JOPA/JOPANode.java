package JOPA;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class JOPANode {

	final int HEADER_HEIGHT = 20;

	protected Rectangle rect;
	protected String header;
	protected String command;
	protected JOPAFormula formula;
	protected ArrayList<JOPAPort> inputs;
	protected ArrayList<JOPAPort> outputs;

	public JOPANode(Rectangle rect, String header, String command, String formula) {
		this.rect = rect;
		this.header = header;
		this.command = command;
		try {
			assignFormula(new JOPAFormula(formula));
		} catch (JOPAException e) {
			System.err.println(e.getMessage());
			assignFormula(null);
		}
	}

	private void assignFormula(JOPAFormula formula) {
		this.formula = formula;
		if (formula != null) {
			inputs = new ArrayList<JOPAPort>(formula.inputs.length);
			outputs = new ArrayList<JOPAPort>(formula.outputs.length);
			int inputsCount = formula.inputs.length;
			int outputsCount = formula.outputs.length;
			float inputsStep = (rect.height - HEADER_HEIGHT) / (float) (inputsCount + 1);
			float outputsStep = (rect.height - HEADER_HEIGHT) / (float) (outputsCount + 1);
			for (int i = 0; i < formula.inputs.length; i++) {
				float h = rect.y + HEADER_HEIGHT + (i + 1) * inputsStep;
				JOPAPort port = new JOPAPort(this, new Point(rect.x, (int) h), formula.inputs[i], false);
				inputs.add(port);
			}
			for (int i = 0; i < formula.outputs.length; i++) {
				float h = rect.y + HEADER_HEIGHT + (i + 1) * outputsStep;
				JOPAPort port = new JOPAPort(this, new Point(rect.x + rect.width, (int) h), formula.outputs[i], true);
				outputs.add(port);
			}
		}
	}

	public void move(int x, int y) {
		rect.x += x;
		rect.y += y;
		inputs.forEach(node -> node.move(x, y));
		outputs.forEach(node -> node.move(x, y));
	}

	public void draw(Graphics2D g, JOPANode selectedNode, JOPAPort selectedPort) {
		g.setColor(Color.WHITE);
		g.fillRect(rect.x, rect.y, rect.width, rect.height);
		if (selectedNode == this) {
			g.setColor(Color.CYAN);
			g.fillRect(rect.x, rect.y, rect.width, HEADER_HEIGHT);
		}
		g.setColor(Color.BLACK);
		g.drawRect(rect.x, rect.y, rect.width, HEADER_HEIGHT);
		g.drawRect(rect.x, rect.y, rect.width, rect.height);
		g.drawString(header, rect.x, rect.y + HEADER_HEIGHT);
		g.drawString(command, rect.x, rect.y + HEADER_HEIGHT * 2);
		inputs.forEach(port -> port.draw(g, selectedPort));
		outputs.forEach(port -> port.draw(g, selectedPort));
	}

	public boolean hit(Point p) {
		return rect.contains(p);
	}

	public JOPAPort hitPort(Point p) {
		for (JOPAPort port : inputs) {
			if (port.hit(p)) {
				return port;
			}
		}

		for (JOPAPort port : outputs) {
			if (port.hit(p)) {
				return port;
			}
		}

		return null;
	}

}