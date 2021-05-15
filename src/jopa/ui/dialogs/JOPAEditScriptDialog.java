package jopa.ui.dialogs;

import static jopa.io.JOPAIO.loadTextFile;
import static jopa.io.JOPAIO.saveTextFile;
import static jopa.main.JOPAMain.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.io.File;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import jopa.playground.JOPASimulationScript;

public class JOPAEditScriptDialog extends JOPADialog<JOPASimulationScript> {

	private static final long serialVersionUID = -6554667081986955507L;

	private final FileNameExtensionFilter jopascriptFilter;

	private JTextArea scriptEditor;

	public JOPAEditScriptDialog(Frame owner, JOPASimulationScript script) {
		super(owner, "Edit script", script);
		jopascriptFilter = new FileNameExtensionFilter("JOPA script file", "jopascript");

		init();
		initMenu();

		setVisible(true);
	}

	@Override
	protected void closing() {
		object.setCode(scriptEditor.getText());
	}

	private void init() {
		area.removeAll();
		area.setLayout(new BorderLayout());
		scriptEditor = new JTextArea();
		scriptEditor.setText(object.getCode());
		scriptEditor.setFont(new Font("Consolas", Font.BOLD, 22));
		area.add(scriptEditor);
		pack();
	}

	private void initMenu() {
		JMenuBar scriptMenuBar = new JMenuBar();

		{
			JMenu scriptMenu = new JMenu("script");

			JMenuItem newScriptMenuItem = new JMenuItem("new script");
			JMenuItem validateMenuItem = new JMenuItem("validate");
			JMenuItem loadFromFileMenuItem = new JMenuItem("load from file");
			JMenuItem saveToFileMenuItem = new JMenuItem("save to file");

			newScriptMenuItem.setAccelerator(KeyStroke.getKeyStroke('N', CTRL_MODIFIER));
			loadFromFileMenuItem.setAccelerator(KeyStroke.getKeyStroke('O', CTRL_MODIFIER));
			saveToFileMenuItem.setAccelerator(KeyStroke.getKeyStroke('S', CTRL_MODIFIER));

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
			loadFromFileMenuItem.addActionListener(e -> {
				File selectedFile = gui.showFileDialog(null, jopascriptFilter, "Load JOPA script", false);
				if (selectedFile != null) {
					String code = loadTextFile(selectedFile.getAbsolutePath());
					object.setCode(code);
					init();
				}
			});
			saveToFileMenuItem.addActionListener(e -> {
				String code = object.getCode();
				File selectedFile = gui.showFileDialog(null, jopascriptFilter, "Save JOPA script", true);
				if (selectedFile != null) {
					saveTextFile(selectedFile.getAbsolutePath(), code);
					init();
				}
			});

			scriptMenu.add(newScriptMenuItem);
			scriptMenu.add(validateMenuItem);
			scriptMenu.add(loadFromFileMenuItem);
			scriptMenu.add(saveToFileMenuItem);

			scriptMenuBar.add(scriptMenu);
		}

		setJMenuBar(scriptMenuBar);
	}

}