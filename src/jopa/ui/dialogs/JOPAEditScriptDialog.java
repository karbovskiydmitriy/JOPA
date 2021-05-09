package jopa.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;

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
		super(owner, "Script", script);

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

			newScriptMenuItem
					.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

			newScriptMenuItem.addActionListener(e -> {
				scriptEditor.setText("");
			});
			validateMenuItem.addActionListener(e -> {
				// TODO script validation (good luck)
			});

			scriptMenu.add(newScriptMenuItem);
			scriptMenu.add(validateMenuItem);

			scriptMenuBar.add(scriptMenu);
		}

		setJMenuBar(scriptMenuBar);
	}

}