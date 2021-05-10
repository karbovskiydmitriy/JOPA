package jopa.ui.dialogs;

import java.awt.Frame;

import jopa.nodes.JOPAFunctionNode;

public class JOPAEditFunctionNodeDialog extends JOPADialog<JOPAFunctionNode> {

	private static final long serialVersionUID = -6843184896079741622L;

	public JOPAEditFunctionNodeDialog(Frame owner, JOPAFunctionNode node) {
		super(owner, "Edit function node", node);
		// TODO interface init
		setVisible(true);
	}

	@Override
	protected void closing() {
		// TODO closing
	}

}