package ui.dialogs;

import static app.Main.gui;

import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import app.Project;
import types.CustomType;

public class EditTypesListDialog extends Dialog<Project> {

	private static final long serialVersionUID = 8357189910475070239L;

	public EditTypesListDialog(Frame owner, Project project) {
		super(owner, "Edit types list", project);

		setMinimumSize(new Dimension(300, 300));
		init();
		initMenu();

		setVisible(true);
	}

	@Override
	protected void closing() {
	}

	private void init() {
		area.removeAll();

		for (int i = 0; i < object.types.size(); i++) {
			CustomType type = object.types.get(i);
			JLabel typeIndexLabel = new JLabel("types[" + i + "]");
			JLabel typeNameLabel = new JLabel("Type name: " + type.name);
			JButton editTypeButton = new JButton("edit");
			JButton deleteTypeButton = new JButton("delete");
			editTypeButton.addActionListener(l -> {
				gui.openTypeEditor(type);
			});
			deleteTypeButton.addActionListener(l -> {
				object.types.remove(type);
				init();
			});
			area.add(typeIndexLabel);
			area.add(typeNameLabel);
			area.add(editTypeButton);
			area.add(deleteTypeButton);
		}
		adjustGrid(area.getComponentCount() / 4, 4, 10, 10, 10, 10);
		pack();

		revalidate();
		repaint();
	}

	private void initMenu() {
		JMenuBar typesMenuBar = new JMenuBar();

		JMenu typeMenu = new JMenu("types");

		JMenuItem newTypeMenuItem = new JMenuItem("new type");
		JMenuItem clearTypesMenuItem = new JMenuItem("clear types");

		newTypeMenuItem.setAccelerator(KeyStroke.getKeyStroke('N', CTRL_MODIFIER));

		newTypeMenuItem.addActionListener(l -> {
			object.types.add(new CustomType("NewType"));
			init();
		});
		clearTypesMenuItem.addActionListener(l -> {
			object.types.clear();
			init();
		});

		typeMenu.add(newTypeMenuItem);
		typeMenu.add(clearTypesMenuItem);

		typesMenuBar.add(typeMenu);

		setJMenuBar(typesMenuBar);
	}

}