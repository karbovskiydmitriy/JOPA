package ui.dialogs;

import static app.Main.gui;

import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import app.Template;
import app.Variable;
import nodes.StatementNode;
import ports.DataPort;
import types.GLSLType;
import ui.editors.DataPortEditor;
import ui.editors.EditorComponent;

public class EditStatementNodeDialog extends Dialog<StatementNode> {

	private static final long serialVersionUID = -6507027855437162763L;

	public EditStatementNodeDialog(Frame owner, StatementNode node) {
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
			DataPort port = object.inputs.get(i);
			addEditor("input[" + i + "]", port);
		}
		for (int i = 0; i < object.outputs.size(); i++) {
			DataPort port = object.outputs.get(i);
			addEditor("output[" + i + "]", port);
		}
		adjustGrid(area.getComponentCount() / 3, 3, 10, 10, 10, 10);

		revalidate();
		repaint();
	}

	private void initMenu() {
		JMenuBar statementNodeMenuBar = new JMenuBar();

		{
			JMenu inputsMenu = new JMenu("inputs");

			JMenuItem newInputMenuItem = new JMenuItem("new input");

			newInputMenuItem.setAccelerator(KeyStroke.getKeyStroke('I', CTRL_MODIFIER));

			newInputMenuItem.addActionListener(l -> {
				object.createPort(new Variable(GLSLType.FLOAT, "input_port"), false, true);
				init();
			});

			inputsMenu.add(newInputMenuItem);

			statementNodeMenuBar.add(inputsMenu);
		}

		{
			JMenu outputsMenu = new JMenu("outputs");

			JMenuItem newOutputMenuItem = new JMenuItem("new output");

			newOutputMenuItem.setAccelerator(KeyStroke.getKeyStroke('O', CTRL_MODIFIER));

			newOutputMenuItem.addActionListener(l -> {
				object.createPort(new Variable(GLSLType.FLOAT, "output_port"), true, true);
				init();
			});

			outputsMenu.add(newOutputMenuItem);

			statementNodeMenuBar.add(outputsMenu);
		}

		{
			JMenu templateMenu = new JMenu("template");

			JMenuItem selectMenuItem = new JMenuItem("select");
			JMenuItem editMenuItem = new JMenuItem("edit");

			selectMenuItem.addActionListener(l -> {
				Template template = gui.selectTemplate();
				System.out.println(template);
				if (template != null) {
					object.template = template;
				}
			});
			editMenuItem.addActionListener(l -> {
				setVisible(false);
				gui.openTemplateEditor(object.template);
			});

			templateMenu.add(selectMenuItem);
			templateMenu.add(editMenuItem);

			statementNodeMenuBar.add(templateMenu);
		}

		setJMenuBar(statementNodeMenuBar);
	}

	private void addEditor(String name, DataPort port) {
		EditorComponent<?> editor = new DataPortEditor(port);
		JLabel label = new JLabel(name, JLabel.TRAILING);
		label.setLabelFor(editor);
		JButton deleteButton = new JButton("delete");
		deleteButton.addActionListener(l -> {
			object.deletePort(port);
			init();
		});
		area.add(label);
		area.add(editor);
		area.add(deleteButton);
		editors.add(editor);
	}

}