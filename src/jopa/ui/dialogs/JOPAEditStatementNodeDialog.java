package jopa.ui.dialogs;

import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import jopa.main.JOPAVariable;
import jopa.nodes.JOPAStatementNode;
import jopa.ports.JOPADataPort;
import jopa.types.JOPAGLSLType;
import jopa.ui.editors.JOPADataPortEditor;
import jopa.ui.editors.JOPAEditorComponent;

public class JOPAEditStatementNodeDialog extends JOPADialog<JOPAStatementNode> {

	private static final long serialVersionUID = -6507027855437162763L;

	public JOPAEditStatementNodeDialog(Frame owner, JOPAStatementNode node) {
		super(owner, "Edit statement node", node);

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

		for (int i = 0; i < object.inputs.size(); i++) {
			JOPADataPort port = object.inputs.get(i);
			addEditorPair("input[" + i + "]", port);
		}
		for (int i = 0; i < object.outputs.size(); i++) {
			JOPADataPort port = object.outputs.get(i);
			addEditorPair("output[" + i + "]", port);
		}
		adjustGrid(area.getComponentCount() / 3, 3, 10, 10, 10, 10);

		revalidate();
		repaint();
	}

	private void initMenu() {
		JMenuBar statementMenuBar = new JMenuBar();

		{
			JMenu inputsMenu = new JMenu("inputs");

			JMenuItem newInputMenuItem = new JMenuItem("new input");

			newInputMenuItem
					.setAccelerator(KeyStroke.getKeyStroke('I', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

			newInputMenuItem.addActionListener(e -> {
				object.createPort(new JOPAVariable(JOPAGLSLType.FLOAT, "input_port"), false, true);
				init();
			});

			inputsMenu.add(newInputMenuItem);

			statementMenuBar.add(inputsMenu);
		}

		{
			JMenu outputsMenu = new JMenu("outputs");

			JMenuItem newOutputMenuItem = new JMenuItem("new output");

			newOutputMenuItem
					.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

			newOutputMenuItem.addActionListener(e -> {
				object.createPort(new JOPAVariable(JOPAGLSLType.FLOAT, "output_port"), true, true);
				init();
			});

			outputsMenu.add(newOutputMenuItem);

			statementMenuBar.add(outputsMenu);
		}

		setJMenuBar(statementMenuBar);
	}

	protected void addEditorPair(String name, JOPADataPort port) {
		JOPAEditorComponent<?> editor = new JOPADataPortEditor(port);
		JLabel label = new JLabel(name, JLabel.TRAILING);
		label.setLabelFor(editor);
		JButton deleteButton = new JButton("delete");
		deleteButton.addActionListener(e -> {
			object.deletePort(port);
			init();
		});
		area.add(label);
		area.add(editor);
		area.add(deleteButton);
		editors.add(editor);
	}

}