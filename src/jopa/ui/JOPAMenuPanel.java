package jopa.ui;

import static jopa.main.JOPAMain.createNewFunction;
import static jopa.main.JOPAMain.createNewNode;
import static jopa.main.JOPAMain.createNewProject;
import static jopa.main.JOPAMain.editCurrentFunction;
import static jopa.main.JOPAMain.editProject;
import static jopa.main.JOPAMain.generateShader;
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
		setFocusable(false);
	}

	public void initMenu() {
		{
			Button newProjectButton = new Button("new project");
			newProjectButton.addActionListener(l -> createNewProject());
			newProjectButton.setFocusable(false);
			add(newProjectButton);
		}

		{
			Button editProjectButton = new Button("edit project");
			editProjectButton.addActionListener(l -> editProject());
			editProjectButton.setFocusable(false);
			add(editProjectButton);
		}

		{
			Button newFunctionButton = new Button("new function");
			newFunctionButton.addActionListener(l -> createNewFunction());
			newFunctionButton.setFocusable(false);
			add(newFunctionButton);
		}

		{
			Button editFunctionButton = new Button("edit function");
			editFunctionButton.addActionListener(l -> editCurrentFunction());
			editFunctionButton.setFocusable(false);
			add(editFunctionButton);
		}

		{
			Button newNodeButton = new Button("new node");
			newNodeButton.addActionListener(l -> createNewNode(JOPAStatementNode.class));
			newNodeButton.setFocusable(false);
			add(newNodeButton);
		}

		{
			Button genenerateShader = new Button("generate shader");
			genenerateShader.addActionListener(l -> generateShader());
			genenerateShader.setFocusable(false);
			add(genenerateShader);
		}

		{
			Button showShaderButton = new Button("show shader");
			showShaderButton.addActionListener(l -> showShaderCode());
			showShaderButton.setFocusable(false);
			add(showShaderButton);
		}

		{
			Button runSimulationButton = new Button("run simulation");
			runSimulationButton.addActionListener(l -> startPlayground());
			runSimulationButton.setFocusable(false);
			add(runSimulationButton);
		}
	}

}