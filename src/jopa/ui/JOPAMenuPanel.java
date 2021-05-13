package jopa.ui;

import static jopa.main.JOPAMain.createNewFunction;
import static jopa.main.JOPAMain.createNewNode;
import static jopa.main.JOPAMain.createNewProject;
import static jopa.main.JOPAMain.showShaderCode;
import static jopa.main.JOPAMain.startPlayground;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Panel;

import jopa.nodes.JOPAStatementNode;

public class JOPAMenuPanel extends Panel {

	private static final long serialVersionUID = -4645036666787867231L;

	public Button[] buttons;

	public JOPAMenuPanel() {
		setLayout(new FlowLayout(FlowLayout.LEFT));
	}

	public void initMenu() {
		{
			Button newProjectButton = new Button("new project");
			newProjectButton.addActionListener(e -> createNewProject());
			add(newProjectButton);
		}

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