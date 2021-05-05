package jopa.playground;

import static jopa.io.JOPALoader.loadStandardShader;
import static jopa.util.JOPAOGLUtil.passUniforms;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
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
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2i;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.GL_RGBA32F;
import static org.lwjgl.opengl.GL42.glBindImageTexture;
import static org.lwjgl.opengl.GL42.glTexStorage2D;
import static org.lwjgl.opengl.GL43.GL_COMPUTE_SHADER;
import static org.lwjgl.opengl.GL43.glDispatchCompute;
import static org.lwjgl.system.MemoryUtil.NULL;

import static jopa.util.JOPAOGLUtil.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.lwjgl.glfw.GLFWVidMode;

import jopa.exceptions.JOPAPlaygroundException;
import jopa.types.JOPAGLSLType;
import jopa.types.JOPAResource;
import jopa.types.JOPAResourceType;

public class JOPASimulationScript {

	private ArrayList<String> commands;
	private Iterator<String> nextCommand;
	private JOPASimulationType executionType;
	private ArrayList<JOPAResource> resources;

	long window;
	int state = 0;
	int image;

	public JOPASimulationScript(JOPASimulationType simulationType) throws JOPAPlaygroundException {
		if (simulationType == null) {
			throw new JOPAPlaygroundException("simulation type is null");
		}
		if (simulationType == JOPASimulationType.NONE) {
			throw new JOPAPlaygroundException("simulation type is NONE");
		}

		this.executionType = simulationType;
		this.commands = new ArrayList<String>();
		this.resources = new ArrayList<JOPAResource>();
	}

	public void setupScript(Collection<String> commands, JOPAResource... resources) throws JOPAPlaygroundException {
		if (executionType != JOPASimulationType.CUSTOM) {
			throw new JOPAPlaygroundException("custom setup is only possible in custom script");
		}
		if (commands == null) {
			throw new JOPAPlaygroundException("commands is null");
		}

		this.commands.clear();
		this.commands.addAll(commands);
		this.resources.clear();
		this.resources.addAll(Arrays.asList(resources));
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
		glfwInit();

		GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		window = glfwCreateWindow(videoMode.width() / 2, videoMode.height() / 2, "Default fragment shader showcase",
				NULL, NULL);
		int[] windowWidht = new int[1];
		int[] windowHeight = new int[1];
		glfwGetWindowSize(window, windowWidht, windowHeight);

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		glfwShowWindow(window);

		createCapabilities();

		glViewport(0, 0, windowWidht[0], windowHeight[0]);

		int defaultProgram = createProgram(loadFragmentShader("fragment.glsl"));
		glUseProgram(defaultProgram);

		JOPAResource screenSize = new JOPAResource();
		screenSize.name = "screenSize";
		screenSize.type = JOPAResourceType.GLSL_TYPE;
		screenSize.glslType = JOPAGLSLType.JOPA_INT_VECTOR_2;
		screenSize.value = new int[] { windowWidht[0], windowHeight[0] };
		this.resources.add(screenSize);
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
		setCapabilities(null);
		glfwDestroyWindow(window);
		glfwTerminate();
	}

	private void defaultComputeShaderInit() {
		glfwInit();

		GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		window = glfwCreateWindow(videoMode.width() / 2, videoMode.height() / 2, "Default compute shader showcase",
				NULL, NULL);
		int[] windowWidht = new int[1];
		int[] windowHeight = new int[1];
		glfwGetWindowSize(window, windowWidht, windowHeight);

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		glfwShowWindow(window);

		createCapabilities();

		glViewport(0, 0, windowWidht[0], windowHeight[0]);
		glEnable(GL_TEXTURE_2D);

		image = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, image);
		glTexStorage2D(GL_TEXTURE_2D, 1, GL_RGBA32F, windowWidht[0], windowHeight[0]);

		int defaultProgram = createProgram(loadComputeShader("compute.glsl"));
		glUseProgram(defaultProgram);
		glBindImageTexture(0, image, 0, false, 0, GL_WRITE_ONLY, GL_RGBA32F);
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
		setCapabilities(null);
		glfwDestroyWindow(window);
		glfwTerminate();
	}

}