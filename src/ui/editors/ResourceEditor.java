package ui.editors;

import static app.Main.gui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;

import types.Resource;
import types.ResourceType;

public class ResourceEditor extends EditorComponent<Resource> {

	private static final long serialVersionUID = -5506957299099537553L;

	private JButton editButton;

	public ResourceEditor(Resource object) {
		super(object);
		JLabel typeNameLabel = new JLabel();
		if (object.type == ResourceType.IMAGE) {
			typeNameLabel.setText("Texture " + object.name);
		} else if (object.type == ResourceType.BUFFER) {
			typeNameLabel.setText("Buffer " + object.name);
		}
		editButton = new JButton("Edit");
		editButton.addActionListener(l -> {
			if (object.type == ResourceType.IMAGE) {
				gui.openTextureEditor(object.getAsImage());
			} else if (object.type == ResourceType.BUFFER) {
				gui.openBufferEditor(object.getAsBuffer());
			}
		});
		add(typeNameLabel);
		add(editButton);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

	@Override
	public void writeBack() {
	}

}