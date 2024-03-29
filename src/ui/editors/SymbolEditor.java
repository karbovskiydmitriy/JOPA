package ui.editors;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

import app.Symbol;

public class SymbolEditor extends EditorComponent<Symbol> {

	private static final long serialVersionUID = -9083229189025827842L;

	private JTextField nameField;
	private JTextField valueField;

	public SymbolEditor(Symbol symbol) {
		super(symbol);
		nameField = new JTextField(symbol.name);
		valueField = new JTextField(symbol.value);
		add(nameField);
		add(valueField);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

	@Override
	public void writeBack() {
		object.name = nameField.getText();
		object.value = valueField.getText();
	}

}