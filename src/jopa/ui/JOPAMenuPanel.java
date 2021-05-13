package jopa.ui;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Panel;

import jopa.nodes.JOPAStatementNode;

import static jopa.main.JOPAMain.*;

public class JOPAMenuPanel extends Panel {

	private static final long serialVersionUID = -4645036666787867231L;

	public Button[] buttons;

	public JOPAMenuPanel() {
		setLayout(new FlowLayout(FlowLayout.LEFT));
	}

	public void initMenu() {
		{
			Button newFunctionButton = new Button("new function");
			newFunctionButton.addActionListener(e -> createNewFunction());
			add(newFunctionButton);
		}

		{
			Button newNodeButton = new Button("new node");
			newNodeButton.addActionListener(e -> createNewNode(JOPAStatementNode.class));
			add(newNodeButton);
		}

		{
			Button showShaderButton = new Button("show shader");
			showShaderButton.addActionListener(e -> showShaderCode());
			add(showShaderButton);
		}

		{
			Button runSimulationButton = new Button("run simulation");
			runSimulationButton.addActionListener(e -> startPlayground());
			add(runSimulationButton);
		}
	}

}