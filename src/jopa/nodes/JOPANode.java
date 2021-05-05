package jopa.nodes;

import static java.awt.Color.BLACK;
import static java.awt.Color.CYAN;
import static java.awt.Color.WHITE;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import jopa.main.JOPATemplate;
import jopa.ports.JOPAControlPort;
import jopa.ports.JOPADataPort;
import jopa.ports.JOPAPort;
import jopa.types.JOPANodeType;

public abstract class JOPANode {

	private static final int HEADER_HEIGHT = 20;
	private static final Color SELECTED_COLOR = CYAN;

	public Rectangle rect;
	public String header;
	public String command;
	public transient JOPATemplate template;

	public JOPANodeType nodeType;
	public ArrayList<JOPADataPort> inputs;
	public ArrayList<JOPADataPort> outputs;

	public JOPANode(Rectangle rect, String header, String template) {
		this.rect = rect;
		this.header = header;
		assignTemplate(template);
		init();
	}

	public JOPANode(Rectangle rect, String header) {
		this.rect = rect;
		this.header = header;
		assignTemplate("TEST_EMPTY");
		init();
	}

	public JOPANode(int x, int y, String header, String template) {
		this.rect = new Rectangle(x, y, 100, 100);
		this.header = header;
		assignTemplate(template);
		init();
	}

	public JOPANode(int x, int y, String header) {
		this.rect = new Rectangle(x, y, 100, 100);
		this.header = header;
		assignTemplate("TEST_EMPTY");
		init();
	}

	protected abstract void init();
	public abstract boolean remove();

	private void assignTemplate(String formulaName) {
		JOPATemplate template = JOPATemplate.getFormulaByName(formulaName);
		this.template = template;
		if (template != null) {
			inputs = new ArrayList<JOPADataPort>(template.inputs.length);
			outputs = new ArrayList<JOPADataPort>(template.outputs.length);
			int inputsCount = template.inputs.length;
			int outputsCount = template.outputs.length;
			float inputsStep = (rect.height - HEADER_HEIGHT) / (float) (inputsCount + 1);
			float outputsStep = (rect.height - HEADER_HEIGHT) / (float) (outputsCount + 1);
			for (int i = 0; i < template.inputs.length; i++) {
				float h = rect.y + HEADER_HEIGHT + (i + 1) * inputsStep;
				JOPADataPort port = new JOPADataPort(this, new Point(rect.x, (int) h), template.inputs[i], false);
				inputs.add(port);
			}
			for (int i = 0; i < template.outputs.length; i++) {
				float h = rect.y + HEADER_HEIGHT + (i + 1) * outputsStep;
				JOPADataPort port = new JOPADataPort(this, new Point(rect.x + rect.width, (int) h), template.outputs[i],
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
		g.drawString(header, rect.x + JOPAControlPort.WIDTH, rect.y + HEADER_HEIGHT);
		if (command != null) {
			g.drawString(command, rect.x, rect.y + HEADER_HEIGHT * 2);
		}
	}

	public void draw(Graphics2D g, JOPANode selectedNode, JOPAPort selectedPort) {
		drawFrame(g, selectedNode == this);
		if (inputs != null) {
			inputs.forEach(port -> port.draw(g, selectedPort));
		}
		if (outputs != null) {
			outputs.forEach(port -> port.draw(g, selectedPort));
		}
	}

	public boolean hit(Point p) {
		return rect.contains(p);
	}

	public JOPAPort hitPort(Point p) {
		for (JOPADataPort port : inputs) {
			if (port.hit(p)) {
				return port;
			}
		}

		for (JOPADataPort port : outputs) {
			if (port.hit(p)) {
				return port;
			}
		}

		return null;
	}

	public boolean inputsConnected() {
		if (inputs != null) {
			for (JOPADataPort port : inputs) {
				if (port.connections.size() == 0) {
					System.out.println("node " + header + " not OK");

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