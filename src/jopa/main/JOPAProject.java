package jopa.main;

import static jopa.main.JOPAFunction.NEW_LINE;
import static jopa.main.JOPAFunction.TWO_LINES;
import static jopa.main.JOPAMain.currentProject;
import static jopa.main.JOPAMain.gui;
import static jopa.main.JOPAMain.settings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import jopa.io.JOPASerializer;
import jopa.nodes.JOPABranchNode;
import jopa.nodes.JOPAConstantsNode;
import jopa.nodes.JOPAEndNode;
import jopa.nodes.JOPAFunctionNode;
import jopa.nodes.JOPALoopNode;
import jopa.nodes.JOPANode;
import jopa.nodes.JOPAStartNode;
import jopa.nodes.JOPAStatementNode;
import jopa.nodes.JOPATypesNode;
import jopa.playground.JOPAPlayground;
import jopa.playground.JOPASimulationScript;
import jopa.ports.JOPADataPort;
import jopa.ports.JOPAPort;
import jopa.types.JOPACustomType;

public class JOPAProject implements Serializable {

	private static final long serialVersionUID = -5034273809770939736L;

	private transient long lastSelectTick;
	private transient Point prevPoint;
	private transient JOPAPort selectedPort;
	private transient JOPANode selectedNode;
	private transient JOPANode draggingNode;
	private transient Point cursorPosition;
	private transient String generatedShader;
	private transient JOPAPlayground playground;
	private ArrayList<JOPAFunction> functions;
	private JOPAFunction mainFunction;

	public transient JOPASimulationScript script;
	public String name;
	public JOPAProjectType projectType;
	public ArrayList<JOPACustomType> types;
	public ArrayList<JOPAConstant> constants;
	public ArrayList<JOPAVariable> globalVariables;
	public JOPAFunction currentFunction;

	public JOPAProject(String name, JOPAProjectType type) {
		this.name = name;
		this.projectType = type;
		init();
	}

	private void init() {
		currentProject = this;
		types = new ArrayList<JOPACustomType>();
		constants = new ArrayList<JOPAConstant>();
		globalVariables = new ArrayList<JOPAVariable>();
		functions = new ArrayList<JOPAFunction>();
	}

	public synchronized JOPAFunction createFunction(String name) {
		JOPAFunction function;
		if (functions.size() == 0) {
			function = new JOPAFunction("main");
			mainFunction = function;
		} else {
			if (name == null) {
				name = "function_" + functions.size();
			}
			function = new JOPAFunction(name);
		}
		functions.add(function);

		return function;
	}

	public void updateConstants() {
		functions.forEach(function -> {
			function.constantsNode.updateConstants();
		});
	}

	public void updateGlobals() {
		functions.forEach(function -> {
			function.globalsNode.updateGlobals();
		});
	}

	public synchronized boolean deleteFunction(String name) {
		return functions.removeIf(function -> function.name.equals(name));
	}

	public synchronized void selectFunction(int index) {
		if (functions.size() > 0) {
			currentFunction = functions.get(index);
		}
	}

	public synchronized void createPlayground(JOPAProjectType type) {
		if (playground != null) {
			playground.stop();
			playground.close();
		}
		playground = JOPAPlayground.create(type);
	}

	public synchronized void startPlayground() {
		if (playground != null) {
			playground.stop();
			playground.start();
		}
	}

	public synchronized void stopPlayground() {
		if (playground != null) {
			playground.stop();
		}
	}

	public synchronized void closePlayground() {
		if (playground != null) {
			playground.close();
			playground = null;
		}
	}

	public synchronized void draw(Graphics2D g) {
		if (currentFunction != null) {
			currentFunction.draw(g, selectedNode, selectedPort);
		}
		if (selectedPort != null) {
			JOPADataPort.drawConnection(g, selectedPort.position.x, selectedPort.position.y, cursorPosition.x,
					cursorPosition.y, Color.BLACK);
		}
	}

	public synchronized void mousePressed(Point p) {
		JOPAPort port = getPortOnPoint(p);
		if (port != null) {
			selectedNode = null;
			if (selectedPort == null) {
				selectedPort = port;
				if (!selectedPort.isOutput) {
					selectedPort.destroyAllConnections();
				}
			}
		} else {
			JOPANode node = getNodeOnPoint(p);
			if (node != null) {
				selectedNode = node;
				draggingNode = node;
				prevPoint = p;
			}
		}
	}

	public synchronized void mouseReleased(Point p) {
		JOPAPort port = getPortOnPoint(p);
		if (port != null) {
			if (port.makeConnection(selectedPort)) {
				selectedPort = null;
			} else {
				if (selectedPort != port) {
					selectedPort = null;
				}
			}
		} else {
			selectedPort = null;
		}
		draggingNode = null;
	}

	public synchronized void mouseClicked(Point p) {
		JOPAPort port = getPortOnPoint(p);
		if (port != null) {
			if (!port.isOutput) {
				if (port.connections.size() > 0) {
					port.destroyAllConnections();
				}
			}
			selectedNode = null;
			if (selectedPort == null) {
				selectedPort = port;
			} else {
				if (port.makeConnection(selectedPort)) {
					selectedPort = null;
				} else {
					selectedPort = null;
				}
			}
		} else {
			JOPANode node = getNodeOnPoint(p);
			if (node != null) {
				selectedPort = null;
				if (selectedNode == node) {
					long currentTime = System.currentTimeMillis();
					if (currentTime - lastSelectTick <= 500) {
						openNodeEditor(node);
					}
					lastSelectTick = currentTime;
				}
				selectedNode = node;
			}
		}
	}

	public synchronized void mouseMoved(Point p) {
		cursorPosition = p;
		if (draggingNode != null) {
			selectedPort = null;
			draggingNode.move(p.x - prevPoint.x, p.y - prevPoint.y);
			prevPoint = p;
		}
	}

	public synchronized void keyTyped(int keyCode) {
		switch (keyCode) {
		case 8:
			if (selectedNode != null) {
				currentFunction.removeNode(selectedNode);
				gui.repaint();
			}
			break;
		case 10:
			if (selectedNode != null) {
				openNodeEditor(selectedNode);
			}
			break;
		case 'E':
			settings.highlightNodes = !settings.highlightNodes;
			gui.repaint();
			break;
		case 'S':
			showGeneratedShader();
			break;
		case 'G':
			generateShader();
			break;
		case 'R':
			gui.repaint();
			break;
		case 'T':
			settings.showPortTypes = !settings.showPortTypes;
			gui.repaint();
			break;
		default:
			break;
		}
	}

	private void openNodeEditor(JOPANode node) {
		Class<?> nodeType = node.getClass();
		if (nodeType.equals(JOPAStatementNode.class)) {
			gui.openStatementNodeEditor((JOPAStatementNode) node);
		} else if (nodeType.equals(JOPABranchNode.class)) {
			gui.openBranchNodeEditor((JOPABranchNode) node);
		} else if (nodeType.equals(JOPALoopNode.class)) {
			gui.openLoopNodeEditor((JOPALoopNode) node);
		} else if (nodeType.equals(JOPAFunctionNode.class)) {
			gui.openFunctionNodeEditor((JOPAFunctionNode) node);
		} else if (nodeType.equals(JOPATypesNode.class)) {
			gui.openTypesListEditor(currentProject);
		} else if (nodeType.equals(JOPAConstantsNode.class)) {
			gui.openConstantsEditor(this);
		} else if (nodeType.equals(JOPAStartNode.class)) {
			gui.openFunctionEditor(currentFunction);
		} else if (nodeType.equals(JOPAEndNode.class)) {
			gui.openFunctionEditor(currentFunction);
		}
	}

	private JOPANode getNodeOnPoint(Point p) {
		if (currentFunction != null) {
			return currentFunction.getNodeOnPoint(p);
		}

		return null;
	}

	private JOPAPort getPortOnPoint(Point p) {
		if (currentFunction != null) {
			return currentFunction.getPortOnPoint(p);
		}

		return null;
	}

	public synchronized boolean verifyFunction(JOPAFunction function) {
		if (!function.verifyNodes()) {
			return false;
		}

		return true;
	}

	public synchronized boolean verifyFunctions() {
		for (JOPAFunction function : functions) {
			if (!verifyFunction(function)) {
				return false;
			}
		}

		return true;
	}

	public synchronized void generateShader() {
		if (verifyFunctions()) {
			String shaderCode = "#version ";
			switch (projectType) {
			case CUSTOM:
				return;
			case FRAGMENT:
				shaderCode += "130";
				break;
			case COMPUTE:
				shaderCode += "430";
				break;
			}
			shaderCode += TWO_LINES;
			// TODO defines
			if (projectType == JOPAProjectType.COMPUTE) {
				shaderCode += ""; // TODO local groups size
				shaderCode += TWO_LINES;
			}
			shaderCode += mainFunction.typesNode.generateCode();
			if (mainFunction.constantsNode.outputs.size() > 0) {
				shaderCode += mainFunction.constantsNode.generateCode();
				shaderCode += NEW_LINE;
			}
			// TODO buffers, images
			if (mainFunction.startNode.outputs.size() > 0) {
				for (JOPADataPort port : mainFunction.startNode.outputs) {
					if (port.variable.name.startsWith("gl_")) {
						continue;
					}
					String modifier = "uniform ";
					shaderCode += modifier + port.generateCode() + ";";
					shaderCode += NEW_LINE;
				}
				shaderCode += NEW_LINE;
			}
			if (globalVariables.size() > 0) {
				shaderCode += mainFunction.globalsNode.generateCode();
				shaderCode += TWO_LINES;
			}
			if (functions.size() > 1) {
				for (JOPAFunction function : functions) {
					if (function != mainFunction) {
						shaderCode += function.getPrototype() + ";";
					}
				}
				shaderCode += TWO_LINES;
			}
			for (JOPAFunction function : functions) {
				shaderCode += function.generateCode();
			}
			this.generatedShader = shaderCode;
		} else {
			gui.showMessage("project contains error!");
			generatedShader = null;
		}
	}

	public synchronized String getGeneratedShader() {
		// DECIDE update?
		if (generatedShader == null) {
			generateShader();
		}

		return generatedShader;
	}

	public synchronized void showGeneratedShader() {
		if (generatedShader != null) {
			gui.showShader(generatedShader);
		} else {
			gui.showMessage("shader not generated!");
		}
	}

	public static synchronized JOPAProject loadFromFile(File file) {
		return JOPASerializer.readFromfile(file);
	}

	public static synchronized boolean saveToFile(File file, JOPAProject project) {
		return JOPASerializer.saveToFile(file, project);
	}

}