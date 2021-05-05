package jopa.ui.dialogs;

import java.awt.Frame;

import jopa.main.JOPATemplate;

public class JOPAEditTemplateDialog extends JOPADialog {

	private static final long serialVersionUID = 7445189603617253590L;

	public JOPAEditTemplateDialog(Frame owner, JOPATemplate template) {
		super(owner, "Edit template");
		setVisible(true);
	}

}