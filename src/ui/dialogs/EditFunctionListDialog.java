package ui.dialogs;

import static app.Main.gui;

import java.awt.Frame;

import javax.swing.JButton;

import app.Function;
import app.Project;

public class EditFunctionListDialog extends Dialog<Project> {

	private static final long serialVersionUID = 2808671805175640268L;

	public EditFunctionListDialog(Frame owner, Project project) {
		super(owner, "Functions", project);

		init();

		setVisible(true);
	}

	@Override
	protected void closing() {
	}

	private void init() {
		for (String functionName : object.getFunctions()) {
			Function function = object.getFunctionByName(functionName);
			if (function.isCustom) {
				JButton editFunctionButton = new JButton("Edit \"" + functionName + "\"");
				JButton deleteFunctionButton = new JButton("Delete \"" + functionName + "\"");
				editFunctionButton.addActionListener(l -> {
					setVisible(false);
					gui.openFunctionEditor(function);
				});
				deleteFunctionButton.addActionListener(l -> {
					object.deleteFunction(functionName);
					object.deleteFunctionReferencies(function);
					init();
				});
				add(editFunctionButton);
				add(deleteFunctionButton);
			}
		}
		adjustGrid(area.getComponentCount() / 2, 2, 10, 10, 10, 10);
		pack();
	}

}