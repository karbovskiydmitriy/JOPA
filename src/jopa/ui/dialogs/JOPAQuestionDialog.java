package jopa.ui.dialogs;

import java.awt.Frame;

import javax.swing.JDialog;

public class JOPAQuestionDialog extends JDialog {

	private static final long serialVersionUID = 8969733818418438240L;

	public JOPAQuestionDialog(Frame owner, String question) {
		super(owner);
		setTitle(question);
		// TODO interface
		setVisible(true);
	}

}