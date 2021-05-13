package jopa.ui.dialogs;

import static jopa.main.JOPAMain.gui;

import java.awt.Frame;

import javax.swing.JButton;

import jopa.main.JOPAFunction;
import jopa.main.JOPAProject;

public class JOPAShowFunctionListDialog extends JOPADialog<JOPAProject> {

	private static final long serialVersionUID = 2808671805175640268L;

	public JOPAShowFunctionListDialog(Frame owner, JOPAProject project) {
		super(owner, "Functions", project);

		init();

		setVisible(true);
	}

	@Override
	protected void closing() {
	}

	private void init() {
		for (String functionName : object.getFunctions()) {
			JOPAFunction function = object.getFunctionByName(functionName);
			JButton functionButton = new JButton("Edit \"" + functionName + "\"");
			functionButton.addActionListener(e -> {
				setVisible(false);
				gui.openFunctionEditor(function);
			});
			add(functionButton);
		}
		adjustGrid(area.getComponentCount(), 1, 10, 10, 10, 10);
		pack();
	}

}