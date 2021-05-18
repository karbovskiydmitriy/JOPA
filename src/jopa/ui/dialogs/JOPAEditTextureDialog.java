package jopa.ui.dialogs;

import java.awt.Frame;

import jopa.graphics.JOPAImage;

public class JOPAEditTextureDialog extends JOPADialog<JOPAImage> {

	private static final long serialVersionUID = -7131920665569487567L;

	public JOPAEditTextureDialog(Frame owner, JOPAImage object) {
		super(owner, "Edit texture", object);

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