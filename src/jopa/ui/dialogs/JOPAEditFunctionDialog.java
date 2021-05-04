package jopa.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;

import javax.swing.JDialog;

import jopa.main.JOPAFunction;

public class JOPAEditFunctionDialog extends JOPADialog {

	private static final long serialVersionUID = -664731452292221L;

	public JOPAFunction selectedFunction;

	public JOPAEditFunctionDialog(Frame owner, JOPAFunction function) {
		super(owner);

		selectedFunction = function;
		setLayout(new BorderLayout());
		setSize(500, 500);
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		/*for (int i = 0; i < function.inputs.size(); i++) {
			JOPANode input = function.inputs.get(i);
			Label label = new Label(input.header);
			add(label);
			label.setSize(200, 20);
			label.setLocation(0, i * 40);
		}
		for (int i = 0; i < function.outputs.size(); i++) {
			JOPANode output = function.outputs.get(i);
			Label label = new Label(output.header);
			add(label);
			label.setSize(200, 20);
			label.setLocation(300, i * 40);
		}*/

		Button b = new Button("OK");
		b.addActionListener(al -> setVisible(false));
		// add(b);

		setVisible(true);
	}

}