package JOPA;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class JOPAFunction {

	public String name;
	public ArrayList<JOPANode> nodes;

	public JOPAFunction(String name) {
		this.name = name;
		this.nodes = new ArrayList<JOPANode>(
				List.of(new JOPANode(new Rectangle(100, 100, 100, 100), "HEADER", "TEST_0", "();();foobar"),
						new JOPANode(new Rectangle(300, 100, 100, 100), "HEADER", "TEST_1", "();();foobar")));
	}

	public void draw(Graphics2D g, JOPANode selectedNode, JOPAPort selectedPort) {
		nodes.forEach(node -> node.draw(g, selectedNode, selectedPort));

	}
	
	public JOPANode getNodeOnPoint(Point p) {
		for (JOPANode node : nodes) {
			if (node.hit(p)) {
				return node;
			}
		}

		return null;
	}

	public JOPAPort getPortOnPoint(Point p) {
		for (JOPANode node : nodes) {
			JOPAPort port = node.hitPort(p);
			if (port != null) {
				return port;
			}
		}

		return null;
	}

}