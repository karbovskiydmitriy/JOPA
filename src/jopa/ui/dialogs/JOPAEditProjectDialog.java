package jopa.ui.dialogs;

import static jopa.main.JOPAMain.currentProject;
import static jopa.main.JOPAMain.gui;
import static jopa.main.JOPAMain.verifyProject;

import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

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
		JButton editTypesButton = new JButton("Edit types");
		JButton editConstants = new JButton("Edit constants");
		editTypesButton.addActionListener(e -> {
			gui.openTypesListEditor(object);
		});
		editConstants.addActionListener(e -> {
			gui.openConstantsEditor(currentProject);
		});
		area.add(projectTypeComboBox);
		area.add(editTypesButton);
		area.add(editConstants);
		adjustGrid(area.getComponentCount(), 1, 10, 10, 10, 10);

		pack();
	}

	private void initMenu() {
		JMenuBar projectMenuBar = new JMenuBar();

		{
			JMenu projectMenu = new JMenu("project");

			JMenuItem verifyMenuItem = new JMenuItem("verify");

			verifyMenuItem.addActionListener(e -> verifyProject());

			projectMenu.add(verifyMenuItem);

			projectMenuBar.add(projectMenu);
		}

		setJMenuBar(projectMenuBar);
	}

}