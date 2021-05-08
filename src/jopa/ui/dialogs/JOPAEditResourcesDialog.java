package jopa.ui.dialogs;

import java.awt.Frame;

import jopa.main.JOPAProject;
import jopa.types.JOPAResource;

public class JOPAEditResourcesDialog extends JOPADialog<JOPAProject> {

	private static final long serialVersionUID = 1945776977252735362L;

	public JOPAEditResourcesDialog(Frame owner, JOPAProject object) {
		super(owner, "Resources", object);

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

		for (int i = 0; i < object.resources.size(); i++) {
			JOPAResource resource = object.resources.get(i);
			addEditorPair("resource[" + i + "]", resource);
		}
		adjustGrid(area, area.getComponentCount() / 3, 3, 10, 10, 10, 10);

		revalidate();
		repaint();
	}

	private void initMenu() {

	}

	protected void addEditorPair(String name, JOPAResource resource) {
		// JOPAEditorComponent<?> editor = new JOPADataPortEditor(port);
		// JLabel label = new JLabel(name, JLabel.TRAILING);
		// label.setLabelFor(editor);
		// JButton deleteButton = new JButton("delete");
		// deleteButton.addActionListener(e -> {
		// object.deletePort(port);
		// init();
		// });
		// area.add(label);
		// area.add(editor);
		// area.add(deleteButton);
		// editors.add(editor);
	}

}