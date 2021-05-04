package jopa.ui.dialogs;

import java.awt.Button;
import java.awt.Frame;

import jopa.main.JOPAFunction;

public class JOPAEditFunctionDialog extends JOPADialog {

	private static final long serialVersionUID = -664731452292221L;

	public JOPAFunction selectedFunction;

	public JOPAEditFunctionDialog(Frame owner, JOPAFunction function) {
		super(owner, "Edit function");

		selectedFunction = function;

		/*
		 * for (int i = 0; i < function.inputs.size(); i++) { JOPANode input =
		 * function.inputs.get(i); Label label = new Label(input.header); add(label);
		 * label.setSize(200, 20); label.setLocation(0, i * 40); } for (int i = 0; i <
		 * function.outputs.size(); i++) { JOPANode output = function.outputs.get(i);
		 * Label label = new Label(output.header); add(label); label.setSize(200, 20);
		 * label.setLocation(300, i * 40); }
		 */

		Button b = new Button("OK");
		b.addActionListener(al -> setVisible(false));
		// add(b);

		setVisible(true);
	}

	@Override
	protected void closed() {
		// TODO
	}

}