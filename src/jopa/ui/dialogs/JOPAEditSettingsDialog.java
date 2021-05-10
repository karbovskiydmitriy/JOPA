package jopa.ui.dialogs;

import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import jopa.main.JOPASettings;

public class JOPAEditSettingsDialog extends JOPADialog<JOPASettings> {

	private static final long serialVersionUID = -8865225363458706703L;

	public JOPAEditSettingsDialog(Frame owner, JOPASettings settings) {
		super(owner, "Settings", settings);

		init();
		initMenu();

		setVisible(true);
	}

	@Override
	protected void closing() {
		object.writeSettings();
	}

	private void init() {

	}

	private void initMenu() {
		JMenuBar settingsMenuBar = new JMenuBar();

		{
			JMenu settingsMenu = new JMenu("settings");

			JMenuItem restoreDefaultsMenuItem = new JMenuItem("restore defaults");

			restoreDefaultsMenuItem
					.setAccelerator(KeyStroke.getKeyStroke('R', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

			restoreDefaultsMenuItem.addActionListener(e -> {
				object.showPortTypes = true;
				object.highlightNodes = true;
				object.writeSettings();
			});

			settingsMenu.add(restoreDefaultsMenuItem);

			settingsMenuBar.add(settingsMenu);
		}

		setJMenuBar(settingsMenuBar);
	}

}