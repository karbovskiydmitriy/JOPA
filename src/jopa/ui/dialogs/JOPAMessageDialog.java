package jopa.ui.dialogs;

import java.awt.Label;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JDialog;

public class JOPAMessageDialog extends JDialog {

	private static final long serialVersionUID = 43221962375631247L;

	public JOPAMessageDialog(Window owner, String text, String header) {
		super(owner);

		setTitle(header);
		setSize(300, 100);
		Label textLabel = new Label(text);
		textLabel.setAlignment(Label.CENTER);
		add(textLabel);
		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		textLabel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent ce) {
				textLabel.requestFocusInWindow();
			}
		});
		
		pack();

		setVisible(true);
	}

}