package jopa.ui;

import java.awt.Button;
import java.awt.Panel;

public class JOPAMenuPanel extends Panel {

	private static final long serialVersionUID = -4645036666787867231L;

	public Button[] buttons;

	public void init() {
		buttons = new Button[10];
		int width = getWidth() / 10;
		width -= 20;
		int height = getHeight() - 20;
		for (int i = 0, offset = 10; i < 10; i++, offset += width + 20) {
			var b = new Button();
			b.setLocation(offset, 10);
			b.setSize(width, height);
			buttons[i] = b;
		}
	}
	
}