package ui.dialogs;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.SpringLayout.Constraints;

import ui.editors.EditorComponent;

public abstract class Dialog<T> extends JDialog {

	private static final long serialVersionUID = 5368138759243687896L;

	protected static final int CTRL_MODIFIER = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

	protected T object;
	protected JPanel area;
	protected ArrayList<EditorComponent<?>> editors;

	public Dialog(Frame owner, String title, T object) {
		super(owner);
		this.object = object;
		this.editors = new ArrayList<EditorComponent<?>>();

		setTitle(title);
		setSize(500, 500);
		area = new JPanel(new SpringLayout());
		setContentPane(area);

		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				removeWindowListener(this);
				closing();
				super.windowClosed(e);
			}
		});
	}

	protected abstract void closing();

	protected static Constraints getConstraintsForCell(int row, int col, Container parent, int cols) {
		Component c = parent.getComponent(row * cols + col);

		return ((SpringLayout) parent.getLayout()).getConstraints(c);
	}

	protected void adjustGrid(int rows, int cols, int initialX, int initialY, int xPad, int yPad) {
		JComponent parent = area;
		SpringLayout layout;
		try {
			layout = (SpringLayout) parent.getLayout();
		} catch (ClassCastException e) {
			System.err.println("The first argument must use SpringLayout");

			return;
		}

		Spring x = Spring.constant(initialX);
		for (int c = 0; c < cols; c++) {
			Spring width = Spring.constant(0);
			for (int r = 0; r < rows; r++) {
				width = Spring.max(width, getConstraintsForCell(r, c, parent, cols).getWidth());
			}
			for (int r = 0; r < rows; r++) {
				SpringLayout.Constraints constraints = getConstraintsForCell(r, c, parent, cols);
				constraints.setX(x);
				constraints.setWidth(width);
			}
			x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
		}

		Spring y = Spring.constant(initialY);
		for (int r = 0; r < rows; r++) {
			Spring height = Spring.constant(0);
			for (int c = 0; c < cols; c++) {
				height = Spring.max(height, getConstraintsForCell(r, c, parent, cols).getHeight());
			}
			for (int c = 0; c < cols; c++) {
				SpringLayout.Constraints constraints = getConstraintsForCell(r, c, parent, cols);
				constraints.setY(y);
				constraints.setHeight(height);
			}
			y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
		}

		SpringLayout.Constraints pCons = layout.getConstraints(parent);
		pCons.setConstraint(SpringLayout.SOUTH, y);
		pCons.setConstraint(SpringLayout.EAST, x);
	}

}