package jopa.ui.dialogs;

import java.awt.Frame;

import javax.swing.JLabel;
import javax.swing.JTextArea;

import jopa.graphics.JOPAImage;

public class JOPAEditTextureDialog extends JOPADialog<JOPAImage> {

	private static final long serialVersionUID = -7131920665569487567L;

	private JTextArea widthTextArea;
	private JTextArea heightTextArea;
	private JTextArea formatTextArea;

	public JOPAEditTextureDialog(Frame owner, JOPAImage object) {
		super(owner, "Edit texture", object);

		init();

		setVisible(true);
	}

	@Override
	protected void closing() {
		int width;
		int height;
		object.formatText = formatTextArea.getText();
		try {
			width = Integer.parseUnsignedInt(widthTextArea.getText());
			height = Integer.parseUnsignedInt(heightTextArea.getText());
		} catch (NumberFormatException e) {
			return;
		}
		if (width > 0 && height > 0) {
			object.width = width;
			object.height = height;
		}
	}

	private void init() {
		JLabel widthLabel = new JLabel("width");
		widthTextArea = new JTextArea("" + object.width);
		widthLabel.setLabelFor(widthTextArea);
		JLabel heightLabel = new JLabel("height");
		heightTextArea = new JTextArea("" + object.height);
		heightLabel.setLabelFor(heightTextArea);
		JLabel formatLabel = new JLabel("format");
		formatTextArea = new JTextArea("" + object.formatText);
		formatLabel.setLabelFor(formatTextArea);
		area.add(widthLabel);
		area.add(widthTextArea);
		area.add(heightLabel);
		area.add(heightTextArea);
		area.add(formatLabel);
		area.add(formatTextArea);
		adjustGrid(area.getComponentCount() / 2, 2, 10, 10, 10, 10);
		pack();
	}

}