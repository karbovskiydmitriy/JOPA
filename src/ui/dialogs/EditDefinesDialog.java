package ui.dialogs;

import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import app.Project;
import app.Symbol;
import ui.editors.EditorComponent;
import ui.editors.SymbolEditor;

public class EditDefinesDialog extends Dialog<Project> {

	private static final long serialVersionUID = -4171029875775950351L;

	public EditDefinesDialog(Frame owner, Project object) {
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
			Symbol symbol = object.defines.get(i);
			addEditor("defines[" + i + "]", symbol);
		}
		adjustGrid(area.getComponentCount() / 3, 3, 10, 10, 10, 10);

		revalidate();
		repaint();
	}

	private void initMenu() {
		JMenuBar definesMenuBar = new JMenuBar();

		{
			JMenu definesMenu = new JMenu("defines");

			JMenuItem newMenuItem = new JMenuItem("new");
			JMenuItem clearMenuItem = new JMenuItem("clear");

			newMenuItem.setAccelerator(KeyStroke.getKeyStroke('N', CTRL_MODIFIER));

			newMenuItem.addActionListener(l -> {
				Symbol preprocessorSymbol = new Symbol("name", "value");
				object.defines.add(preprocessorSymbol);
				init();
			});
			clearMenuItem.addActionListener(l -> {
				object.defines.clear();
				init();
			});

			definesMenu.add(newMenuItem);
			definesMenu.add(clearMenuItem);

			definesMenuBar.add(definesMenu);
		}

		setJMenuBar(definesMenuBar);
	}

	private void addEditor(String name, Symbol symbol) {
		EditorComponent<?> editor = new SymbolEditor(symbol);
		JLabel label = new JLabel(name, JLabel.TRAILING);
		label.setLabelFor(editor);
		JButton deleteButton = new JButton("delete");
		deleteButton.addActionListener(l -> {
			object.defines.remove(symbol);
			init();
		});
		area.add(label);
		area.add(editor);
		area.add(deleteButton);
		editors.add(editor);
	}

}