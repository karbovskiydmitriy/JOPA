package jopa.ui.dialogs;

import static jopa.io.JOPAIO.loadTextFile;
import static jopa.io.JOPAIO.saveTextFile;
import static jopa.main.JOPAMain.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.TextArea;
import java.io.File;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import jopa.main.JOPATemplate;

public class JOPAEditTemplateDialog extends JOPADialog<JOPATemplate> {

	private static final long serialVersionUID = 7445189603617253590L;

	private final FileNameExtensionFilter templateFilter;

	private TextArea templateEditor;

	public JOPAEditTemplateDialog(Frame owner, JOPATemplate template) {
		super(owner, "Edit template", template);
		templateFilter = new FileNameExtensionFilter("Template file", "json");

		init();
		initMenu();

		setVisible(true);
	}

	@Override
	protected void closing() {
		object.template = templateEditor.getText();
	}

	private void init() {
		area.removeAll();
		
		area.setLayout(new BorderLayout());
		templateEditor = new TextArea(object.template);
		Font templateFont = new Font("Consolas", Font.BOLD, 18);
		templateEditor.setFont(templateFont);
		area.add(templateEditor);
		
		revalidate();
		repaint();
	}

	private void initMenu() {
		JMenuBar templateMenuBar = new JMenuBar();

		{
			JMenu templateMenu = new JMenu("template");

			JMenuItem clearMenuItem = new JMenuItem("clear");
			JMenuItem loadMenuItem = new JMenuItem("load");
			JMenuItem saveMenuItem = new JMenuItem("save");

			loadMenuItem.setAccelerator(KeyStroke.getKeyStroke('O', CTRL_MODIFIER));
			saveMenuItem.setAccelerator(KeyStroke.getKeyStroke('S', CTRL_MODIFIER));

			clearMenuItem.addActionListener(l -> {
				templateEditor.setText("");
				init();
			});
			loadMenuItem.addActionListener(l -> {
				File selectedFile = gui.showFileDialog(null, templateFilter, "Load template", false);
				if (selectedFile != null) {
					String code = loadTextFile(selectedFile.getAbsolutePath());
					object.template = code;
					// TODO init
					init();
				}
			});
			saveMenuItem.addActionListener(l -> {
				File selectedFile = gui.showFileDialog(null, templateFilter, "Load template", false);
				if (selectedFile != null) {
					String code = templateEditor.getText();
					saveTextFile(selectedFile.getAbsolutePath(), code);
				}
			});

			templateMenu.add(clearMenuItem);
			templateMenu.add(loadMenuItem);
			templateMenu.add(saveMenuItem);

			templateMenuBar.add(templateMenu);
		}

		setJMenuBar(templateMenuBar);
	}

}