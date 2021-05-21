package ui.dialogs;

import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import app.Project;
import app.Variable;
import types.GLSLType;
import ui.editors.EditorComponent;
import ui.editors.GlobalVariableEditor;

public class EditGlobalsDialog extends Dialog<Project> {

	private static final long serialVersionUID = -2077410402563597588L;

	public EditGlobalsDialog(Frame owner, Project project) {
		super(owner, "Edit globals", project);

		init();
		initMenu();

		setVisible(true);
	}

	@Override
	protected void closing() {
		editors.forEach(editor -> editor.writeBack());
	}

	private void init() {
		editors.clear();
		area.removeAll();

		for (int i = 0; i < object.globalVariables.size(); i++) {
			Variable variable = object.globalVariables.get(i);
			addEditor("defines[" + i + "]", variable);
		}
		adjustGrid(area.getComponentCount() / 3, 3, 10, 10, 10, 10);

		revalidate();
		repaint();
	}

	private void initMenu() {
		JMenuBar globalsMenuBar = new JMenuBar();

		{
			JMenu globalsMenu = new JMenu("global");

			JMenuItem newMenuItem = new JMenuItem("new");
			JMenuItem clearMenuItem = new JMenuItem("clear");

			newMenuItem.setAccelerator(KeyStroke.getKeyStroke('N', CTRL_MODIFIER));

			newMenuItem.addActionListener(l -> {
				Variable globalVariable = new Variable(GLSLType.INT, "foobar");
				object.globalVariables.add(globalVariable);
				object.updateGlobals();
				init();
			});
			clearMenuItem.addActionListener(l -> {
				object.globalVariables.clear();
				object.updateGlobals();
				init();
			});

			globalsMenu.add(newMenuItem);
			globalsMenu.add(clearMenuItem);

			globalsMenuBar.add(globalsMenu);
		}

		setJMenuBar(globalsMenuBar);
	}

	private void addEditor(String name, Variable variable) {
		EditorComponent<?> editor = new GlobalVariableEditor(variable);
		JLabel label = new JLabel(name, JLabel.TRAILING);
		label.setLabelFor(editor);
		JButton deleteButton = new JButton("delete");
		deleteButton.addActionListener(l -> {
			object.globalVariables.remove(variable);
			object.updateGlobals();
			init();
		});
		area.add(label);
		area.add(editor);
		area.add(deleteButton);
		editors.add(editor);
	}

}