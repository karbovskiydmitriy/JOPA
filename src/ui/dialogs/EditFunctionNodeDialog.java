package ui.dialogs;

import static app.Main.currentProject;

import java.awt.Frame;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import nodes.FunctionNode;

public class EditFunctionNodeDialog extends Dialog<FunctionNode> {

	private static final long serialVersionUID = -6843184896079741622L;

	private JComboBox<String> functionComboBox;

	public EditFunctionNodeDialog(Frame owner, FunctionNode node) {
		super(owner, "Edit function node", node);

		init();
		initMenu();

		setVisible(true);
	}

	@Override
	protected void closing() {
		String selectedFunction = (String) functionComboBox.getSelectedItem();
		if (!selectedFunction.equals(currentProject.currentFunction.name)) {
			object.referencedFunction = currentProject.getFunctionByName(selectedFunction);
		} else {
			object.referencedFunction = null;
		}
		object.applyFunction();
	}

	private void init() {
		setSize(300, 100);
		JLabel functionLabel = new JLabel("function");
		functionComboBox = new JComboBox<String>(currentProject.getFunctions());
		if (object.referencedFunction != null) {
			functionComboBox.setSelectedItem(object.referencedFunction.name);
		}
		add(functionLabel);
		add(functionComboBox);
		adjustGrid(1, 2, 10, 10, 10, 10);
	}

	private void initMenu() {
		JMenuBar functionNodeMenuBar = new JMenuBar();

		{
			JMenu functionMenu = new JMenu("function");

			JMenuItem createNewMenuItem = new JMenuItem("create new");

			createNewMenuItem.setAccelerator(KeyStroke.getKeyStroke('N', CTRL_MODIFIER));

			createNewMenuItem.addActionListener(l -> {

			});

			functionMenu.add(createNewMenuItem);

			functionNodeMenuBar.add(functionMenu);
		}

		setJMenuBar(functionNodeMenuBar);
	}

}