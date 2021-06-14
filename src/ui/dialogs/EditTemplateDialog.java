package ui.dialogs;

import static app.Main.gui;
import static io.IO.loadTextFile;
import static io.IO.saveTextFile;

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

import app.Template;

public class EditTemplateDialog extends Dialog<Template> {

	private static final long serialVersionUID = 7445189603617253590L;

	private final FileNameExtensionFilter templateFilter;

	private TextArea templateEditor;

	public EditTemplateDialog(Frame owner, Template template) {
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
					init();
				}
			});
			saveMenuItem.addActionListener(l -> {
				File selectedFile = gui.showFileDialog(null, templateFilter, "Save template", true);
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