package ui;

import static app.Main.createNewFunction;
import static app.Main.createNewNode;
import static app.Main.createNewProject;
import static app.Main.editCurrentFunction;
import static app.Main.editProject;
import static app.Main.generateShader;
import static app.Main.showShaderCode;
import static app.Main.startPlayground;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Panel;

import nodes.StatementNode;

public class MenuPanel extends Panel {

	private static final long serialVersionUID = -4645036666787867231L;

	public Button[] buttons;

	public MenuPanel() {
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
			newNodeButton.addActionListener(l -> createNewNode(StatementNode.class));
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