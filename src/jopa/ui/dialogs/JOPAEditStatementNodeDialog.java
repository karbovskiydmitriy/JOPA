package jopa.ui.dialogs;

import java.awt.Frame;

import javax.swing.JLabel;
import javax.swing.JTextField;

import jopa.nodes.JOPAStatementNode;

public class JOPAEditStatementNodeDialog extends JOPADialog {

	private static final long serialVersionUID = -6507027855437162763L;

	private JOPAStatementNode node;

	public JOPAEditStatementNodeDialog(Frame owner, JOPAStatementNode node) {
		super(owner, "Edit statement node");
		this.node = node;

		// area.setLayout(new FlowLayout(FlowLayout.CENTER));
		String[] labels = new String[] { "1", "2", "3", "4", "5", "6" };
		for (String label : labels) {
			JLabel l = new JLabel(label, JLabel.TRAILING);
			area.add(l);
			JTextField textField = new JTextField(10);
			l.setLabelFor(textField);
			area.add(textField);
		}

		makeCompactGrid(area, labels.length, area.getComponentCount() / labels.length, 10, 10, 10, 10);

		setVisible(true);
	}

	@Override
	protected void closed() {

	}

}