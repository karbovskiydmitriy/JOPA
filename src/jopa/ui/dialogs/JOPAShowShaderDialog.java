package jopa.ui.dialogs;

import static jopa.main.JOPAMain.saveGeneratedShader;
import static jopa.main.JOPAMain.validateGeneratedShader;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.TextArea;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class JOPAShowShaderDialog extends JOPADialog<String> {

	private static final long serialVersionUID = 1L;

	public JOPAShowShaderDialog(Frame owner, String shaderCode) {
		super(owner, "Generated shader", shaderCode);

		init();
		initMenu();

		setVisible(true);
	}

	@Override
	protected void closing() {
	}

	private void init() {
		area.setLayout(new BorderLayout());
		TextArea shaderTextTestArea = new TextArea(object);
		shaderTextTestArea.setEditable(false);
		area.add(shaderTextTestArea);
	}

	private void initMenu() {
		JMenuBar showShaderMenuBar = new JMenuBar();

		{
			JMenu shaderMenu = new JMenu("shader");

			JMenuItem validateMenuItem = new JMenuItem("validate");
			JMenuItem saveToFileMenuItem = new JMenuItem("save to file");

			saveToFileMenuItem.setAccelerator(KeyStroke.getKeyStroke('S', CTRL_MODIFIER));

			validateMenuItem.addActionListener(e -> validateGeneratedShader());
			saveToFileMenuItem.addActionListener(e -> saveGeneratedShader());

			shaderMenu.add(validateMenuItem);
			shaderMenu.add(saveToFileMenuItem);

			showShaderMenuBar.add(shaderMenu);
		}

		setJMenuBar(showShaderMenuBar);
	}

}