package jopa.ui.dialogs;

import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import jopa.main.JOPAProject;
import jopa.main.JOPAVariable;
import jopa.types.JOPAGLSLType;
import jopa.ui.editors.JOPAEditorComponent;
import jopa.ui.editors.JOPAGlobalVariableEditor;

public class JOPAEditGlobalsDialog extends JOPADialog<JOPAProject> {

	private static final long serialVersionUID = -2077410402563597588L;

	public JOPAEditGlobalsDialog(Frame owner, JOPAProject project) {
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
			JOPAVariable variable = object.globalVariables.get(i);
			addEditorPair("defines[" + i + "]", variable);
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

			newMenuItem.setAccelerator(KeyStroke.getKeyStroke('N', CTRL_MODIFIER));

			newMenuItem.addActionListener(e -> {
				JOPAVariable globalVariable = new JOPAVariable(JOPAGLSLType.INT, "foobar");
				object.globalVariables.add(globalVariable);
				System.out.println(object.globalVariables.size());
				object.updateGlobals();
				init();
			});

			globalsMenu.add(newMenuItem);

			globalsMenuBar.add(globalsMenu);
		}

		setJMenuBar(globalsMenuBar);
	}

	protected void addEditorPair(String name, JOPAVariable variable) {
		JOPAEditorComponent<?> editor = new JOPAGlobalVariableEditor(variable);
		JLabel label = new JLabel(name, JLabel.TRAILING);
		label.setLabelFor(editor);
		JButton deleteButton = new JButton("delete");
		deleteButton.addActionListener(e -> {
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