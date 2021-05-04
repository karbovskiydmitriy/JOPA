package jopa.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;

public abstract class JOPADialog extends JDialog {

	private static final long serialVersionUID = 5368138759243687896L;

	public JOPADialog(Frame owner, String title) {
		super(owner);
		setTitle(title);
		setLayout(new BorderLayout());
		setSize(500, 500);
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				removeWindowListener(this);
				closed();
				super.windowClosed(e);
			}
		});
	}

	protected void closed() {
	}

}