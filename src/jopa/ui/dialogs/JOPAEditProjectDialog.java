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
import jopa.types.JOPAProjectType;

public class JOPAEditProjectDialog extends JOPADialog<JOPAProject> {

	private static final long serialVersionUID = -6455920925968222447L;

	private static final String[] PROJECT_TYPES = new String[] { "Fragment", "Compute", };

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
		projectTypeComboBox.setSelectedIndex((int) object.projectType.ordinal());
		projectTypeComboBox.setPreferredSize(new Dimension(200, 50));
		JButton editTemplatesButton = new JButton("Edit templates");
		JButton editDefinesButton = new JButton("Edit defines");
		JButton editTypesButton = new JButton("Edit types");
		JButton editResourcesButton = new JButton("Edit resources");
		JButton editConstantsButton = new JButton("Edit constants");
		JButton editGlobalsButton = new JButton("Edit globals");

		editTemplatesButton.addActionListener(e -> {
			gui.showTemplatesList(currentProject);
		});
		editDefinesButton.addActionListener(e -> {
			gui.openDefinesEditor(currentProject);
		});
		editTypesButton.addActionListener(e -> {
			gui.openTypesListEditor(object);
		});
		editResourcesButton.addActionListener(e -> {
			gui.openResourcesEditor(currentProject);
		});
		editConstantsButton.addActionListener(e -> {
			gui.openConstantsEditor(currentProject);
		});
		editGlobalsButton.addActionListener(e -> {
			gui.openGlobalsEditor(currentProject.getFunctionByName(currentProject.getFunctions()[0]));
		});

		area.add(projectTypeComboBox);
		area.add(editTemplatesButton);
		area.add(editDefinesButton);
		area.add(editTypesButton);
		area.add(editConstantsButton);
		area.add(editResourcesButton);
		area.add(editGlobalsButton);
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