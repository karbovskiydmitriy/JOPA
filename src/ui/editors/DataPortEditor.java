package ui.editors;

import static util.TypeUtil.getNameForType;
import static util.TypeUtil.getTypeForName;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import ports.DataPort;
import util.TypeUtil;

public class DataPortEditor extends EditorComponent<DataPort> {

	private static final long serialVersionUID = -1831292319745863889L;

	private JComboBox<String> typesComboBox;
	private JTextField nameField;

	public DataPortEditor(DataPort port) {
		super(port);
		setBackground(port.isOutput ? Color.GREEN : Color.GRAY);
		typesComboBox = new JComboBox<String>(TypeUtil.getAllTypes());
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