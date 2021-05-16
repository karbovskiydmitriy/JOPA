package jopa.ui.dialogs;

import static jopa.util.JOPAOGLUtil.getTextureFormat;

import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import jopa.graphics.JOPAImage;
import jopa.main.JOPAProject;
import jopa.types.JOPAResource;
import jopa.types.JOPAResourceType;
import jopa.ui.editors.JOPAEditorComponent;
import jopa.ui.editors.JOPAResourceEditor;

public class JOPAEditResourcesDialog extends JOPADialog<JOPAProject> {

	private static final long serialVersionUID = 1945776977252735362L;

	public JOPAEditResourcesDialog(Frame owner, JOPAProject object) {
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
			JOPAResource resource = object.resources.get(0);
			addEditor("resources[" + i + "]", resource);
		}
		
		revalidate();
		repaint();
	}

	private void initMenu() {
		JMenuBar resourcesMenuBar = new JMenuBar();

		{
			JMenu resourcesMenu = new JMenu();

			JMenuItem newTextureMenuBar = new JMenuItem("new texture");

			newTextureMenuBar.addActionListener(e -> {
				JOPAImage image = new JOPAImage(0, 100, 100, getTextureFormat(4, float.class, false));
				JOPAResource resource = new JOPAResource(JOPAResourceType.BUFFER_HANDLE, "", image);
				object.resources.add(resource);
			});

			resourcesMenu.add(newTextureMenuBar);

			resourcesMenuBar.add(resourcesMenu);
		}

		setJMenuBar(resourcesMenuBar);
	}

	private void addEditor(String name, JOPAResource resource) {
		JOPAEditorComponent<?> editor = new JOPAResourceEditor(resource);
		JLabel label = new JLabel(name, JLabel.TRAILING);
		label.setLabelFor(editor);
		JButton deleteButton = new JButton("delete");
		deleteButton.addActionListener(e -> {
			object.resources.remove(resource);
			init();
		});
		area.add(label);
		area.add(editor);
		area.add(deleteButton);
		editors.add(editor);
	}

}