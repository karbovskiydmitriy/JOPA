package ui.dialogs;

import java.awt.Frame;

import javax.swing.JLabel;
import javax.swing.JTextArea;

import types.Buffer;

public class EditBufferDialog extends Dialog<Buffer> {

	private static final long serialVersionUID = -5388121931707000865L;

	private JTextArea typeTextArea;
	private JTextArea countTextArea;

	public EditBufferDialog(Frame owner, Buffer object) {
		super(owner, "Edit buffer", object);

		init();
		initMenu();

		setVisible(true);
	}

	@Override
	protected void closing() {
		object.type = typeTextArea.getText();
		int count;
		try {
			count = Integer.parseUnsignedInt(countTextArea.getText());
		} catch (NumberFormatException e) {
			return;
		}
		object.length = count;
	}

	private void init() {
		JLabel typeLabel = new JLabel("type");
		typeTextArea = new JTextArea("" + object.type);
		typeLabel.setLabelFor(typeTextArea);
		JLabel countLabel = new JLabel("count");
		countTextArea = new JTextArea("" + object.length);
		countLabel.setLabelFor(countTextArea);
		area.add(typeLabel);
		area.add(typeTextArea);
		area.add(countLabel);
		area.add(countTextArea);
		adjustGrid(area.getComponentCount() / 2, 2, 10, 10, 10, 10);
		pack();
	}

	private void initMenu() {

	}

}