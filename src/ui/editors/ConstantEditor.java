package ui.editors;

import static util.TypeUtil.getNameForType;
import static util.TypeUtil.getTypeForName;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import app.Constant;
import util.TypeUtil;

public class ConstantEditor extends EditorComponent<Constant> {

	private static final long serialVersionUID = -3736353513643802472L;

	private JComboBox<String> typesComboBox;
	private JTextField nameField;
	private JTextField valueField;

	public ConstantEditor(Constant constant) {
		super(constant);
		typesComboBox = new JComboBox<String>(TypeUtil.getAllTypes());
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