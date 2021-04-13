package jopa.ui;

import java.awt.Label;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JDialog;

public class JOPAMessageWindow extends JDialog {

	private static final long serialVersionUID = 43221962375631247L;

	public JOPAMessageWindow(Window owner, String text, String header) {
		super(owner);

		setTitle(header);
		setSize(300, 100);
		Label textLabel = new Label(text);
		textLabel.setAlignment(Label.CENTER);
		add(textLabel);
		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);

		// not invoking ?
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyCode() == 27) {
					dispose();
				}
//				System.out.println(e.getKeyCode());
			}
		});
	}

}