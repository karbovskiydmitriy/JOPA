package jopa.nodes;

import static java.awt.Color.BLACK;
import static java.awt.Color.RED;
import static java.awt.Color.WHITE;
import static jopa.util.JOPATypeUtil.getNameForType;
import static jopa.util.JOPATypeUtil.getTypeForName;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import jopa.main.JOPAMain;
import jopa.main.JOPANodeTemplate;
import jopa.ports.JOPAControlPort;
import jopa.ports.JOPADataPort;
import jopa.ports.JOPAPort;
import jopa.types.JOPAGLSLType;
import jopa.types.JOPANodeType;

public abstract class JOPANode {

	private static final int HEADER_HEIGHT = 20;
	private static final int DEFAULT_SIZE = 100;

	public Rectangle rect;
	public String header;
	public String command;
	public transient JOPANodeTemplate template;
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
		assignTemplate("EMPTY");
		init();
	}

	public JOPANode(int x, int y, String header, String template) {
		this.rect = new Rectangle(x, y, DEFAULT_SIZE, DEFAULT_SIZE);
		this.header = header;
		assignTemplate(template);
		init();
	}

	public JOPANode(int x, int y, String header) {
		this.rect = new Rectangle(x, y, DEFAULT_SIZE, DEFAULT_SIZE);
		this.header = header;
		assignTemplate("EMPTY");
		init();
	}

	protected abstract void init();

	public abstract boolean check();

	public abstract String generateCode();

	public abstract boolean remove();

	protected boolean flowInconsistency() {
		return false;
	}

	protected String generateConnectionsCode() {
		String text = "";
		for (JOPADataPort input : inputs) {
			String varType = getNameForType(input.dataType);
			String varName = input.name;
			String valueName = input.connections.get(0).name;
			text += varType + " " + varName + " = " + valueName + ";";
		}

		return text;
	}

	private void assignTemplate(String formulaName) {
		JOPANodeTemplate template = JOPANodeTemplate.getFormulaByName(formulaName);
		this.template = template;
		if (template != null) {
			inputs = new ArrayList<JOPADataPort>(template.inputs.length);
			outputs = new ArrayList<JOPADataPort>(template.outputs.length);
			for (int i = 0; i < template.inputs.length; i++) {
				String input = template.inputs[i];
				JOPAGLSLType type = JOPAGLSLType.JOPA_NONE;
				String name = input;
				if (input.contains(":")) {
					String[] parts = input.split(":");
					if (parts.length == 2) {
						type = getTypeForName(parts[0]);
						name = parts[1];
					}
				}
				createPort(type, name, false, false);
			}
			for (int i = 0; i < template.outputs.length; i++) {
				String output = template.outputs[i];
				JOPAGLSLType type = JOPAGLSLType.JOPA_NONE;
				String name = output;
				if (output.contains(":")) {
					String[] parts = output.split(":");
					if (parts.length == 2) {
						type = getTypeForName(parts[0]);
						name = parts[1];
					}
				}
				createPort(type, name, true, false);
			}
			adjustPorts();
		}
	}

	public void createPort(JOPAGLSLType type, String name, boolean isOutput, boolean update) {
		JOPADataPort port = new JOPADataPort(this, type, name, isOutput);
		if (!isOutput) {
			inputs.add(port);
		} else {
			outputs.add(port);
		}
		if (update) {
			adjustPorts();
		}
	}

	public void deletePort(JOPADataPort port) {
		if (!port.isOutput) {
			inputs.remove(port);
		} else {
			outputs.remove(port);
		}
		port.destroyAllConnections();
		adjustPorts();
	}

	protected void adjustPorts() {
		float inputsStep = (rect.height - HEADER_HEIGHT) / (float) (inputs.size() + 1);
		float outputsStep = (rect.height - HEADER_HEIGHT) / (float) (outputs.size() + 1);

		for (int i = 0; i < inputs.size(); i++) {
			JOPADataPort port = inputs.get(i);
			float h = rect.y + HEADER_HEIGHT + (i + 1) * inputsStep;
			port.position = new Point(rect.x, (int) h);
		}
		for (int i = 0; i < outputs.size(); i++) {
			JOPADataPort port = outputs.get(i);
			float h = rect.y + HEADER_HEIGHT + (i + 1) * outputsStep;
			port.position = new Point(rect.x + rect.width, (int) h);
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
			g.setColor(JOPAMain.settings.defaultPalette.selectedNodeColor);
			g.fillRect(rect.x, rect.y, rect.width, HEADER_HEIGHT);
		}
		boolean isCorrect = check();
		if (JOPAMain.settings.highlightIncorrectNodes && !isCorrect) {
			g.setColor(RED);
		} else {
			g.setColor(BLACK);
		}
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