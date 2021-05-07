package jopa.ui.dialogs;

import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import jopa.nodes.JOPAStatementNode;
import jopa.ports.JOPADataPort;
import jopa.ui.editors.JOPADataPortEditor;

public class JOPAEditStatementNodeDialog extends JOPADialog {

	private static final long serialVersionUID = -6507027855437162763L;

	private JOPAStatementNode node;

	public JOPAEditStatementNodeDialog(Frame owner, JOPAStatementNode node) {
		super(owner, "Edit statement node");
		this.node = node;

		JMenuBar statementMenuBar = new JMenuBar();

		{
			JMenu inputsMenu = new JMenu("inputs");

			JMenuItem newInputMenuItem = new JMenuItem("new input");

			newInputMenuItem
					.setAccelerator(KeyStroke.getKeyStroke('I', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

			newInputMenuItem.addActionListener(null);

			inputsMenu.add(newInputMenuItem);

			statementMenuBar.add(inputsMenu);
		}

		{
			JMenu outputsMenu = new JMenu("outputs");

			JMenuItem newOutputMenuItem = new JMenuItem("new output");

			newOutputMenuItem.addActionListener(null);

			outputsMenu.add(newOutputMenuItem);

			statementMenuBar.add(outputsMenu);
		}

		setJMenuBar(statementMenuBar);
		init(node);
		setVisible(true);
	}

	@Override
	protected void closing() {

	}

	private void init(JOPAStatementNode node) {
		for (int i = 0; i < node.inputs.size(); i++) {
			JOPADataPort port = node.inputs.get(i);
			JComponent editor = new JOPADataPortEditor(port);
			addEditorPair("input[" + i + "]", editor);
		}
		for (int i = 0; i < node.outputs.size(); i++) {
			JOPADataPort port = node.outputs.get(i);
			JComponent editor = new JOPADataPortEditor(port);
			addEditorPair("output[" + i + "]", editor);
		}
	}

	protected void addEditorPair(String name, JComponent editor) {
		JLabel l = new JLabel(name, JLabel.TRAILING);
		area.add(l);
		l.setLabelFor(editor);
		area.add(editor);
		JButton deleteButton = new JButton("delete");
		area.add(deleteButton);
		makeCompactGrid(area, area.getComponentCount() / 3, 3, 10, 10, 10, 10);
	}

}