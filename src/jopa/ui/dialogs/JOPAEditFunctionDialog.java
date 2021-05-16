package jopa.ui.dialogs;

import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import jopa.main.JOPAFunction;
import jopa.main.JOPAVariable;
import jopa.types.JOPAGLSLType;
import jopa.ui.editors.JOPAEditorComponent;
import jopa.ui.editors.JOPAGlobalVariableEditor;

public class JOPAEditFunctionDialog extends JOPADialog<JOPAFunction> {

	private static final long serialVersionUID = -664731452292221L;

	public JOPAFunction selectedFunction;

	public JOPAEditFunctionDialog(Frame owner, JOPAFunction function) {
		super(owner, "Edit function", function);

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

		for (int i = 0; i < object.args.size(); i++) {
			JOPAVariable arg = object.args.get(i);
			addEditor("args[" + i + "]", arg);
		}
		// TODO edit function return type
		adjustGrid(area.getComponentCount() / 3, 3, 10, 10, 10, 10);

		revalidate();
		repaint();
	}

	private void initMenu() {
		JMenuBar functionMenuBar = new JMenuBar();

		{
			JMenu functionMenu = new JMenu("function");

			JMenuItem newArgumentMenuItem = new JMenuItem("new argument");
			JMenuItem clearArgumentsMenuItem = new JMenuItem("clear arguments");

			newArgumentMenuItem.addActionListener(e -> {
				JOPAVariable globalVariable = new JOPAVariable(JOPAGLSLType.INT, "foobar");
				object.args.add(globalVariable);
				init();
			});
			clearArgumentsMenuItem.addActionListener(e -> {
				object.args.clear();
				init();
			});

			functionMenu.add(newArgumentMenuItem);
			functionMenu.add(clearArgumentsMenuItem);

			functionMenuBar.add(functionMenu);
		}

		setJMenuBar(functionMenuBar);
	}

	private void addEditor(String name, JOPAVariable variable) {
		JOPAEditorComponent<?> editor = new JOPAGlobalVariableEditor(variable);
		JLabel label = new JLabel(name, JLabel.TRAILING);
		label.setLabelFor(editor);
		JButton deleteButton = new JButton("delete");
		deleteButton.addActionListener(e -> {
			object.args.remove(variable);
			init();
		});
		area.add(label);
		area.add(editor);
		area.add(deleteButton);
		editors.add(editor);
	}

}