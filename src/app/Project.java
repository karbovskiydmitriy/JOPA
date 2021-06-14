package app;

import static app.Function.NEW_LINE;
import static app.Function.TAB;
import static app.Function.TWO_LINES;
import static app.Main.currentProject;
import static app.Main.gui;
import static app.Main.settings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import io.Loader;
import io.Serializer;
import nodes.BranchNode;
import nodes.ConstantsNode;
import nodes.DefinesNode;
import nodes.EndNode;
import nodes.FunctionNode;
import nodes.GlobalsNode;
import nodes.LoopNode;
import nodes.Node;
import nodes.StartNode;
import nodes.StatementNode;
import nodes.TypesNode;
import playground.Playground;
import playground.SimulationScript;
import ports.DataPort;
import ports.Port;
import types.CustomType;
import types.GLSLType;
import types.ProjectType;
import types.Resource;
import types.ResourceType;

public class Project implements Serializable {

	private static final long serialVersionUID = -5034273809770939736L;

	private transient long lastSelectTick;
	private transient Point prevPoint;
	private transient Port selectedPort;
	private transient Node selectedNode;
	private transient Node draggingNode;
	private transient Point cursorPosition;
	private transient String generatedShader;
	private transient Playground playground;
	private boolean projectChanged;

	public transient SimulationScript script;
	public boolean isCustom = false;
	public String name;
	public ProjectType projectType;
	public int[] localGroupSize;
	public ArrayList<Template> templates;
	public ArrayList<Symbol> defines;
	public ArrayList<CustomType> types;
	public ArrayList<Constant> constants;
	public ArrayList<Resource> resources;
	public ArrayList<Variable> globalVariables;
	public ArrayList<Function> functions;
	public Function currentFunction;
	public Function mainFunction;

	public Project(String name, ProjectType type) {
		this.name = name;
		this.projectType = type;
		init();
	}

	private void init() {
		currentProject = this;
		localGroupSize = new int[] { 1, 1, 1 };
		templates = new ArrayList<Template>();
		defines = new ArrayList<Symbol>();
		types = new ArrayList<CustomType>();
		constants = new ArrayList<Constant>();
		resources = new ArrayList<Resource>();
		globalVariables = new ArrayList<Variable>();
		functions = new ArrayList<Function>();
		Template.initStandardTemplates(this);
		System.out.println("[PROJECT] Loaded " + templates.size() + " templates");
	}

	public synchronized Function createFunction(String name, boolean isMain) {
		Function function;
		if (isMain) {
			function = new Function("main", GLSLType.VOID, true, null);
			mainFunction = function;
		} else {
			if (name == null) {
				name = "function_" + functions.size();
			}
			function = new Function(name, GLSLType.VOID, true, null);
		}
		functions.add(function);

		return function;
	}

	public synchronized void updateConstants() {
		functions.forEach(function -> {
			if (function.isCustom) {
				function.constantsNode.updateConstants();
			}
		});
	}

	public synchronized void updateGlobals() {
		functions.forEach(function -> {
			if (function.isCustom) {
				function.globalsNode.updateGlobals();
			}
		});
	}

	public synchronized void deleteFunctionReferencies(Function function) {
		functions.forEach(currentFunction -> {
			if (currentFunction.isCustom) {
				currentFunction.statementNodes.forEach(node -> {
					if (node.getClass().equals(FunctionNode.class)) {
						FunctionNode functionNode = (FunctionNode) node;
						if (functionNode.referencedFunction == function) {
							functionNode.referencedFunction = null;
							functionNode.applyFunction();
						}
					}
				});
			}
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

	public synchronized void createPlayground(ProjectType type) {
		if (playground != null) {
			playground.stop();
			playground.close();
		}
		playground = new Playground();
		script = SimulationScript.createCustom("test.script");
	}

	public synchronized void startPlayground() {
		if (playground != null) {
			if (isCustom) {
				if (generateShader()) {
					script.setupScript(getGeneratedShader());
				} else {
					gui.showMessage("Shader generation failed");

					return;
				}
			}
			// HACK
			//String scriptCode = Loader.loadStandardScript("ants.script");
			//script.setupScript(scriptCode);
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
			DataPort.drawConnection(g, selectedPort.position.x, selectedPort.position.y, cursorPosition.x,
					cursorPosition.y, Color.BLACK);
		}
	}

	public synchronized void mousePressed(Point p) {
		Port port = getPortOnPoint(p);
		if (port != null) {
			selectedNode = null;
			if (selectedPort == null) {
				selectedPort = port;
				if (!selectedPort.isOutput) {
					selectedPort.destroyAllConnections();
				}
			}
		} else {
			Node node = getNodeOnPoint(p);
			if (node != null) {
				selectedNode = node;
				draggingNode = node;
				prevPoint = p;
			}
		}
	}

	public synchronized void mouseReleased(Point p) {
		Port port = getPortOnPoint(p);
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
		Port port = getPortOnPoint(p);
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
			Node node = getNodeOnPoint(p);
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

	private void openNodeEditor(Node node) {
		Class<?> nodeType = node.getClass();
		if (nodeType.equals(StatementNode.class)) {
			gui.openStatementNodeEditor((StatementNode) node);
		} else if (nodeType.equals(BranchNode.class)) {
			gui.openBranchNodeEditor((BranchNode) node);
		} else if (nodeType.equals(LoopNode.class)) {
			gui.openLoopNodeEditor((LoopNode) node);
		} else if (nodeType.equals(FunctionNode.class)) {
			gui.openFunctionNodeEditor((FunctionNode) node);
		} else if (nodeType.equals(DefinesNode.class)) {
			gui.openDefinesEditor(currentProject);
		} else if (nodeType.equals(TypesNode.class)) {
			gui.openTypesListEditor(currentProject);
		} else if (nodeType.equals(ConstantsNode.class)) {
			gui.openConstantsEditor(this);
		} else if (nodeType.equals(GlobalsNode.class)) {
			gui.openGlobalsEditor(currentFunction);
		} else if (nodeType.equals(StartNode.class)) {
			gui.openFunctionEditor(currentFunction);
		} else if (nodeType.equals(EndNode.class)) {
			gui.openFunctionEditor(currentFunction);
		}
	}

	private Node getNodeOnPoint(Point p) {
		if (currentFunction != null) {
			return currentFunction.getNodeOnPoint(p);
		}

		return null;
	}

	private Port getPortOnPoint(Point p) {
		if (currentFunction != null) {
			return currentFunction.getPortOnPoint(p);
		}

		return null;
	}

	public synchronized String[] getFunctions() {
		String[] functionsArray = new String[functions.size()];

		for (int i = 0; i < functions.size(); i++) {
			Function function = functions.get(i);
			functionsArray[i] = function.name;
		}

		return functionsArray;
	}

	public synchronized Function getFunctionByName(String functionName) {
		for (Function function : functions) {
			if (function.name.equals(functionName)) {
				return function;
			}
		}

		return null;
	}

	public synchronized boolean verifyFunction(Function function) {
		if (function.isCustom) {
			if (!function.verifyNodes()) {
				return false;
			}
		}

		return true;
	}

	public synchronized boolean verifyFunctions() {
		for (Function function : functions) {
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
			if (projectType == ProjectType.COMPUTE) {
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
				for (Resource resource : resources) {
					if (resource.type == ResourceType.IMAGE) {
						hasImages = true;
						String format = resource.getAsImage().formatText;
						shaderCode += "layout(" + format + ", binding = " + index++ + ") image2D ";
						shaderCode += resource.name + ";" + NEW_LINE;
					}
				}
				if (hasImages) {
					shaderCode += TWO_LINES;
				}
				boolean hasBuffers = false;
				for (Resource resource : resources) {
					if (resource.type == ResourceType.BUFFER) {
						hasBuffers = true;
						shaderCode += "layout(std430, binding = " + index++ + ") buffer name" + NEW_LINE;
						shaderCode += "{" + NEW_LINE;
						shaderCode += TAB + resource.getAsBuffer().type + "[];" + NEW_LINE;
						shaderCode += "};" + NEW_LINE;
					}
				}
				if (hasBuffers) {
					shaderCode += TWO_LINES;
				}
			}
			if (mainFunction.startNode.outputs.size() > 0) {
				for (DataPort port : mainFunction.startNode.outputs) {
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
				for (Function function : functions) {
					if (function != mainFunction) {
						shaderCode += function.getPrototype() + ";" + NEW_LINE;
					}
				}
				shaderCode += NEW_LINE;
			}
			shaderCode += mainFunction.generateCode() + NEW_LINE;
			for (Function function : functions) {
				if (function != mainFunction) {
					shaderCode += function.generateCode() + NEW_LINE;
				}
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

	public static synchronized Project loadFromFile(File file) {
		return Serializer.readFromfile(file);
	}

	public static synchronized boolean saveToFile(File file, Project project) {
		return Serializer.saveToFile(file, project);
	}

}