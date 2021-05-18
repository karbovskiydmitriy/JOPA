package jopa.ui.dialogs;

import java.awt.Frame;

import jopa.types.JOPABuffer;

public class JOPAEditBufferDialog extends JOPADialog<JOPABuffer> {

	private static final long serialVersionUID = -5388121931707000865L;

	public JOPAEditBufferDialog(Frame owner, JOPABuffer object) {
		super(owner, "Edit buffer", object);

		init();
		initMenu();

		setVisible(true);
	}

	@Override
	protected void closing() {
	}

	private void init() {

	}

	private void initMenu() {

	}

}