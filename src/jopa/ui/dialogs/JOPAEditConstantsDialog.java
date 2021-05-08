package jopa.ui.dialogs;

import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import jopa.main.JOPAConstant;
import jopa.main.JOPAProject;
import jopa.types.JOPAGLSLType;
import jopa.ui.editors.JOPAConstantEditor;
import jopa.ui.editors.JOPAEditorComponent;

public class JOPAEditConstantsDialog extends JOPADialog<JOPAProject> {

	private static final long serialVersionUID = -2535204900792827922L;

	public JOPAEditConstantsDialog(Frame owner, JOPAProject project) {
		super(owner, "Constants", project);

		init();
		initMenu();

		setVisible(true);
	}

	@Override
	protected void closing() {
	}

	private void init() {
		editors.clear();
		area.removeAll();

		for (int i = 0; i < object.constants.size(); i++) {
			JOPAConstant constant = object.constants.get(i);
			addEditorPair("constants[" + i + "]", constant);
		}

		revalidate();
		repaint();
	}

	private void initMenu() {
		JMenuBar constantsMenuBar = new JMenuBar();

		{
			JMenu constantsMenu = new JMenu("constants");

			JMenuItem newConstantMenuItem = new JMenuItem("new constant");

			newConstantMenuItem
					.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

			newConstantMenuItem.addActionListener(e -> {
				JOPAConstant newConstant = new JOPAConstant(JOPAGLSLType.JOPA_INT, "foobar", "42");
				object.constants.add(newConstant);
				object.updateConstants();
				init();
			});

			constantsMenu.add(newConstantMenuItem);

			constantsMenuBar.add(constantsMenu);
		}

		setJMenuBar(constantsMenuBar);
	}

	protected void addEditorPair(String name, JOPAConstant constant) {
		JOPAEditorComponent<?> editor = new JOPAConstantEditor(constant);
		JLabel label = new JLabel(name, JLabel.TRAILING);
		label.setLabelFor(editor);
		JButton deleteButton = new JButton("delete");
		deleteButton.addActionListener(e -> {
			object.constants.remove(constant);
			init();
		});
		area.add(label);
		area.add(editor);
		area.add(deleteButton);
		editors.add(editor);
		adjustGrid(area, area.getComponentCount() / 3, 3, 10, 10, 10, 10);
	}

}