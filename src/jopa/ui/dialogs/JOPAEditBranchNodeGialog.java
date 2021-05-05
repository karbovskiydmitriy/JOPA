package jopa.ui.dialogs;

import java.awt.Frame;

import jopa.nodes.JOPABranchNode;

public class JOPAEditBranchNodeGialog extends JOPADialog {

	private static final long serialVersionUID = -3013945811135524867L;

	public JOPAEditBranchNodeGialog(Frame owner, JOPABranchNode node) {
		super(owner, "edit branch node");
		setVisible(true);
	}

	@Override
	protected void closed() {
	}

}