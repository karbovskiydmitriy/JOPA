package ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Label;

public class MessageDialog extends Dialog<String> {

	private static final long serialVersionUID = 43221962375631247L;

	public MessageDialog(Frame owner, String text, String header) {
		super(owner, header, text);

		init();

		setVisible(true);
	}

	private void init() {
		setLayout(new BorderLayout());
		remove(area);
		setSize(300, 100);
		Label textLabel = new Label(object);
		textLabel.setAlignment(Label.CENTER);
		add(textLabel);
	}

	@Override
	protected void closing() {
	}

}