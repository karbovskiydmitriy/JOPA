package jopa.ui.dialogs;

import java.awt.Frame;

import jopa.nodes.JOPABranchNode;

public class JOPAEditBranchNodeDialog extends JOPADialog<JOPABranchNode> {

	private static final long serialVersionUID = -3013945811135524867L;

	public JOPAEditBranchNodeDialog(Frame owner, JOPABranchNode node) {
		super(owner, "Edit branch node", node);
		// TODO interface init
		setVisible(true);
	}

	@Override
	protected void closing() {
		// TODO closing
	}

}