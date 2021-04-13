package jopa.ui;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;

import javax.swing.JDialog;

import jopa.JOPAFunction;

public class JOPAEditFunctionDialog extends JDialog {

	private static final long serialVersionUID = -664731452292221L;

	public JOPAFunction selectedFunction;

	public JOPAEditFunctionDialog(Frame owner, JOPAFunction function) {
		super(owner);

		selectedFunction = function;
		setSize(500, 500);
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		for (int i = 0; i < function.inputs.size(); i++) {
			var input = function.inputs.get(i);
			var label = new Label(input.header);
			add(label);
			label.setSize(200, 20);
			label.setLocation(0, i * 40);
		}
		for (int i = 0; i < function.outputs.size(); i++) {
			var output = function.outputs.get(i);
			var label = new Label(output.header);
			add(label);
			label.setSize(200, 20);
			label.setLocation(300, i * 40);
		}
		
		Button b = new Button("OK");
		b.addActionListener(al -> setVisible(false));
		//add(b);

		setVisible(true);
	}

}