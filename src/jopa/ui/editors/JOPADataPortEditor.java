package jopa.ui.editors;

import static jopa.util.JOPATypeUtil.getNameForType;
import static jopa.util.JOPATypeUtil.getTypeForName;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import jopa.ports.JOPADataPort;
import jopa.util.JOPATypeUtil;

public class JOPADataPortEditor extends JOPAEditorComponent<JOPADataPort> {

	private static final long serialVersionUID = -1831292319745863889L;

	private JComboBox<String> typesComboBox;
	private JTextField nameField;

	public JOPADataPortEditor(JOPADataPort port) {
		super(port);
		setBackground(port.isOutput ? Color.GREEN : Color.GRAY);
		typesComboBox = new JComboBox<String>(JOPATypeUtil.getAllTypes());
		typesComboBox.setSelectedItem(getNameForType(object.variable.type));
		nameField = new JTextField(port.variable.name);
		add(typesComboBox);
		add(nameField);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

	@Override
	public void writeBack() {
		object.variable.type = getTypeForName((String) typesComboBox.getSelectedItem());
		object.variable.name = nameField.getText();
		object.update();
	}

}