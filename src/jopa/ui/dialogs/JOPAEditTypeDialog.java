package jopa.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;

import static jopa.main.JOPAMain.*;
import static jopa.util.JOPAOGLUtil.*;

import jopa.main.JOPAProjectType;
import jopa.types.JOPACustomType;

public class JOPAEditTypeDialog extends JOPADialog<JOPACustomType> {

	private static final long serialVersionUID = -8762774190661954314L;

	private JTextArea typeEditor;

	public JOPAEditTypeDialog(Frame owner, JOPACustomType type) {
		super(owner, "Edit type", type);

		init();
		initMenu();

		setVisible(true);
	}

	@Override
	protected void closing() {
		object.template = typeEditor.getText();
	}

	private void init() {
		area.removeAll();

		setLayout(new BorderLayout());
		typeEditor = new JTextArea();
		typeEditor.setText(object.template);
		typeEditor.setFont(new Font("Consolas", Font.BOLD, 22));
		area.add(typeEditor);

		revalidate();
		repaint();
	}

	private void initMenu() {
		JMenuBar typeMenuBar = new JMenuBar();

		{
			JMenu typeMenu = new JMenu("type");

			JMenuItem newStructMenuItem = new JMenuItem("new struct");
			JMenuItem validateMenuItem = new JMenuItem("validate");

			newStructMenuItem.addActionListener(e -> {
				String code = "struct " + object.name + "\n{\n\n};";
				object.template = code;
				init();
			});
			validateMenuItem.addActionListener(e -> {
				String shaderCode = "#version 130\n";
				shaderCode += typeEditor.getText() + '\n';
				shaderCode += "void main()\n";
				shaderCode += "{}\n";
				if (validateShader(shaderCode, JOPAProjectType.FRAGMENT)) {
					gui.showMessage("Type passed validation");
				} else {
					gui.showMessage("Type contains errors");
				}
			});

			typeMenu.add(newStructMenuItem);
			typeMenu.add(validateMenuItem);

			typeMenuBar.add(typeMenu);
		}

		setJMenuBar(typeMenuBar);
	}

}