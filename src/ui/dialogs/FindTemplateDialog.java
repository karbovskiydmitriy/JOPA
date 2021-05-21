package ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.List;
import java.awt.TextArea;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import app.Template;

public class FindTemplateDialog extends Dialog<ArrayList<Template>> {

	private static final long serialVersionUID = 1931720279362522665L;

	private TextArea templateNameTextArea;
	private List templatesList;

	public Template selectedTemplate;

	public FindTemplateDialog(Frame owner, ArrayList<Template> templates) {
		super(owner, "Select template", templates);

		init();

		setVisible(true);
	}

	@Override
	protected void closing() {
		int index = templatesList.getSelectedIndex();
		if (templatesList.getSelectedItem() == "null") {
			selectedTemplate = null;
		} else {
			if (index != -1) {
				selectedTemplate = object.get(index);
			} else {
				selectedTemplate = null;
			}
		}
	}

	private void init() {
		area.setLayout(new BorderLayout());
		templateNameTextArea = new TextArea();
		templatesList = new List(object.size());
		updateList();
		templateNameTextArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				super.keyReleased(e);
				updateList();
			}
		});
		templatesList.addItemListener(l -> {
			setVisible(false);
		});
		getContentPane().add(templateNameTextArea, BorderLayout.NORTH);
		getContentPane().add(templatesList, BorderLayout.CENTER);
	}

	private void updateList() {
		templatesList.removeAll();
		String text = templateNameTextArea.getText().toLowerCase();
		for (Template template : object) {
			if (template.type.equals("statement")) {
				if (text != "") {
					if (template.name.toLowerCase().contains(text)) {
						templatesList.add(template.name);
					}
				} else {
					templatesList.add(template.name);
				}
			}
		}
		templatesList.add("null");
	}

}