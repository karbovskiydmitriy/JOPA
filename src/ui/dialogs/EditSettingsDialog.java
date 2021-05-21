package ui.dialogs;

import java.awt.Frame;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import app.Settings;

public class EditSettingsDialog extends Dialog<Settings> {

	private static final long serialVersionUID = -8865225363458706703L;

	public EditSettingsDialog(Frame owner, Settings settings) {
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

			restoreDefaultsMenuItem.setAccelerator(KeyStroke.getKeyStroke('R', CTRL_MODIFIER));

			restoreDefaultsMenuItem.addActionListener(l -> {
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