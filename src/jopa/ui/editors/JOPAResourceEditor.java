package jopa.ui.editors;

import static jopa.main.JOPAMain.gui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;

import jopa.types.JOPAResource;
import jopa.types.JOPAResourceType;

public class JOPAResourceEditor extends JOPAEditorComponent<JOPAResource> {

	private static final long serialVersionUID = -5506957299099537553L;

	private JButton editButton;

	public JOPAResourceEditor(JOPAResource object) {
		super(object);
		JLabel typeNameLabel = new JLabel();
		if (object.type == JOPAResourceType.IMAGE) {
			typeNameLabel.setText("Texture " + object.name);
		} else if (object.type == JOPAResourceType.BUFFER) {
			typeNameLabel.setText("Buffer " + object.name);
		}
		editButton = new JButton("Edit");
		editButton.addActionListener(l -> {
			if (object.type == JOPAResourceType.IMAGE) {
				gui.openTextureEditor(object.getAsImage());
			} else if (object.type == JOPAResourceType.BUFFER) {
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