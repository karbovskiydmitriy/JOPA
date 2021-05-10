package jopa.ui.dialogs;

import java.awt.Frame;

import jopa.nodes.JOPALoopNode;

public class JOPAEditLoopNodeDialog extends JOPADialog<JOPALoopNode> {

	private static final long serialVersionUID = 1180645266190778954L;

	public JOPAEditLoopNodeDialog(Frame owner, JOPALoopNode node) {
		super(owner, "Edit loop node", node);
		// TODO interface init
		setVisible(true);
	}

	@Override
	protected void closing() {
		// TODO closing
	}

}