package jopa.main;

import static jopa.main.JOPAFunction.NEW_LINE;
import static jopa.main.JOPAFunction.TWO_LINES;
import static jopa.main.JOPAMain.currentProject;
import static jopa.main.JOPAMain.gui;
import static jopa.main.JOPAMain.settings;
import static jopa.io.JOPALoader.loadStandardScript;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import jopa.io.JOPASerializer;
import jopa.nodes.JOPABranchNode;
import jopa.nodes.JOPAConstantsNode;
import jopa.nodes.JOPADefinesNode;
import jopa.nodes.JOPAEndNode;
import jopa.nodes.JOPAFunctionNode;
import jopa.nodes.JOPAGlobalsNode;
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
import jopa.types.JOPAProjectType;
import jopa.types.JOPAResource;
import jopa.types.JOPAResourceType;

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
	private boolean projectChanged;
	private ArrayList<JOPAFunction> functions;

	public transient JOPASimulationScript script;
	public boolean isCustom = false;
	public String name;
	public JOPAProjectType projectType;
	public int[] localGroupSize;
	public ArrayList<JOPANodeTemplate> templates;
	public ArrayList<JOPASymbol> defines;
	public ArrayList<JOPACustomType> types;
	public ArrayList<JOPAConstant> constants;
	public ArrayList<JOPAResource> resources;
	public ArrayList<JOPAVariable> globalVariables;
	public JOPAFunction currentFunction;
	public JOPAFunction mainFunction;

	public JOPAProject(String name, JOPAProjectType type) {
		this.name = name;
		this.projectType = type;
		init();
	}

	private void init() {
		currentProject = this;
		localGroupSize = new int[] { 1, 1, 1 };
		templates = new ArrayList<JOPANodeTemplate>();
		defines = new ArrayList<JOPASymbol>();
		types = new ArrayList<JOPACustomType>();
		constants = new ArrayList<JOPAConstant>();
		resources = new ArrayList<JOPAResource>();
		globalVariables = new ArrayList<JOPAVariable>();
		functions = new ArrayList<JOPAFunction>();
		JOPANodeTemplate.initStandardNodeTemplates(this);
		System.out.println("[PROJECT] Loaded " + templates.size() + " templates");
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

	public synchronized void selectFunction(String name) {
		functions.forEach(function -> {
			if (function.name.equals(name)) {
				currentFunction = function;

				return;
			}
		});
	}

	public synchronized void createPlayground(JOPAProjectType type) {
		if (playground != null) {
			playground.stop();
			playground.close();
		}
		playground = new JOPAPlayground();
		script = JOPASimulationScript.create(type);
	}

	public synchronized void startPlayground() {
		if (playground != null) {
			if (isCustom) {
				String scriptCode = loadStandardScript("ants.jopascript");
				if (scriptCode != null) {
					script.setupScript(scriptCode);
				} else {
					gui.showMessage("Shader generation failed");

					return;
				}
			}
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

	public synchronized void keyPressed(int keyCode) {
		switch (keyCode) {
		case 8:
		case 127:
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
		} else if (nodeType.equals(JOPADefinesNode.class)) {
			gui.openDefinesEditor(currentProject);
		} else if (nodeType.equals(JOPATypesNode.class)) {
			gui.openTypesListEditor(currentProject);
		} else if (nodeType.equals(JOPAConstantsNode.class)) {
			gui.openConstantsEditor(this);
		} else if (nodeType.equals(JOPAGlobalsNode.class)) {
			gui.openGlobalsEditor(currentFunction);
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

	public synchronized String[] getFunctions() {
		String[] functionsArray = new String[functions.size()];

		for (int i = 0; i < functions.size(); i++) {
			JOPAFunction function = functions.get(i);
			functionsArray[i] = function.name;
		}

		return functionsArray;
	}

	public synchronized JOPAFunction getFunctionByName(String functionName) {
		for (JOPAFunction function : functions) {
			if (function.name.equals(functionName)) {
				return function;
			}
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

	public synchronized boolean generateShader() {
		if (verifyFunctions()) {
			System.out.println("[PROJECT] Generating " + projectType + " shader");
			String shaderCode = "#version ";
			switch (projectType) {
			case FRAGMENT:
				shaderCode += "130";
				break;
			case COMPUTE:
				shaderCode += "430";
				break;
			}
			shaderCode += TWO_LINES;
			if (defines.size() > 0) {
				shaderCode += mainFunction.definesNode.generateCode();
				shaderCode += TWO_LINES;
			}
			if (projectType == JOPAProjectType.COMPUTE) {
				shaderCode += "layout(local_size_x = " + localGroupSize[0] + ", ";
				shaderCode += "local_size_y = " + localGroupSize[1] + ", ";
				shaderCode += "local_size_z = " + localGroupSize[2] + ") in;";
				shaderCode += TWO_LINES;
			}
			if (types.size() > 0) {
				shaderCode += mainFunction.typesNode.generateCode();
				shaderCode += TWO_LINES;
			}
			if (constants.size() > 0) {
				shaderCode += mainFunction.constantsNode.generateCode();
				shaderCode += NEW_LINE;
			}
			if (resources.size() > 0) {
				int index = 0;
				boolean hasImages = false;
				for (JOPAResource resource : resources) {
					if (resource.type == JOPAResourceType.IMAGE) {
						hasImages = true;
						// TODO format
						String format = "";
						shaderCode += "layout(" + format + ", binding = " + index++ + ") image2D ";
						shaderCode += resource.name + ";" + NEW_LINE;
					}
				}
				if (hasImages) {
					shaderCode += TWO_LINES;
				}
				boolean hasBuffers = false;
				for (JOPAResource resource : resources) {
					if (resource.type == JOPAResourceType.BUFFER_HANDLE) {
						hasBuffers = true;
						shaderCode += "layout(std430, binding = " + index++ + ") buffer name" + NEW_LINE;
						shaderCode += "{" + NEW_LINE;
						// TODO buffer type
						// shaderCode +=
						shaderCode += "};" + NEW_LINE;
					}
				}
				if (hasBuffers) {
					shaderCode += TWO_LINES;
				}
			}
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

		return true;
	}

	public synchronized String getGeneratedShader() {
		if (projectChanged || generatedShader == null) {
			generateShader();
			projectChanged = false;
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