package jopa.ui.dialogs;

import java.awt.Frame;

import jopa.nodes.JOPANode;

public class JOPAEditNodeDialog extends JOPADialog {

	private static final long serialVersionUID = -6507027855437162763L;

	public JOPAEditNodeDialog(Frame owner, JOPANode node) {
		super(owner, "Edit node");
		setSize(500, 500);
		setVisible(true);
	}

	@Override
	protected void closed() {
	}

}