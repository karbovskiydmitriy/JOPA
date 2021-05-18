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
import jopa.types.JOPABuffer;
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
			JOPAResource resource = object.resources.get(i);
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
				JOPAImage image = new JOPAImage(0, 100, 100, getTextureFormat(4, float.class, false));
				JOPAResource resource = new JOPAResource(JOPAResourceType.IMAGE, "", image);
				object.resources.add(resource);
				init();
			});
			newBufferMenuBar.addActionListener(l -> {
				JOPABuffer buffer = new JOPABuffer();
				JOPAResource resource = new JOPAResource(JOPAResourceType.BUFFER, "", buffer);
				object.resources.add(resource);
				init();
			});

			resourcesMenu.add(newTextureMenuBar);
			resourcesMenu.add(newBufferMenuBar);

			resourcesMenuBar.add(resourcesMenu);
		}

		setJMenuBar(resourcesMenuBar);
	}

	private void addEditor(String name, JOPAResource resource) {
		JOPAEditorComponent<?> editor = new JOPAResourceEditor(resource);
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