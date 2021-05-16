package jopa.ui.editors;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;

import jopa.types.JOPAResource;

public class JOPAResourceEditor extends JOPAEditorComponent<JOPAResource> {

	private static final long serialVersionUID = -5506957299099537553L;

	private JComboBox<String> resourceTypeComboBox;
	private JButton editButton;

	public JOPAResourceEditor(JOPAResource object) {
		super(object);
		resourceTypeComboBox = new JComboBox<String>(new String[] { "image", "buffer" });
		editButton = new JButton("Edit");
		add(resourceTypeComboBox);
		add(editButton);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

	@Override
	public void writeBack() {
	}

}