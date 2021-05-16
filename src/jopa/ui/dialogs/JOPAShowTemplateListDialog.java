package jopa.ui.dialogs;

import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import jopa.main.JOPANodeTemplate;
import jopa.main.JOPAProject;
import jopa.ui.editors.JOPAEditorComponent;
import jopa.ui.editors.JOPATemplateEditor;

public class JOPAShowTemplateListDialog extends JOPADialog<JOPAProject> {

	private static final long serialVersionUID = -8054549338315214051L;

	public JOPAShowTemplateListDialog(Frame owner, JOPAProject project) {
		super(owner, "Templates list", project);

		init();
		initMenu();

		setVisible(true);
	}

	@Override
	protected void closing() {
	}

	private void init() {
		editors.clear();
		area.removeAll();

		for (int i = 0; i < object.templates.size(); i++) {
			JOPANodeTemplate template = object.templates.get(i);
			addEditor("templates[" + i + "]", template);
		}
		adjustGrid(area.getComponentCount() / 3, 3, 10, 10, 10, 10);
		pack();

		revalidate();
		repaint();
	}

	private void initMenu() {
		JMenuBar templatesMenuBar = new JMenuBar();

		{
			JMenu templatesMenu = new JMenu("templates");

			JMenuItem newMenuItem = new JMenuItem("new");
			JMenuItem clearMenuItem = new JMenuItem("clear");

			newMenuItem.setAccelerator(KeyStroke.getKeyStroke('N', CTRL_MODIFIER));

			newMenuItem.addActionListener(e -> {
				JOPANodeTemplate template = JOPANodeTemplate.create("new template", "");
				object.templates.add(template);
				init();
			});
			clearMenuItem.addActionListener(e -> {
				object.templates.clear();
				init();
			});

			templatesMenu.add(newMenuItem);
			templatesMenu.add(clearMenuItem);

			templatesMenuBar.add(templatesMenu);
		}

		setJMenuBar(templatesMenuBar);
	}

	private void addEditor(String name, JOPANodeTemplate template) {
		JOPAEditorComponent<?> editor = new JOPATemplateEditor(template);
		JLabel label = new JLabel(name, JLabel.TRAILING);
		label.setLabelFor(editor);
		JButton deleteButton = new JButton("delete");
		deleteButton.addActionListener(e -> {
			object.templates.remove(template);
			init();
		});
		area.add(label);
		area.add(editor);
		area.add(deleteButton);
		editors.add(editor);
	}

}