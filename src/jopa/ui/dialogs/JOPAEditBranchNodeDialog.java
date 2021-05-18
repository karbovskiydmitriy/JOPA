package jopa.ui.dialogs;

import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import jopa.nodes.JOPABranchNode;
import jopa.ports.JOPABranchPort;
import jopa.ui.editors.JOPABranchEditor;
import jopa.ui.editors.JOPAEditorComponent;

public class JOPAEditBranchNodeDialog extends JOPADialog<JOPABranchNode> {

	private static final long serialVersionUID = -3013945811135524867L;

	public JOPAEditBranchNodeDialog(Frame owner, JOPABranchNode node) {
		super(owner, "Edit branch node", node);

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

		for (int i = 0; i < object.branches.size(); i++) {
			JOPABranchPort port = object.branches.get(i);
			addEditor("branches[" + i + "]", port);
		}
		adjustGrid(area.getComponentCount() / 3, 3, 10, 10, 10, 10);

		revalidate();
		repaint();
	}

	private void initMenu() {
		JMenuBar branchMenuBar = new JMenuBar();

		{
			JMenu branchMenu = new JMenu("branch");

			JMenuItem newBranchMenu = new JMenuItem("new");

			newBranchMenu.setAccelerator(KeyStroke.getKeyStroke('N', CTRL_MODIFIER));

			newBranchMenu.addActionListener(l -> {
				JOPABranchPort branch = new JOPABranchPort(object, "");
				object.branches.add(branch);
				init();
			});

			branchMenu.add(newBranchMenu);

			branchMenuBar.add(branchMenu);
		}

		setJMenuBar(branchMenuBar);
	}

	private void addEditor(String name, JOPABranchPort port) {
		JOPAEditorComponent<?> editor = new JOPABranchEditor(port);
		JLabel label = new JLabel(name, JLabel.TRAILING);
		label.setLabelFor(editor);
		JButton deleteButton = new JButton("delete");
		deleteButton.addActionListener(l -> {
			object.branches.remove(port);
			init();
		});
		area.add(label);
		area.add(editor);
		area.add(deleteButton);
		editors.add(editor);
	}

}