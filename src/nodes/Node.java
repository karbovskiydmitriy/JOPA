package nodes;

import static app.Main.currentProject;
import static app.Main.settings;
import static java.awt.Color.BLACK;
import static java.awt.Color.RED;
import static java.awt.Color.WHITE;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;

import app.Constant;
import app.Function;
import app.Template;
import app.Variable;
import ports.ControlPort;
import ports.DataPort;
import ports.Port;
import types.GLSLType;

public abstract class Node implements Serializable {

	private static final long serialVersionUID = 3421947909279717819L;

	private static final int HEADER_HEIGHT = 20;
	private static final int DEFAULT_SIZE = 100;

	public Function function;
	public Rectangle rect;
	public String header;
	public String text;
	public transient Template template;
	public ArrayList<DataPort> inputs;
	public ArrayList<DataPort> outputs;

	public Node(Function function, Rectangle rect, String header, String template) {
		this.function = function;
		this.rect = rect;
		this.header = header;
		init();
		assignTemplate(template);
	}

	public Node(Function function, Rectangle rect, String header) {
		this.function = function;
		this.rect = rect;
		this.header = header;
		init();
		assignTemplate("EMPTY");
	}

	public Node(Function function, int x, int y, String header, String template) {
		this.function = function;
		this.rect = new Rectangle(x, y, DEFAULT_SIZE, DEFAULT_SIZE);
		this.header = header;
		init();
		assignTemplate(template);
	}

	public Node(Function function, int x, int y, String header) {
		this.function = function;
		this.rect = new Rectangle(x, y, DEFAULT_SIZE, DEFAULT_SIZE);
		this.header = header;
		init();
		assignTemplate("EMPTY");
	}

	protected void init() {
		inputs = new ArrayList<DataPort>();
		outputs = new ArrayList<DataPort>();
		text = "SAMPLE TEXT";
	}

	public abstract boolean check();

	public abstract boolean remove();

	public abstract String generateCode();

	protected boolean flowInconsistency() {
		return false;
	}

	public int getID() {
		if (this == function.startNode) {
			return 0;
		}
		if (this == function.endNode) {
			return function.statementNodes.size() + 2;
		}

		return function.statementNodes.indexOf(this) + 1;
	}

	// protected String generateConnectionsCode() {
	// String text = "";
	// for (DataPort input : inputs) {
	// String valueName = ((DataPort) input.connections.get(0)).variable.name;
	// if (input.variable.name.startsWith("gl_")) {
	// text += input.variable.name + " = " + valueName + ";";
	// } else {
	// text += input.generateCode() + " = " + valueName + ";";
	// }
	// }
	//
	// return text;
	// }

	private void assignTemplate(String formulaName) {
		if (formulaName == null) {
			return;
		}

		Template template = Template.getFormulaByName(formulaName);
		this.template = template;
		if (template != null) {
			for (int i = 0; i < template.inputs.length; i++) {
				String input = template.inputs[i];
				Variable variable = Variable.create(input);
				if (variable == null) {
					variable = new Variable(GLSLType.NONE, input);
				} else if (variable.getClass() == Constant.class) {
					currentProject.constants.add((Constant) variable);
				}
				createPort(variable, false, false);
			}
			for (int i = 0; i < template.outputs.length; i++) {
				String output = template.outputs[i];
				Variable variable = Variable.create(output);
				if (variable == null) {
					variable = new Variable(GLSLType.NONE, output);
				} else if (variable.getClass() == Constant.class) {
					currentProject.constants.add((Constant) variable);
				}
				createPort(variable, true, false);
			}
			adjustPorts();
		}
	}

	public void createPort(Variable variable, boolean isOutput, boolean update) {
		DataPort port = new DataPort(this, variable, isOutput);
		// if (variable.getClass() == Constant.class) {
		// Main.currentProject.constants.add((Constant) variable);
		// }
		if (!isOutput) {
			inputs.add(port);
		} else {
			outputs.add(port);
		}
		if (update) {
			adjustPorts();
		}
	}

	public void deletePort(DataPort port) {
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
			DataPort port = inputs.get(i);
			float h = rect.y + HEADER_HEIGHT + (i + 1) * inputsStep;
			port.position = new Point(rect.x, (int) h);
		}
		for (int i = 0; i < outputs.size(); i++) {
			DataPort port = outputs.get(i);
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
			g.setColor(settings.defaultPalette.selectedNodeColor);
			g.fillRect(rect.x, rect.y, rect.width, HEADER_HEIGHT);
		}
		boolean isCorrect = check();
		if (settings.highlightNodes && !isCorrect) {
			g.setColor(RED);
		} else {
			g.setColor(BLACK);
		}
		g.drawRect(rect.x, rect.y, rect.width, HEADER_HEIGHT);
		g.drawRect(rect.x, rect.y, rect.width, rect.height);
		g.drawString(header, rect.x + ControlPort.WIDTH, rect.y + HEADER_HEIGHT);
		if (text != null) {
			g.drawString(text, rect.x, rect.y + HEADER_HEIGHT * 2);
		}
	}

	public void draw(Graphics2D g, Node selectedNode, Port selectedPort) {
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

	public Port hitPort(Point p) {
		for (DataPort port : inputs) {
			if (port.hit(p)) {
				return port;
			}
		}

		for (DataPort port : outputs) {
			if (port.hit(p)) {
				return port;
			}
		}

		return null;
	}

	public boolean inputsConnected() {
		if (inputs != null) {
			for (DataPort port : inputs) {
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