package jopa.ui.dialogs;

import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import jopa.main.JOPAProject;
import jopa.main.JOPASymbol;
import jopa.ui.editors.JOPAEditorComponent;
import jopa.ui.editors.JOPASymbolEditor;

public class JOPAEditDefinesDialog extends JOPADialog<JOPAProject> {

	private static final long serialVersionUID = -4171029875775950351L;

	public JOPAEditDefinesDialog(Frame owner, JOPAProject object) {
		super(owner, "Edit defines", object);

		init();
		initMenu();

		setVisible(true);
	}

	@Override
	protected void closing() {
		editors.forEach(editor -> editor.writeBack());
	}

	private void init() {
		editors.clear();
		area.removeAll();

		for (int i = 0; i < object.defines.size(); i++) {
			JOPASymbol symbol = object.defines.get(i);
			addEditorPair("defines[" + i + "]", symbol);
		}

		revalidate();
		repaint();
	}

	private void initMenu() {
		JMenuBar definesMenuBar = new JMenuBar();

		{
			JMenu definesMenu = new JMenu("defines");

			JMenuItem newMenuItem = new JMenuItem("new");

			newMenuItem.setAccelerator(KeyStroke.getKeyStroke('N', CTRL_MODIFIER));

			definesMenu.add(newMenuItem);

			definesMenuBar.add(definesMenu);
		}

		setJMenuBar(definesMenuBar);
	}

	protected void addEditorPair(String name, JOPASymbol symbol) {
		JOPAEditorComponent<?> editor = new JOPASymbolEditor(symbol);
		JLabel label = new JLabel(name, JLabel.TRAILING);
		label.setLabelFor(editor);
		JButton deleteButton = new JButton("delete");
		deleteButton.addActionListener(e -> {
			object.defines.remove(symbol);
			init();
		});
		area.add(label);
		area.add(editor);
		area.add(deleteButton);
		editors.add(editor);
		adjustGrid(area.getComponentCount() / 3, 3, 10, 10, 10, 10);
	}

}