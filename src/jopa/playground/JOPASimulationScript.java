package jopa.playground;

import static jopa.util.JOPAOGLUtil.createProgram;
import static jopa.util.JOPAOGLUtil.createTexture;
import static jopa.util.JOPAOGLUtil.createWindow;
import static jopa.util.JOPAOGLUtil.destroyWindow;
import static jopa.util.JOPAOGLUtil.getTextureFormat;
import static jopa.util.JOPAOGLUtil.loadComputeShader;
import static jopa.util.JOPAOGLUtil.loadFragmentShader;
import static jopa.util.JOPAOGLUtil.passUniforms;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2i;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL42.glBindImageTexture;
import static org.lwjgl.opengl.GL43.glDispatchCompute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import jopa.exceptions.JOPAPlaygroundException;
import jopa.types.JOPAResource;

public class JOPASimulationScript {

	private ArrayList<String> commands;
	private Iterator<String> nextCommand;
	private JOPASimulationType executionType;
	private ArrayList<JOPAResource> resources;

	long window;
	int state = 0;
	int image;

	private JOPASimulationScript(JOPASimulationType simulationType) {
		this.executionType = simulationType;
		this.commands = new ArrayList<String>();
		this.resources = new ArrayList<JOPAResource>();
	}

	public static JOPASimulationScript create(JOPASimulationType simulationType) {
		if (simulationType == null) {
			return null;
		}
		if (simulationType == JOPASimulationType.NONE) {
			return null;
		}

		return new JOPASimulationScript(simulationType);
	}

	public void setupScript(String code, JOPAResource... resources) throws JOPAPlaygroundException {
		if (executionType != JOPASimulationType.CUSTOM) {
			throw new JOPAPlaygroundException("custom setup is only possible in custom script");
		}
		// if (code == null) {
		// throw new JOPAPlaygroundException("commands is null");
		// }

		setCode(code);
		this.resources.clear();
		this.resources.addAll(Arrays.asList(resources));
	}

	public String getCode() {
		String code = "";

		for (String command : commands) {
			code += command + '\n';
		}

		return code;
	}

	public void setCode(String code) {
		if (commands != null) {
			commands.clear();
		}
		commands = new ArrayList<String>(Arrays.asList(code.split("\n")));
	}

	public void reset() {
		nextCommand = commands.iterator();
	}

	public boolean execute() {
		switch (executionType) {
		case NONE:
			return false;
		case FRAGMENT:
			return executeDefaultFragmentShaderSimulation();
		case COMPUTE:
			return executeDefaultComputeShaderSimulation();
		case CUSTOM:
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

	private boolean executeDefaultFragmentShaderSimulation() {
		switch (state) {
		case 0:
			defaultFragmentShaderInit();
			state = 1;

			return true;
		case 1:
			if (!defaultFragmentShaderTick()) {
				state = 2;
			}

			return true;
		case 2:
			defaultFragmentShaderDeinit();
			state = 0;

			return false;
		}

		return false;
	}

	private boolean executeDefaultComputeShaderSimulation() {
		switch (state) {
		case 0:
			defaultComputeShaderInit();
			state = 1;

			return true;
		case 1:
			if (!defaultComputeShaderTick()) {
				state = 2;
			}

			return true;
		case 2:
			defaultComputeShaderDeinit();
			state = 0;

			return false;
		}

		return false;
	}

	private void defaultFragmentShaderInit() {
		window = createWindow(500, 500, false, false, resources);
		glUseProgram(createProgram(loadFragmentShader("fragment.glsl")));
	}

	private boolean defaultFragmentShaderTick() {
		if (!glfwWindowShouldClose(window)) {
			passUniforms(resources);

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
		destroyWindow(window);
	}

	private void defaultComputeShaderInit() {
		window = createWindow(500, 500, false, false, resources);

		int[] windowWidht = new int[1];
		int[] windowHeight = new int[1];
		glfwGetWindowSize(window, windowWidht, windowHeight);
		glEnable(GL_TEXTURE_2D);

		int format = getTextureFormat(4, float.class, false);
		image = createTexture(windowWidht[0], windowHeight[0], format);

		int defaultProgram = createProgram(loadComputeShader("compute.glsl"));
		glUseProgram(defaultProgram);
		glBindImageTexture(0, image, 0, false, 0, GL_WRITE_ONLY, format);
		glDispatchCompute(windowWidht[0] / 2, windowHeight[0] / 2, 1);
	}

	private boolean defaultComputeShaderTick() {
		if (!glfwWindowShouldClose(window)) {
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

	private void defaultComputeShaderDeinit() {
		destroyWindow(window);
	}

}