package ui.editors;

import java.awt.Color;
import java.awt.TextArea;

import javax.swing.BorderFactory;

import ports.BranchPort;

public class BranchEditor extends EditorComponent<BranchPort> {

	private static final long serialVersionUID = 73170013702296708L;

	private TextArea conditionField;

	public BranchEditor(BranchPort branch) {
		super(branch);
		conditionField = new TextArea(branch.condition);
		add(conditionField);
		conditionField.setPreferredSize(getSize());
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

	@Override
	public void writeBack() {
		object.condition = conditionField.getText();
	}

}