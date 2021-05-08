package jopa.ui.dialogs;

import java.awt.Frame;

import javax.swing.JMenuBar;

import jopa.main.JOPAProject;

public class JOPAEditProjectDialog extends JOPADialog<JOPAProject> {

	private static final long serialVersionUID = -6455920925968222447L;

	public JOPAEditProjectDialog(Frame owner, JOPAProject project) {
		super(owner, "Project", project);

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
		JMenuBar projectMenuBar = new JMenuBar();
		
		{
			
		}
		
		setJMenuBar(projectMenuBar);
	}

}