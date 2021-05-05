package jopa.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import jopa.io.JOPASerializer;
import jopa.nodes.JOPAConstantsNode;
import jopa.nodes.JOPANode;
import jopa.nodes.JOPAStatementNode;
import jopa.playground.JOPAPlayground;
import jopa.ports.JOPADataPort;
import jopa.ports.JOPAPort;
import jopa.types.JOPAType;

public class JOPAWorkspace {

	private long lastSelectTick;

	private Point prevPoint;
	private JOPAPort selectedPort;
	private JOPANode selectedNode;
	private JOPANode draggingNode;
	private Point cursorPosition;
	private String name;
	private ArrayList<JOPAFunction> functions;
	private ArrayList<JOPAType> types;
	// private ArrayList<JOPATemplate> globals;
	private JOPAPlayground playground;
	private String generatedShader;

	public JOPAFunction currentFunction;

	public JOPAWorkspace(String name) {
		this.name = name;
		this.functions = new ArrayList<JOPAFunction>();
		this.types = new ArrayList<JOPAType>();
		// this.globals = new ArrayList<JOPATemplate>();
	}

	public synchronized JOPAFunction createFunction(String name) {
		if (name == null) {
			name = "function_" + functions.size();
		}
		JOPAFunction function = new JOPAFunction(name);
		functions.add(function);

		return function;
	}

	public synchronized boolean deleteFunction(String name) {
		return functions.removeIf(function -> function.name.equals(name));
	}

	public synchronized void selectFunction(int index) {
		if (functions.size() > 0) {
			currentFunction = functions.get(index);
		}
	}

	public synchronized void createPlayground() {
		if (playground == null) {
			playground = new JOPAPlayground();
		}
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

	public synchronized JOPAType createNewType(String name) {
		JOPAType type = new JOPAType(name);
		types.add(type);

		return type;
	}

	public synchronized boolean deleteType(String name) {
		return types.removeIf(type -> type.name.equals(name));
	}

	public synchronized void draw(Graphics2D g) {
		functions.forEach(function -> function.draw(g, selectedNode, selectedPort));
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
				// isDragging = true;
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
		// isDragging = false;
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
						Class<?> nodeType = node.getClass();
						if (nodeType.equals(JOPAStatementNode.class)) {
							JOPAMain.ui.editNode(node);
						} else if (nodeType.equals(JOPAConstantsNode.class)) {
							JOPAMain.ui.editConstants(currentFunction);
						}
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
				JOPAMain.ui.editNode(selectedNode);
			}
			break;
		default:
			break;
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
		boolean validated = true;

		for (JOPAFunction function : functions) {
			if (!verifyFunction(function)) {
				validated = false;
			}
		}

		if (validated) {
			JOPAMain.ui.showMessage("Workspace passed validation");
		} else {
			JOPAMain.ui.showMessage("Workspace contains errors");
		}

		return true;
	}

	public synchronized void showGeneratedShader() {
		if (generatedShader != null) {
			JOPAMain.ui.showShader(generatedShader);
		} else {
			JOPAMain.ui.showMessage("Shader not generated!");
		}
	}

	public static synchronized JOPAWorkspace loadFromFile(String fileName) {
		return JOPASerializer.readFromfile(fileName);
	}

	public static synchronized boolean saveToFile(String fileName, JOPAWorkspace project) {
		return JOPASerializer.saveToFile(project.name + ".jopa", project);
	}

}