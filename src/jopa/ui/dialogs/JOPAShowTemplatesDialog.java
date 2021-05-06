package jopa.ui.dialogs;

import java.awt.Frame;

public class JOPAShowTemplatesDialog extends JOPADialog {

	private static final long serialVersionUID = -8054549338315214051L;

	public JOPAShowTemplatesDialog(Frame owner) {
		super(owner, "Templates list");
		setVisible(true);
	}

	@Override
	protected void closed() {
	}

}