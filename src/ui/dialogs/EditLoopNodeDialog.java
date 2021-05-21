package ui.dialogs;

import java.awt.Frame;

import nodes.LoopNode;

// DECIDE remove
public class EditLoopNodeDialog extends Dialog<LoopNode> {

	private static final long serialVersionUID = 1180645266190778954L;

	public EditLoopNodeDialog(Frame owner, LoopNode node) {
		super(owner, "Edit loop node", node);
		setVisible(true);
	}

	@Override
	protected void closing() {
	}

}