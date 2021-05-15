package jopa.ui.dialogs;

import java.awt.Frame;
import java.util.ArrayList;

import jopa.main.JOPANodeTemplate;

public class JOPAShowTemplateListDialog extends JOPADialog<ArrayList<JOPANodeTemplate>> {

	private static final long serialVersionUID = -8054549338315214051L;

	public JOPAShowTemplateListDialog(Frame owner, ArrayList<JOPANodeTemplate> templates) {
		super(owner, "Templates list", templates);
		// TODO interface init
		setVisible(true);
	}

	@Override
	protected void closing() {
		// TODO closing
	}

}