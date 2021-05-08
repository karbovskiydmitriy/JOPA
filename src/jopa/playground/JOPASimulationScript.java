package jopa.playground;

import static jopa.util.JOPAOGLUtil.createProgram;
import static jopa.util.JOPAOGLUtil.createTexture;
import static jopa.util.JOPAOGLUtil.createWindow;
import static jopa.util.JOPAOGLUtil.destroyWindow;
import static jopa.util.JOPAOGLUtil.getTextureFormat;
import static jopa.util.JOPAOGLUtil.loadComputeShader;
import static jopa.util.JOPAOGLUtil.loadFragmentShader;
import static jopa.util.JOPAOGLUtil.tick;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL42.glBindImageTexture;
import static org.lwjgl.opengl.GL43.glDispatchCompute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import jopa.exceptions.JOPAPlaygroundException;
import jopa.types.JOPAResource;

public class JOPASimulationScript implements Serializable {

	private static final long serialVersionUID = -5030460984321077507L;

	private static long window;
	private static int state = 0;
	private static int image;

	private ArrayList<String> commands;
	private Iterator<String> nextCommand;
	private JOPASimulationType executionType;
	private ArrayList<JOPAResource> resources;

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
		reset();
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
			if (!nextCommand.hasNext()) {
				reset();

				return false;
			}

			String command = nextCommand.next();
			if (command.length() > 0) {
				executeCommand(command);
			}

			return true;
		default:
			return false;
		}
	}

	private boolean executeCommand(String command) {
		// TODO commands
		
		return true;
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
		return tick(window, resources);
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
		return tick(window, resources);
	}

	private void defaultComputeShaderDeinit() {
		destroyWindow(window);
	}

}