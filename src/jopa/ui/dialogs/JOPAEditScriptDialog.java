package jopa.ui.dialogs;

import static jopa.main.JOPAMain.*;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import jopa.playground.JOPASimulationScript;

public class JOPAEditScriptDialog extends JOPADialog<JOPASimulationScript> {

	private static final long serialVersionUID = -6554667081986955507L;

	private JTextArea scriptEditor;

	public JOPAEditScriptDialog(Frame owner, JOPASimulationScript script) {
		super(owner, "Edit script", script);

		init();
		initMenu();

		setVisible(true);
	}

	@Override
	protected void closing() {
		object.setCode(scriptEditor.getText());
	}

	private void init() {
		setLayout(new BorderLayout());
		scriptEditor = new JTextArea();
		scriptEditor.setText(object.getCode());
		scriptEditor.setFont(new Font("Consolas", Font.BOLD, 22));
		area.add(scriptEditor);
	}

	private void initMenu() {
		JMenuBar scriptMenuBar = new JMenuBar();

		{
			JMenu scriptMenu = new JMenu("script");

			JMenuItem newScriptMenuItem = new JMenuItem("new script");
			JMenuItem validateMenuItem = new JMenuItem("validate");

			newScriptMenuItem.setAccelerator(KeyStroke.getKeyStroke('N', CTRL_MODIFIER));

			newScriptMenuItem.addActionListener(e -> {
				scriptEditor.setText("");
			});
			validateMenuItem.addActionListener(e -> {
				boolean correct = true;
				String code = scriptEditor.getText();
				String[] lines = code.split(" ");
				for (String line : lines) {
					if (!line.contains("(") || !line.contains(")")) {
						correct = false;
						gui.showMessage(line + " is not a command");
						break;
					}
					String operationPart = line.substring(0, line.indexOf('('));
					String[] parts = operationPart.split(" ");
					if (object.getOperation(parts) == null) {
						correct = false;
						gui.showMessage("Unknown operation: " + operationPart);
						break;
					}
				}
				if (correct) {
					gui.showMessage("Script is correct");
				} else {
					gui.showMessage("Script contains errors");
				}
			});

			scriptMenu.add(newScriptMenuItem);
			scriptMenu.add(validateMenuItem);

			scriptMenuBar.add(scriptMenu);
		}

		setJMenuBar(scriptMenuBar);
	}

}