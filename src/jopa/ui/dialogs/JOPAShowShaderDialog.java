package jopa.ui.dialogs;

import java.awt.Frame;
import java.awt.TextArea;

import javax.swing.JScrollPane;

public class JOPAShowShaderDialog extends JOPADialog {

	private static final long serialVersionUID = 1L;

	public JOPAShowShaderDialog(Frame owner, String shaderCode) {
		super(owner, "Generated shader");
		TextArea shaderTextTestArea = new TextArea(shaderCode);
		shaderTextTestArea.setEditable(false);
		JScrollPane scrolPane = new JScrollPane(shaderTextTestArea);
		add(scrolPane);
		setVisible(true);
	}

	@Override
	protected void closing() {
	}

}