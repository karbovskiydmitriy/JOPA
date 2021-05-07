package jopa.ui.dialogs;

import java.awt.Frame;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import jopa.nodes.JOPAStatementNode;
import jopa.ports.JOPADataPort;
import jopa.types.JOPAGLSLType;
import jopa.ui.editors.JOPADataPortEditor;
import jopa.ui.editors.JOPAEditorComponent;

public class JOPAEditStatementNodeDialog extends JOPADialog {

	private static final long serialVersionUID = -6507027855437162763L;

	private JOPAStatementNode node;
	private ArrayList<JOPAEditorComponent<?>> editors;

	public JOPAEditStatementNodeDialog(Frame owner, JOPAStatementNode node) {
		super(owner, "Edit statement node");
		this.node = node;
		this.editors = new ArrayList<JOPAEditorComponent<?>>();

		JMenuBar statementMenuBar = new JMenuBar();

		{
			JMenu inputsMenu = new JMenu("inputs");

			JMenuItem newInputMenuItem = new JMenuItem("new input");

			newInputMenuItem
					.setAccelerator(KeyStroke.getKeyStroke('I', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

			newInputMenuItem.addActionListener(e -> {
				node.createPort(JOPAGLSLType.JOPA_FLOAT, "input_port", false, true);
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
				node.createPort(JOPAGLSLType.JOPA_FLOAT, "output_port", true, true);
				init();
			});

			outputsMenu.add(newOutputMenuItem);

			statementMenuBar.add(outputsMenu);
		}

		setJMenuBar(statementMenuBar);
		init();
		setVisible(true);
	}

	@Override
	protected void closing() {
		editors.forEach(editor -> editor.writeBack());
	}

	private void init() {
		editors.clear();
		area.removeAll();
		for (int i = 0; i < node.inputs.size(); i++) {
			JOPADataPort port = node.inputs.get(i);
			addEditorPair("input[" + i + "]", port);
		}
		for (int i = 0; i < node.outputs.size(); i++) {
			JOPADataPort port = node.outputs.get(i);
			addEditorPair("output[" + i + "]", port);
		}
		revalidate();
		repaint();
	}

	protected void addEditorPair(String name, JOPADataPort port) {
		JOPAEditorComponent<?> editor = new JOPADataPortEditor(port);
		JLabel label = new JLabel(name, JLabel.TRAILING);
		label.setLabelFor(editor);
		JButton deleteButton = new JButton("delete");
		deleteButton.addActionListener(e -> {
			node.deletePort(port);
			init();
		});
		area.add(label);
		area.add(editor);
		area.add(deleteButton);
		editors.add(editor);
		makeCompactGrid(area, area.getComponentCount() / 3, 3, 10, 10, 10, 10);
	}

}