package jopa.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.JTextArea;

import jopa.playground.JOPASimulationScript;

public class JOPAEditScriptDialog extends JOPADialog<JOPASimulationScript> {

	private static final long serialVersionUID = -6554667081986955507L;

	private JTextArea scriptEditor;

	public JOPAEditScriptDialog(Frame owner, JOPASimulationScript script) {
		super(owner, "Script", script);

		setLayout(new BorderLayout());
		scriptEditor = new JTextArea();
		scriptEditor.setText(script.getCode());
		scriptEditor.setFont(new Font("Consolas", Font.BOLD, 22));
		area.add(scriptEditor);

		setVisible(true);
	}

	@Override
	protected void closing() {
		object.setCode(scriptEditor.getText());
	}

}