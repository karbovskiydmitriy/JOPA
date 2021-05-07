package jopa.ui.editors;

import static jopa.util.JOPATypeUtil.getNameForType;
import static jopa.util.JOPATypeUtil.getTypeForName;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import jopa.main.JOPAConstant;
import jopa.util.JOPATypeUtil;

public class JOPAConstantEditor extends JOPAEditorComponent<JOPAConstant> {

	private static final long serialVersionUID = -3736353513643802472L;

	private JComboBox<String> typesComboBox;
	private JTextField nameField;
	private JTextField valueField;

	public JOPAConstantEditor(JOPAConstant constant) {
		super(constant);
		typesComboBox = new JComboBox<String>(JOPATypeUtil.getAllTypes());
		typesComboBox.setSelectedItem(getNameForType(constant.type));
		nameField = new JTextField(constant.name);
		valueField = new JTextField(constant.value);
		add(typesComboBox);
		add(nameField);
		add(valueField);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

	@Override
	public void writeBack() {
		object.type = getTypeForName((String) typesComboBox.getSelectedItem());
		object.name = nameField.getText();
		object.value = valueField.getText();
	}

}