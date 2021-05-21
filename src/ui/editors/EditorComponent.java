package ui.editors;

import javax.swing.JPanel;

public abstract class EditorComponent<T> extends JPanel {

	private static final long serialVersionUID = 460052701621725720L;

	protected T object;

	public EditorComponent(T object) {
		this.object = object;
	}

	public abstract void writeBack();

}