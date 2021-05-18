package jopa.ui.dialogs;

import java.awt.Frame;

import javax.swing.JButton;

public class JOPAQuestionDialog extends JOPADialog<Boolean> {

	private static final long serialVersionUID = 8969733818418438240L;

	public JOPAQuestionDialog(Frame owner, String question) {
		super(owner, question, false);

		init();

		setVisible(true);
	}

	@Override
	protected void closing() {
	}

	private void init() {
		JButton okButton = new JButton("OK");
		okButton.addActionListener(l -> {
			object = true;
			setVisible(true);
		});
		area.add(okButton);
	}
	
	public boolean getAnswer() {
		return object;
	}

}