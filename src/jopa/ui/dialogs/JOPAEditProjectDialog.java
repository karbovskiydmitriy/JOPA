package jopa.ui.dialogs;

import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenuBar;

import jopa.main.JOPAMain;
import jopa.main.JOPAProject;
import jopa.main.JOPAProjectType;

public class JOPAEditProjectDialog extends JOPADialog<JOPAProject> {

	private static final long serialVersionUID = -6455920925968222447L;

	private static final String[] PROJECT_TYPES = new String[] { "Fragment", "Compute" };

	private JComboBox<String> projectTypeComboBox;

	public JOPAEditProjectDialog(Frame owner, JOPAProject project) {
		super(owner, "Project", project);

		init();
		initMenu();

		setVisible(true);
	}

	@Override
	protected void closing() {
		object.projectType = JOPAProjectType.valueOf(projectTypeComboBox.getSelectedItem().toString().toUpperCase());
	}

	private void init() {
		projectTypeComboBox = new JComboBox<String>(PROJECT_TYPES);
		projectTypeComboBox.setSelectedIndex((int) (object.projectType.ordinal() - 1));
		projectTypeComboBox.setPreferredSize(new Dimension(200, 50));
		JButton editResourceeButton = new JButton("Edit recources");
		editResourceeButton.addActionListener(e -> {
			JOPAMain.ui.openResourcesEditor(JOPAMain.currentProject);
		});
		area.add(projectTypeComboBox);
		area.add(editResourceeButton);
		adjustGrid(2, 1, 10, 10, 10, 10);

		pack();
	}

	private void initMenu() {
		JMenuBar projectMenuBar = new JMenuBar();

		{

		}

		setJMenuBar(projectMenuBar);
	}

}