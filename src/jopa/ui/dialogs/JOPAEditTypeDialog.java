package jopa.ui.dialogs;

import java.awt.Frame;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import jopa.types.JOPACustomType;

public class JOPAEditTypeDialog extends JOPADialog<JOPACustomType> {

	private static final long serialVersionUID = -8762774190661954314L;

	public JOPAEditTypeDialog(Frame owner, JOPACustomType type) {
		super(owner, "Edit type", type);

		init();
		initMenu();

		setVisible(true);
	}

	@Override
	protected void closing() {
		// TODO type write-back
	}

	private void init() {
		// TODO create UI for type
	}

	private void initMenu() {
		JMenuBar typeMenuBar = new JMenuBar();

		{
			JMenu typeMenu = new JMenu("type");

			JMenuItem clearTemplateMenuItem = new JMenuItem("clear tmeplate");
			JMenuItem validateMenuItem = new JMenuItem("validate");

			// TODO handlers
			clearTemplateMenuItem.addActionListener(e -> {

			});
			validateMenuItem.addActionListener(e -> {

			});

			typeMenuBar.add(typeMenu);
		}

		setJMenuBar(typeMenuBar);
	}

}