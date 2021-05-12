package jopa.ui.dialogs;

import static jopa.main.JOPAMain.currentProject;

import java.awt.Frame;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import jopa.nodes.JOPAFunctionNode;

public class JOPAEditFunctionNodeDialog extends JOPADialog<JOPAFunctionNode> {

	private static final long serialVersionUID = -6843184896079741622L;

	private JComboBox<String> functionComboBox;

	public JOPAEditFunctionNodeDialog(Frame owner, JOPAFunctionNode node) {
		super(owner, "Edit function node", node);

		init();
		initMenu();

		setVisible(true);
	}

	@Override
	protected void closing() {
		String selectedFunction = (String) functionComboBox.getSelectedItem();
		if (!selectedFunction.equals(currentProject.currentFunction.name)) {
			object.applyFunction(currentProject.getFunctionByName(selectedFunction));
		} else {
			object.applyFunction(null);
		}
	}

	private void init() {
		JLabel functionLabel = new JLabel("function");
		functionComboBox = new JComboBox<String>(currentProject.getFunctions());
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

			createNewMenuItem.addActionListener(e -> {

			});

			functionMenu.add(createNewMenuItem);

			functionNodeMenuBar.add(functionMenu);
		}

		setJMenuBar(functionNodeMenuBar);
	}

}