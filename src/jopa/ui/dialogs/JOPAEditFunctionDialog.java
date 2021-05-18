package jopa.ui.dialogs;

import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;

import jopa.main.JOPAFunction;
import jopa.main.JOPAVariable;
import jopa.types.JOPAGLSLType;
import jopa.ui.editors.JOPAEditorComponent;
import jopa.ui.editors.JOPAGlobalVariableEditor;
import static jopa.util.JOPATypeUtil.*;

public class JOPAEditFunctionDialog extends JOPADialog<JOPAFunction> {

	private static final long serialVersionUID = -664731452292221L;

	private JTextArea nameTextArea;
	private JComboBox<String> returnTypeComboBox;

	public JOPAEditFunctionDialog(Frame owner, JOPAFunction function) {
		super(owner, "Edit function", function);

		init();
		initMenu();

		setVisible(true);
	}

	@Override
	protected void closing() {
		editors.forEach(editor -> editor.writeBack());
		object.name = nameTextArea.getText();
		object.returnType = getTypeForName((String) returnTypeComboBox.getSelectedItem());
		object.updateFunction();
	}

	private void init() {
		editors.clear();
		area.removeAll();

		JLabel nameLabel = new JLabel("name");
		nameTextArea = new JTextArea(object.name);
		JButton applyNameButton = new JButton("apply");
		applyNameButton.addActionListener(l -> {
			object.name = nameTextArea.getText();
			object.updateFunction();
		});
		add(nameLabel);
		add(nameTextArea);
		add(applyNameButton);
		for (int i = 0; i < object.args.size(); i++) {
			JOPAVariable arg = object.args.get(i);
			addEditor("args[" + i + "]", arg);
		}
		JLabel returnTypeLabel = new JLabel("return type");
		returnTypeComboBox = new JComboBox<String>(getAllTypes());
		returnTypeComboBox.setSelectedItem(getNameForType(object.returnType));
		JButton applyReturnTypeButton = new JButton("apply");
		applyNameButton.addActionListener(l -> {
			object.returnType = getTypeForName((String) returnTypeComboBox.getSelectedItem());
			object.updateFunction();
		});
		add(returnTypeLabel);
		add(returnTypeComboBox);
		add(applyReturnTypeButton);
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

			newArgumentMenuItem.addActionListener(l -> {
				JOPAVariable globalVariable = new JOPAVariable(JOPAGLSLType.INT, "foobar");
				object.args.add(globalVariable);
				object.updateFunction();
				init();
			});
			clearArgumentsMenuItem.addActionListener(l -> {
				object.args.clear();
				object.updateFunction();
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
		deleteButton.addActionListener(l -> {
			object.args.remove(variable);
			init();
		});
		area.add(label);
		area.add(editor);
		area.add(deleteButton);
		editors.add(editor);
	}

}