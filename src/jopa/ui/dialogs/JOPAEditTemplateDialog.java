package jopa.ui.dialogs;

import java.awt.Frame;

import jopa.main.JOPANodeTemplate;

public class JOPAEditTemplateDialog extends JOPADialog<JOPANodeTemplate> {

	private static final long serialVersionUID = 7445189603617253590L;

	public JOPAEditTemplateDialog(Frame owner, JOPANodeTemplate template) {
		super(owner, "Edit template", template);
		setVisible(true);
	}

	@Override
	protected void closing() {
	}

}