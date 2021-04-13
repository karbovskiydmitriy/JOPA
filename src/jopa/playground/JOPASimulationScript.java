package jopa.playground;

import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL.setCapabilities;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2i;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL43.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import static jopa.io.JOPAIO.*;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import jopa.exceptions.JOPAPlaygroundException;

public class JOPASimulationScript {

	private ArrayList<String> commands;
	private Iterator<String> nextCommand;
	private JOPASimulationType simulationType;

	public JOPASimulationScript(JOPASimulationType simulationType) throws JOPAPlaygroundException {
		if (simulationType == null) {
			throw new JOPAPlaygroundException("simulation type is null");
		}
		if (simulationType == JOPASimulationType.NONE) {
			throw new JOPAPlaygroundException("simulation type is NONE");
		}

		this.simulationType = simulationType;
	}

	public JOPASimulationScript(Collection<String> commands) throws JOPAPlaygroundException {
		if (commands == null) {
			throw new JOPAPlaygroundException("commands is null");
		}

		this.commands = new ArrayList<>(commands);
		this.simulationType = JOPASimulationType.CUSTOM_SIMULATION;
	}

	public void reset() {
		nextCommand = commands.iterator();
	}

	public boolean execute() {
		switch (simulationType) {
		case NONE:
			return false;
		case FRAGMENT_SHADER_SIMULATION:
			executeDefaultFragmentShaderSimulation();

			return true;
		case COMPUTE_SHADER_SIMULATION:
			executeDefaultComputeShaderSimulation();

			return true;
		case CUSTOM_SIMULATION:
			if (nextCommand == null) {
				reset();

				return false;
			}

			String command = nextCommand.next();
			if (command != null && command.length() > 0) {
				if (command.contains(" ")) {
					String[] parts = command.split(" ");
					if (parts.length > 0) {

					}
				} else {

				}

			}

			return true;
		default:
			return false;
		}
	}

	long window;
	int state = 0;

	private boolean executeDefaultFragmentShaderSimulation() {
//		System.out.println("frag");

		if (state == 0) {
			defaultFragmentShaderInit();
			state = 1;

			return true;
		}

		if (state == 1) {
			if (!defaultFragmentShaderTick()) {
				state = 2;
				
				return true;
			}
		}
		
		if (state == 2) {
			defaultFragmentShaderDeinit();
			
			state = 0;
			
			return false;
		}
		
		return false;
	}

	private void defaultFragmentShaderInit() {
		glfwInit();

		window = glfwCreateWindow(1024, 1024, "Default fragment shader showcase", NULL, NULL);
		int[] windowWidht = new int[1];
		int[] windowHeight = new int[1];
		glfwGetWindowSize(window, windowWidht, windowHeight);

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		glfwShowWindow(window);

		createCapabilities();

		glViewport(0, 0, windowWidht[0], windowHeight[0]);

		int defaultProgram = glCreateProgram();
		int defaultFragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(defaultFragmentShader, loadTextFile(".\\shaders\\examples\\fragment.glsl"));
		glCompileShader(defaultFragmentShader);
		System.out.println(glGetShaderInfoLog(defaultFragmentShader));
		glAttachShader(defaultProgram, defaultFragmentShader);
		glLinkProgram(defaultProgram);
		glUseProgram(defaultProgram);
	}

	private boolean defaultFragmentShaderTick() {
		if (!glfwWindowShouldClose(window)) {

			// activate fragment shader program
			// pass parameters

			glClear(GL_COLOR_BUFFER_BIT);
			glBegin(GL_QUADS);
			glTexCoord2f(0, 1);
			glVertex2i(-1, -1);
			glTexCoord2f(0, 0);
			glVertex2i(-1, 1);
			glTexCoord2f(1, 0);
			glVertex2i(1, 1);
			glTexCoord2f(1, 1);
			glVertex2i(1, -1);
			glEnd();

			glfwSwapBuffers(window);
			glfwPollEvents();
			
			return true;
		}
		
		return false;
	}

	private void defaultFragmentShaderDeinit() {
		setCapabilities(null);
		glfwDestroyWindow(window);
		glfwTerminate();
	}

	private void executeDefaultComputeShaderSimulation() {
		// TODO copy from tests
	}

}