package jopa.nodes;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import jopa.main.JOPAVariable;
import jopa.ports.JOPAPort;

public class JOPAVariableNode extends JOPANode {

	private static final long serialVersionUID = -4245064405386575712L;

	public JOPAVariable variable;

	public JOPAVariableNode(Rectangle rect, String header) {
		super(rect, header);
	}

	@Override
	protected void init() {
		super.init();
		// TODO init
	}

	@Override
	public boolean check() {
		return true;
	}

	@Override
	public String generateCode() {
		return variable.toString();
	}

	@Override
	public boolean remove() {
		return false;
	}

	@Override
	public void draw(Graphics2D g, JOPANode selectedNode, JOPAPort selectedPort) {
		super.draw(g, selectedNode, selectedPort);
	}

}