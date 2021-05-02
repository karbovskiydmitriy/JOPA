package jopa.ports;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

import jopa.nodes.JOPANode;

public class JOPAControlFlowPort extends JOPAPort {

	public ArrayList<JOPAControlFlowPort> connections;

	public JOPAControlFlowPort(JOPANode node, Point position, String name, boolean isOutput,
			JOPAControlFlowPort... connections) {
		super(node, position, isOutput, name);
		this.connections = new ArrayList<JOPAControlFlowPort>(Arrays.asList(connections));
	}

}