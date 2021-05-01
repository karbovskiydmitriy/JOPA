package jopa;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import jopa.nodes.JOPANode;
import jopa.nodes.JOPADataPort;
import jopa.playground.JOPAPlayground;
import jopa.types.JOPAType;

public class JOPAWorkspace {

	public String name;
	public ArrayList<JOPAFunction> functions;
	public ArrayList<JOPAType> types;
	public ArrayList<JOPAFormula> globals;
	public JOPAPlayground playground;
	public JOPAFunction currentFunction;
	public JOPADataPort selectedPort;
	public JOPANode selectedNode;
	public JOPANode draggingNode;
	public Point cursorPosition;
	public Point prevPoint;
	public boolean isDragging;
	public boolean justReleased;

	public JOPAWorkspace(String name) {
		this.name = name;
		this.functions = new ArrayList<JOPAFunction>();
		this.types = new ArrayList<JOPAType>();
		this.globals = new ArrayList<JOPAFormula>();
	}

	public synchronized JOPAFunction createFunction(String name) {
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
			JOPADataPort.drawConnection(g, selectedPort.position, cursorPosition, Color.BLACK);
		}
	}

	public synchronized void mousePressed(Point p) {
		JOPADataPort port = getPortOnPoint(p);
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
				isDragging = true;
				prevPoint = p;
			}
		}
	}

	public synchronized void mouseReleased(Point p) {
		JOPADataPort port = getPortOnPoint(p);
		if (port != null) {
			if (makeConnection(port, selectedPort)) {
				selectedPort = null;
			} else {
				if (selectedPort != port) {
					selectedPort = null;
				}
			}
		} else {
			selectedPort = null;
		}
		isDragging = false;
		selectedNode = null;
		justReleased = true;
	}

	public synchronized void mouseClicked(Point p) {
		if (justReleased) {
			justReleased = false;
		} else {
			JOPADataPort port = getPortOnPoint(p);
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
					if (makeConnection(selectedPort, port)) {
						selectedPort = null;
					} else {
						selectedPort = null;
					}
				}
			} else {
				JOPANode node = getNodeOnPoint(p);
				if (node != null) {
					selectedPort = null;
				}
			}
		}
	}

	public synchronized void mouseMoved(Point p) {
		cursorPosition = p;
		if (selectedNode != null) {
			selectedPort = null;
			selectedNode.move(p.x - prevPoint.x, p.y - prevPoint.y);
			prevPoint = p;
		}
	}

	public synchronized void keyTyped(int keyCode) {
		switch (keyCode) {
		case 9:
			if (selectedNode != null) {
				functions.forEach(function -> function.statements.remove(selectedNode));
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

	private JOPADataPort getPortOnPoint(Point p) {
		if (currentFunction != null) {
			return currentFunction.getPortOnPoint(p);
		}

		return null;
	}

	public boolean verifyFunction(JOPAFunction function) {
		if (!function.verifyNodes()) {
			return false;
		}

		return true;
	}

	public boolean verifyFunctions() {
		boolean validated = true;

		for (JOPAFunction function : functions) {
			if (!verifyFunction(function)) {
				validated = false;
			}
		}

		if (validated) {
			System.out.println("workspace " + name + " OK");
		} else {
			System.out.println("workspace " + name + " not OK");
		}

		return true;
	}

	private static boolean makeConnection(JOPADataPort from, JOPADataPort to) {
		if (from == null || to == null) {
			return false;
		}
		if (from.isOutput == to.isOutput) {
			return false;
		}
		if (from.node == to.node) {
			return false;
		}

		if (!from.isOutput) {
			from.destroyAllConnections();
		} else if (!to.isOutput) {
			to.destroyAllConnections();
		}

		from.connections.add(to);
		to.connections.add(from);

		return true;
	}

}