package jopa.ui.dialogs;

import static jopa.util.JOPAOGLUtil.validateShader;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.TextArea;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import jopa.main.JOPAMain;

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

			validateMenuItem.addActionListener(e -> {
				if (validateShader(object, JOPAMain.currentProject.projectType)) {
					JOPAMain.ui.showMessage("Shader compiled successfully");
				} else {
					JOPAMain.ui.showMessage("Shader did not compile");
				}
			});

			shaderMenu.add(validateMenuItem);

			showShaderMenuBar.add(shaderMenu);
		}

		setJMenuBar(showShaderMenuBar);
	}

}