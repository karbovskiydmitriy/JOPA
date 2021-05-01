package jopa.playground;

import static jopa.io.JOPAIO.loadTextFile;
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
import static org.lwjgl.opengl.GL11.glGetInteger;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2i;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL20.GL_CURRENT_PROGRAM;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2fv;
import static org.lwjgl.opengl.GL20.glUniform2iv;
import static org.lwjgl.opengl.GL20.glUniform3fv;
import static org.lwjgl.opengl.GL20.glUniform3iv;
import static org.lwjgl.opengl.GL20.glUniform4fv;
import static org.lwjgl.opengl.GL20.glUniform4iv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.GL_RGBA32F;
import static org.lwjgl.opengl.GL30.glUniform1ui;
import static org.lwjgl.opengl.GL40.glUniform1d;
import static org.lwjgl.opengl.GL42.glBindImageTexture;
import static org.lwjgl.opengl.GL42.glTexStorage2D;
import static org.lwjgl.opengl.GL43.GL_COMPUTE_SHADER;
import static org.lwjgl.opengl.GL43.glDispatchCompute;
import static org.lwjgl.system.MemoryUtil.NULL;

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
	private JOPASimulationType simulationType;
	private ArrayList<JOPAResource> resources;

	// temp variables for simulation purposes
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

		this.simulationType = simulationType;
		this.resources = new ArrayList<JOPAResource>();
	}

	public JOPASimulationScript(Collection<String> commands, JOPAResource... resources) throws JOPAPlaygroundException {
		if (commands == null) {
			throw new JOPAPlaygroundException("commands is null");
		}

		this.commands = new ArrayList<String>(commands);
		this.simulationType = JOPASimulationType.CUSTOM_SIMULATION;
		this.resources = new ArrayList<JOPAResource>(Arrays.asList(resources));
	}

	public void reset() {
		nextCommand = commands.iterator();
	}

	private static void passUniforms(Collection<JOPAResource> resources) {
		if (resources != null && resources.size() > 0) {
			int program = glGetInteger(GL_CURRENT_PROGRAM);
			if (program != 0) {
				for (JOPAResource resource : resources) {
					String name = resource.name;
					if (name != null && name.length() > 0) {
						int location = glGetUniformLocation(program, name);
						if (location > -1) {
							passUniformValue(resource, location);
						}
					}
				}
			}
		}
	}

	private static void passUniformValue(JOPAResource resource, int location) {
		if (resource != null && resource.type != null) {
			switch (resource.type) {
			case BUFFER_HANDLE:
			case TEXTURE_HANDLE: {
				int value = safeCast(resource.value, int.class);
				glUniform1i(location, value);
			}
			case GLSL_TYPE:
				if (resource.glslType != null) {
					JOPAGLSLType type = resource.glslType;
					switch (type) {
					case JOPA_BOOL: {
						int value = safeCast(resource.value, boolean.class) ? 1 : 0;
						glUniform1i(location, value);
						break;
					}
					case JOPA_INT: {
						int value = safeCast(resource.value, int.class);
						glUniform1i(location, value);
						break;
					}
					case JOPA_UINT: {
						int value = safeCast(resource.value, int.class);
						glUniform1ui(location, value);
						break;
					}
					case JOPA_FLOAT: {
						float value = safeCast(resource.value, float.class);
						glUniform1f(location, value);
						break;
					}
					case JOPA_DOUBLE: {
						double value = safeCast(resource.value, double.class);
						glUniform1d(location, value);
						break;
					}
					case JOPA_BOOL_VECTOR_2: {
						int[] value = safeCast(resource.value, int[].class);
						glUniform2iv(location, value);
						break;
					}
					case JOPA_BOOL_VECTOR_3: {
						int[] value = safeCast(resource.value, int[].class);
						glUniform3iv(location, value);
						break;
					}
					case JOPA_BOOL_VECTOR_4: {
						int[] value = safeCast(resource.value, int[].class);
						glUniform4iv(location, value);
						break;
					}
					case JOPA_INT_VECTOR_2: {
						int[] value = safeCast(resource.value, int[].class);
						glUniform2iv(location, value);
						break;
					}
					case JOPA_INT_VECTOR_3: {
						int[] value = safeCast(resource.value, int[].class);
						glUniform3iv(location, value);
						break;
					}
					case JOPA_INT_VECTOR_4: {
						int[] value = safeCast(resource.value, int[].class);
						glUniform4iv(location, value);
						break;
					}
					case JOPA_UINT_VECTOR_2: {
						int[] value = safeCast(resource.value, int[].class);
						glUniform2iv(location, value);
						break;
					}
					case JOPA_UINT_VECTOR_3: {
						int[] value = safeCast(resource.value, int[].class);
						glUniform3iv(location, value);
						break;
					}
					case JOPA_UINT_VECTOR_4: {
						int[] value = safeCast(resource.value, int[].class);
						glUniform4iv(location, value);
						break;
					}
					case JOPA_FLOAT_VECTOR_2: {
						float[] value = safeCast(resource.value, float[].class);
						glUniform2fv(location, value);
						break;
					}
					case JOPA_FLOAT_VECTOR_3: {
						float[] value = safeCast(resource.value, float[].class);
						glUniform3fv(location, value);
						break;
					}
					case JOPA_FLOAT_VECTOR_4: {
						float[] value = safeCast(resource.value, float[].class);
						glUniform4fv(location, value);
						break;
					}
					// TODO remaining
					case JOPA_NONE: {
						return;
					}
					default:
						return;
					}
				}
			default:
				return;
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> T safeCast(Object object, Class<T> type) {
		if (object == null) {
			return (T) null;
		} else {
			try {
				return (T) object;
			} catch (Exception e) {
				return (T) null;
			}
		}
	}

	public boolean execute() {
		switch (simulationType) {
		case NONE:
			return false;
		case FRAGMENT_SHADER_SIMULATION:
			return executeDefaultFragmentShaderSimulation();
		case COMPUTE_SHADER_SIMULATION:
			return executeDefaultComputeShaderSimulation();
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

		int defaultProgram = glCreateProgram();
		int defaultFragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(defaultFragmentShader, loadTextFile(".\\shaders\\examples\\fragment.glsl"));
		glCompileShader(defaultFragmentShader);
		// System.out.println(glGetShaderInfoLog(defaultFragmentShader));
		glAttachShader(defaultProgram, defaultFragmentShader);
		glLinkProgram(defaultProgram);
		glUseProgram(defaultProgram);

		JOPAResource screenSize = new JOPAResource();
		// screenSize.id = 1;
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

		int defaultProgram = glCreateProgram();
		int defaultComputeShader = glCreateShader(GL_COMPUTE_SHADER);
		glShaderSource(defaultComputeShader, loadTextFile(".\\shaders\\examples\\compute.glsl"));
		glCompileShader(defaultComputeShader);
		glAttachShader(defaultProgram, defaultComputeShader);
		glLinkProgram(defaultProgram);
		glUseProgram(defaultProgram);
		glBindImageTexture(0, image, 0, false, 0, GL_WRITE_ONLY, GL_RGBA32F);
		glDispatchCompute(windowWidht[0] / 2, windowHeight[0] / 2, defaultComputeShader);
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