package jopa.nodes;

import static java.awt.Color.BLACK;
import static java.awt.Color.CYAN;
import static java.awt.Color.WHITE;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;

import jopa.JOPAFormula;
import jopa.ui.JOPAUIPort;

public class JOPANode implements Serializable {

	private static final long serialVersionUID = -7460552036692405280L;

	final int HEADER_HEIGHT = 20;
	final Color SELECTED_COLOR = CYAN;

	protected Rectangle rect;
	protected String header;
	protected String command;
	protected transient JOPAFormula formula;

	public ArrayList<JOPAUIPort> inputs;
	public ArrayList<JOPAUIPort> outputs;

	public JOPANode(Rectangle rect, String header) {
		this.rect = rect;
		this.header = header;
		assignFormula("FOOBAR"); // FIXME!!!
	}

	private void assignFormula(String formulaName) {
		JOPAFormula formula = JOPAFormula.getFormulaByName(formulaName);
		this.formula = formula;
		if (formula != null) {
			inputs = new ArrayList<JOPAUIPort>(formula.inputs.length);
			outputs = new ArrayList<JOPAUIPort>(formula.outputs.length);
			int inputsCount = formula.inputs.length;
			int outputsCount = formula.outputs.length;
			float inputsStep = (rect.height - HEADER_HEIGHT) / (float) (inputsCount + 1);
			float outputsStep = (rect.height - HEADER_HEIGHT) / (float) (outputsCount + 1);
			for (int i = 0; i < formula.inputs.length; i++) {
				float h = rect.y + HEADER_HEIGHT + (i + 1) * inputsStep;
				JOPAUIPort port = new JOPAUIPort(this, new Point(rect.x, (int) h), formula.inputs[i], false);
				inputs.add(port);
			}
			for (int i = 0; i < formula.outputs.length; i++) {
				float h = rect.y + HEADER_HEIGHT + (i + 1) * outputsStep;
				JOPAUIPort port = new JOPAUIPort(this, new Point(rect.x + rect.width, (int) h), formula.outputs[i],
						true);
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

	protected void drawFrame(Graphics2D g, boolean isSelected) {
		g.setColor(WHITE);
		g.fillRect(rect.x, rect.y, rect.width, rect.height);
		if (isSelected) {
			g.setColor(SELECTED_COLOR);
			g.fillRect(rect.x, rect.y, rect.width, HEADER_HEIGHT);
		}
		g.setColor(BLACK);
		g.drawRect(rect.x, rect.y, rect.width, HEADER_HEIGHT);
		g.drawRect(rect.x, rect.y, rect.width, rect.height);
		g.drawString(header, rect.x, rect.y + HEADER_HEIGHT);
//		g.drawString(command, rect.x, rect.y + HEADER_HEIGHT * 2);
	}

	public void draw(Graphics2D g, JOPANode selectedNode, JOPAUIPort selectedPort) {
		drawFrame(g, selectedNode == this);
		inputs.forEach(port -> port.draw(g, selectedPort));
		outputs.forEach(port -> port.draw(g, selectedPort));
	}

	public boolean hit(Point p) {
		return rect.contains(p);
	}

	public JOPAUIPort hitPort(Point p) {
		for (JOPAUIPort port : inputs) {
			if (port.hit(p)) {
				return port;
			}
		}

		for (JOPAUIPort port : outputs) {
			if (port.hit(p)) {
				return port;
			}
		}

		return null;
	}

	public boolean inputsConnected() {
		if (inputs != null) {
			for (var port : inputs) {
				if (port.connections.size() == 0) {
					return false;
				}
				if (!port.connections.get(0).node.inputsConnected()) {
					return false;
				}
			}
		}

		return true;
	}

}