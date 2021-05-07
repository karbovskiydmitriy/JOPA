package jopa.ui.editors;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jopa.ports.JOPADataPort;
import jopa.util.JOPATypeUtil;

public class JOPADataPortEditor extends JPanel {

	private static final long serialVersionUID = -1831292319745863889L;

	public JOPADataPortEditor(JOPADataPort port) {
		add(new JLabel(JOPATypeUtil.getNameForType(port.dataType) + " " + port.name));
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

}