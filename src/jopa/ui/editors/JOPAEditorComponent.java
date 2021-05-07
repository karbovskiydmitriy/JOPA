package jopa.ui.editors;

import javax.swing.JPanel;

public abstract class JOPAEditorComponent<T> extends JPanel {

	private static final long serialVersionUID = 460052701621725720L;

	protected T object;

	protected JOPAEditorComponent(T object) {
		this.object = object;
	}

	public abstract void writeBack();

}