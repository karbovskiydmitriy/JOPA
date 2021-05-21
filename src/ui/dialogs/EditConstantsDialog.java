package ui.dialogs;

import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import app.Constant;
import app.Project;
import types.GLSLType;
import ui.editors.ConstantEditor;
import ui.editors.EditorComponent;

public class EditConstantsDialog extends Dialog<Project> {

	private static final long serialVersionUID = -2535204900792827922L;

	public EditConstantsDialog(Frame owner, Project project) {
		super(owner, "Constants", project);

		init();
		initMenu();

		setVisible(true);
	}

	@Override
	protected void closing() {
		editors.forEach(editor -> editor.writeBack());
		object.updateConstants();
	}

	private void init() {
		editors.clear();
		area.removeAll();

		for (int i = 0; i < object.constants.size(); i++) {
			Constant constant = object.constants.get(i);
			addEditor("constants[" + i + "]", constant);
		}
		adjustGrid(area.getComponentCount() / 3, 3, 10, 10, 10, 10);

		revalidate();
		repaint();
	}

	private void initMenu() {
		JMenuBar constantsMenuBar = new JMenuBar();

		{
			JMenu constantsMenu = new JMenu("constants");

			JMenuItem newConstantMenuItem = new JMenuItem("new constant");

			newConstantMenuItem.setAccelerator(KeyStroke.getKeyStroke('N', CTRL_MODIFIER));

			newConstantMenuItem.addActionListener(l -> {
				Constant newConstant = new Constant(GLSLType.INT, "foobar", "42");
				object.constants.add(newConstant);
				editors.forEach(editor -> editor.writeBack());
				object.updateConstants();
				init();
			});

			constantsMenu.add(newConstantMenuItem);

			constantsMenuBar.add(constantsMenu);
		}

		setJMenuBar(constantsMenuBar);
	}

	private void addEditor(String name, Constant constant) {
		EditorComponent<?> editor = new ConstantEditor(constant);
		JLabel label = new JLabel(name, JLabel.TRAILING);
		label.setLabelFor(editor);
		JButton deleteButton = new JButton("delete");
		deleteButton.addActionListener(l -> {
			object.constants.remove(constant);
			init();
		});
		area.add(label);
		area.add(editor);
		area.add(deleteButton);
		editors.add(editor);
	}

}