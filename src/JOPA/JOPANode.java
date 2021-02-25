package JOPA;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class JOPANode {

	final int HEADER_HEIGHT = 20;
	final int PORT_RADIUS = 6;

	protected Rectangle rect;
	protected String header;
	protected String command;
	protected JOPAFormula formula;
	protected JOPAPort[] inputs;
	protected JOPAPort[] outputs;

	public JOPANode(Rectangle rect, String header, String command, String formula) {
		this.rect = rect;
		this.header = header;
		this.command = command;
		try {
			assignFormula(new JOPAFormula(formula));
		} catch (Exception e) {
			System.err.println(e.getMessage());
			assignFormula(null);
		}
	}

	private void assignFormula(JOPAFormula formula) {
		this.formula = formula;
		if (formula != null) {
			inputs = new JOPAPort[formula.inputs.length];
			outputs = new JOPAPort[formula.outputs.length];
			int inputsCount = formula.inputs.length;
			int outputsCount = formula.outputs.length;
			float inputsStep = (rect.height - HEADER_HEIGHT) / (float) (inputsCount + 1);
			float outputsStep = (rect.height - HEADER_HEIGHT) / (float) (outputsCount + 1);
			for (int i = 0; i < inputs.length; i++) {
				float h = HEADER_HEIGHT + (i + 1) * inputsStep;
				inputs[i] = new JOPAPort(this, new Point(0, (int) h), PORT_RADIUS, formula.inputs[i], false);
			}
			for (int i = 0; i < outputs.length; i++) {
				float h = HEADER_HEIGHT + (i + 1) * outputsStep;
				outputs[i] = new JOPAPort(this, new Point(rect.width, (int) h), PORT_RADIUS, formula.outputs[i], true);
			}
		}
	}

	public void draw(Graphics2D g, boolean isSelected) {
		g.setColor(Color.WHITE);
		g.fillRect(rect.x, rect.y, rect.width, rect.height);
		if (isSelected) {
			g.setColor(Color.CYAN);
			g.fillRect(rect.x, rect.y, rect.width, HEADER_HEIGHT);
		}
		g.setColor(Color.BLACK);
		g.drawRect(rect.x, rect.y, rect.width, HEADER_HEIGHT);
		g.drawRect(rect.x, rect.y, rect.width, rect.height);
		g.drawString(header, rect.x, rect.y + HEADER_HEIGHT);
		g.drawString(command, rect.x, rect.y + HEADER_HEIGHT * 2);
		for (JOPAPort port : inputs) {
			port.draw(g);
		}
		for (JOPAPort port : outputs) {
			port.draw(g);
		}
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