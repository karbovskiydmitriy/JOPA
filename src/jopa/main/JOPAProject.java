package jopa.main;

import static jopa.main.JOPAFunction.NEW_LINE;
import static jopa.main.JOPAFunction.TWO_LINES;

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
import jopa.playground.JOPASimulationType;
import jopa.ports.JOPADataPort;
import jopa.ports.JOPAPort;
import jopa.types.JOPAResource;
import jopa.types.JOPAType;

public class JOPAProject implements Serializable {

	private static final long serialVersionUID = -5034273809770939736L;

	private long lastSelectTick;
	private Point prevPoint;
	private JOPAPort selectedPort;
	private JOPANode selectedNode;
	private JOPANode draggingNode;
	private Point cursorPosition;
	private ArrayList<JOPAFunction> functions;
	private JOPAFunction mainFunction;
	private String generatedShader;
	private JOPAPlayground playground;

	public String name;
	public JOPAProjectType projectType;
	public ArrayList<JOPAType> types;
	public ArrayList<JOPAConstant> constants;
	public ArrayList<JOPAVariable> publicVariables;
	public ArrayList<JOPAResource> resources;
	public JOPAFunction currentFunction;
	public JOPASimulationScript script;

	public JOPAProject(String name, JOPAProjectType type) {
		this.name = name;
		this.projectType = type;
		init();
	}

	private void init() {
		types = new ArrayList<JOPAType>();
		constants = new ArrayList<JOPAConstant>();
		publicVariables = new ArrayList<JOPAVariable>();
		functions = new ArrayList<JOPAFunction>();
		resources = new ArrayList<JOPAResource>();
		script = JOPASimulationScript.create(JOPASimulationType.CUSTOM);
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

	public synchronized boolean deleteFunction(String name) {
		return functions.removeIf(function -> function.name.equals(name));
	}

	public synchronized void selectFunction(int index) {
		if (functions.size() > 0) {
			currentFunction = functions.get(index);
		}
	}

	public synchronized void createPlayground(JOPASimulationType type) {
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

	// public synchronized JOPAType createNewType(String name) {
	// JOPAType type = new JOPAType(name);
	// types.add(type);
	//
	// return type;
	// }
	//
	// public synchronized boolean deleteType(String name) {
	// return types.removeIf(type -> type.name.equals(name));
	// }

	public synchronized void draw(Graphics2D g) {
		// functions.forEach(function -> function.draw(g, selectedNode, selectedPort));
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
				JOPAMain.ui.repaint();
			}
			break;
		case 10:
			if (selectedNode != null) {
				openNodeEditor(selectedNode);
			}
			break;
		case 'E':
			JOPAMain.settings.highlightNodes = !JOPAMain.settings.highlightNodes;
			JOPAMain.ui.repaint();
			break;
		case 'S':
			showGeneratedShader();
			break;
		case 'G':
			generateShader();
			break;
		case 'R':
			JOPAMain.ui.repaint();
			break;
		case 'T':
			JOPAMain.settings.showPortTypes = !JOPAMain.settings.showPortTypes;
			JOPAMain.ui.repaint();
			break;
		default:
			break;
		}
	}

	private void openNodeEditor(JOPANode node) {
		Class<?> nodeType = node.getClass();
		if (nodeType.equals(JOPAStatementNode.class)) {
			JOPAMain.ui.openStatementNodeEditor((JOPAStatementNode) node);
		} else if (nodeType.equals(JOPABranchNode.class)) {
			JOPAMain.ui.openBranchNodeEditor((JOPABranchNode) node);
		} else if (nodeType.equals(JOPALoopNode.class)) {
			JOPAMain.ui.openLoopNodeEditor((JOPALoopNode) node);
		} else if (nodeType.equals(JOPAFunctionNode.class)) {
			JOPAMain.ui.openFunctionNodeEditor((JOPAFunctionNode) node);
		} else if (nodeType.equals(JOPATypesNode.class)) {
			JOPAMain.ui.openTypesListEditor(JOPAMain.currentProject);
		} else if (nodeType.equals(JOPAConstantsNode.class)) {
			JOPAMain.ui.openConstantsEditor(currentFunction);
		} else if (nodeType.equals(JOPAStartNode.class)) {
			JOPAMain.ui.openFunctionEditor(currentFunction);
		} else if (nodeType.equals(JOPAEndNode.class)) {
			JOPAMain.ui.openFunctionEditor(currentFunction);
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
			String shaderCode = "#version 130";
			shaderCode += TWO_LINES;
			if (types.size() > 0) {
				for (JOPAType type : types) {
					shaderCode += type.generateCode();
				}
				shaderCode += TWO_LINES;
			}
			if (mainFunction.constantsNode.outputs.size() > 0) {
				shaderCode += mainFunction.constantsNode.generateCode();
				shaderCode += NEW_LINE;
			}
			if (mainFunction.startNode.outputs.size() > 0) {
				for (JOPADataPort port : mainFunction.startNode.outputs) {
					if (port.variable.name.startsWith("gl_")) {
						continue;
					}
					if (port.connections.size() == 0) {
						continue;
					}
					String modifier = "uniform ";
					shaderCode += modifier + port.generateCode() + ";\n";
				}
				shaderCode += NEW_LINE;
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
			JOPAMain.ui.showMessage("project contains error!");
			generatedShader = null;
		}
	}

	public synchronized String getGeneratedShader() {
		if (generatedShader == null) {
			generateShader();
		}

		return generatedShader;
	}

	public synchronized void showGeneratedShader() {
		if (generatedShader != null) {
			JOPAMain.ui.showShader(generatedShader);
		} else {
			JOPAMain.ui.showMessage("shader not generated!");
		}
	}

	public static synchronized JOPAProject loadFromFile(File file) {
		return JOPASerializer.readFromfile(file);
	}

	public static synchronized boolean saveToFile(File file, JOPAProject project) {
		return JOPASerializer.saveToFile(file, project);
	}

}