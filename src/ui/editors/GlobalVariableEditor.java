package ui.editors;

import static util.TypeUtil.getNameForType;
import static util.TypeUtil.getTypeForName;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import app.Variable;
import util.TypeUtil;

public class GlobalVariableEditor extends EditorComponent<Variable> {

	private static final long serialVersionUID = -5663533384827897141L;

	private JComboBox<String> typesComboBox;
	private JTextField nameField;

	public GlobalVariableEditor(Variable variable) {
		super(variable);
		typesComboBox = new JComboBox<String>(TypeUtil.getAllTypes());
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