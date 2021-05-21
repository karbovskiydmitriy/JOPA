package ui.dialogs;

import static util.OGLUtil.getTextureFormat;

import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import app.Project;
import graphics.Image;
import types.Buffer;
import types.Resource;
import types.ResourceType;
import ui.editors.EditorComponent;
import ui.editors.ResourceEditor;

public class EditResourcesDialog extends Dialog<Project> {

	private static final long serialVersionUID = 1945776977252735362L;

	public EditResourcesDialog(Frame owner, Project object) {
		super(owner, "Edit resources", object);

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

		for (int i = 0; i < object.resources.size(); i++) {
			Resource resource = object.resources.get(i);
			addEditor("resources[" + i + "]", resource);
		}
		adjustGrid(area.getComponentCount() / 3, 3, 10, 10, 10, 10);

		revalidate();
		repaint();
	}

	private void initMenu() {
		JMenuBar resourcesMenuBar = new JMenuBar();

		{
			JMenu resourcesMenu = new JMenu("resources");

			JMenuItem newTextureMenuBar = new JMenuItem("new texture");
			JMenuItem newBufferMenuBar = new JMenuItem("new buffer");

			newTextureMenuBar.addActionListener(l -> {
				Image image = new Image(0, 100, 100, getTextureFormat(4, float.class, false));
				Resource resource = new Resource(ResourceType.IMAGE, "", image);
				object.resources.add(resource);
				init();
			});
			newBufferMenuBar.addActionListener(l -> {
				Buffer buffer = new Buffer();
				Resource resource = new Resource(ResourceType.BUFFER, "", buffer);
				object.resources.add(resource);
				init();
			});

			resourcesMenu.add(newTextureMenuBar);
			resourcesMenu.add(newBufferMenuBar);

			resourcesMenuBar.add(resourcesMenu);
		}

		setJMenuBar(resourcesMenuBar);
	}

	private void addEditor(String name, Resource resource) {
		EditorComponent<?> editor = new ResourceEditor(resource);
		JLabel label = new JLabel(name, JLabel.TRAILING);
		label.setLabelFor(editor);
		JButton deleteButton = new JButton("delete");
		deleteButton.addActionListener(l -> {
			object.resources.remove(resource);
			init();
		});
		area.add(label);
		area.add(editor);
		area.add(deleteButton);
		editors.add(editor);
	}

}