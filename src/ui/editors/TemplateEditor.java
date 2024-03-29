package ui.editors;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

import app.Template;

public class TemplateEditor extends EditorComponent<Template> {

	private static final long serialVersionUID = 1313263351292997404L;

	private JTextField nameTextField;

	public TemplateEditor(Template object) {
		super(object);
		nameTextField = new JTextField(object.name);
		add(nameTextField);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

	@Override
	public void writeBack() {
		object.name = nameTextField.getText();
	}

}