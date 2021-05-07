package jopa.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.TextArea;

import javax.swing.JScrollPane;

public class JOPAShowShaderDialog extends JOPADialog<String> {

	private static final long serialVersionUID = 1L;

	public JOPAShowShaderDialog(Frame owner, String shaderCode) {
		super(owner, "Generated shader", shaderCode);
		setLayout(new BorderLayout());
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