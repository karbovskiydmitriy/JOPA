package ui.dialogs;

import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import app.Project;
import app.Template;
import ui.editors.EditorComponent;
import ui.editors.TemplateEditor;

public class ShowTemplateListDialog extends Dialog<Project> {

	private static final long serialVersionUID = -8054549338315214051L;

	public ShowTemplateListDialog(Frame owner, Project project) {
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
			Template template = object.templates.get(i);
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

			newMenuItem.addActionListener(l -> {
				Template template = Template.create("new template", "");
				object.templates.add(template);
				init();
			});
			clearMenuItem.addActionListener(l -> {
				object.templates.clear();
				init();
			});

			templatesMenu.add(newMenuItem);
			templatesMenu.add(clearMenuItem);

			templatesMenuBar.add(templatesMenu);
		}

		setJMenuBar(templatesMenuBar);
	}

	private void addEditor(String name, Template template) {
		EditorComponent<?> editor = new TemplateEditor(template);
		JLabel label = new JLabel(name, JLabel.TRAILING);
		label.setLabelFor(editor);
		JButton deleteButton = new JButton("delete");
		deleteButton.addActionListener(l -> {
			object.templates.remove(template);
			init();
		});
		area.add(label);
		area.add(editor);
		area.add(deleteButton);
		editors.add(editor);
	}

}