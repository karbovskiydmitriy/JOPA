package jopa.ui.dialogs;

import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JLabel;

import jopa.main.JOPAProject;
import jopa.main.JOPAVariable;
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

		revalidate();
		repaint();
	}

	private void initMenu() {

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
		adjustGrid(area.getComponentCount() / 3, 3, 10, 10, 10, 10);
	}

}