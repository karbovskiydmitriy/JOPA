package jopa.ui.dialogs;

import java.awt.Frame;

import jopa.nodes.JOPAStatementNode;

public class JOPAEditStatementNodeDialog extends JOPADialog {

	private static final long serialVersionUID = -6507027855437162763L;

	public JOPAEditStatementNodeDialog(Frame owner, JOPAStatementNode node) {
		super(owner, "Edit statement node");
		setVisible(true);
	}

	@Override
	protected void closed() {
	}

}