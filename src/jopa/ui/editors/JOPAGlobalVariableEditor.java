package jopa.ui.editors;

import static jopa.util.JOPATypeUtil.getNameForType;
import static jopa.util.JOPATypeUtil.getTypeForName;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import jopa.main.JOPAVariable;
import jopa.util.JOPATypeUtil;

public class JOPAGlobalVariableEditor extends JOPAEditorComponent<JOPAVariable> {

	private static final long serialVersionUID = -5663533384827897141L;

	private JComboBox<String> typesComboBox;
	private JTextField nameField;

	public JOPAGlobalVariableEditor(JOPAVariable variable) {
		super(variable);
		typesComboBox = new JComboBox<String>(JOPATypeUtil.getAllTypes());
		typesComboBox.setSelectedItem(getNameForType(variable.type));
		nameField = new JTextField(variable.name);
		add(typesComboBox);
		add(nameField);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

	@Override
	public void writeBack() {
		object.type = getTypeForName((String) typesComboBox.getSelectedItem());
		object.name = nameField.getText();
	}

}