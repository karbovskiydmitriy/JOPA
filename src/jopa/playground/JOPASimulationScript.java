package jopa.playground;

import static jopa.util.JOPAOGLUtil.createProgram;
import static jopa.util.JOPAOGLUtil.createTexture;
import static jopa.util.JOPAOGLUtil.createWindow;
import static jopa.util.JOPAOGLUtil.destroyWindow;
import static jopa.util.JOPAOGLUtil.getTextureFormat;
import static jopa.util.JOPAOGLUtil.getWindowSize;
import static jopa.util.JOPAOGLUtil.loadComputeShader;
import static jopa.util.JOPAOGLUtil.loadFragmentShader;
import static jopa.util.JOPAOGLUtil.tick;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL42.glBindImageTexture;
import static org.lwjgl.opengl.GL43.glDispatchCompute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import jopa.exceptions.JOPAPlaygroundException;
import jopa.types.JOPAResource;

public class JOPASimulationScript implements Serializable {

	private static final long serialVersionUID = -5030460984321077507L;

	private static final String[] NEW_WINDOW = { "new", "window" };
	private static final String[] NEW_TEXTURE = { "new", "texture" };
	private static final String[] NEW_BUFFER = { "new", "buffer" };
	private static final String[] DO_TICKS = { "do", "ticks" };
	private static final String[] DESTROY_WINDOW = { "destroy", "window" };
	private static final String[] DESTROY_TEXTURE = { "destroy", "texture" };
	private static final String[] DESTROY_BUFFER = { "destroy", "buffer" };

	private static long window;
	private static int state = 0;
	private static int texture;

	private boolean executed;
	private int commandIndex;

	private ArrayList<String> commands;
	private JOPASimulationType executionType;
	private ArrayList<JOPAResource> resources;

	private final Map<String[], Predicate<String[]>> operations;

	private Predicate<String[]> NEW_WINDOW_OPERATION = args -> {
		window = createWindow(500, 500, false, false, resources);

		return true;
	};

	private Predicate<String[]> NEW_TEXTURE_OPERATION = args -> {
		int[] windowSize = getWindowSize(window);

		texture = createTexture(windowSize[0], windowSize[1], getTextureFormat(1, Float.class, false), resources);

		return true;
	};

	private Predicate<String[]> NEW_BUFFER_OPERATION = args -> {
		return true;
	};

	private Predicate<String[]> DO_TICKS_PREDICATE = args -> {
		if (tick(window, resources)) {
			commandIndex--;

			return true;
		}

		return false;
	};

	private Predicate<String[]> DESTROY_WINDOW_PREDICATE = args -> {
		destroyWindow(window);

		return true;
	};

	private Predicate<String[]> DESTROY_TEXTURE_PREDICATE = args -> {
		return true;
	};

	private Predicate<String[]> DESTROY_BUFFER_PREDICATE = args -> {
		return true;
	};

	private JOPASimulationScript(JOPASimulationType simulationType) {
		this.executionType = simulationType;
		this.commands = new ArrayList<String>();
		this.resources = new ArrayList<JOPAResource>();

		this.operations = new HashMap<String[], Predicate<String[]>>();
		this.operations.put(NEW_WINDOW, NEW_WINDOW_OPERATION);
		this.operations.put(NEW_TEXTURE, NEW_TEXTURE_OPERATION);
		this.operations.put(NEW_BUFFER, NEW_BUFFER_OPERATION);
		this.operations.put(DO_TICKS, DO_TICKS_PREDICATE);
		this.operations.put(DESTROY_WINDOW, DESTROY_WINDOW_PREDICATE);
		this.operations.put(DESTROY_TEXTURE, DESTROY_TEXTURE_PREDICATE);
		this.operations.put(DESTROY_BUFFER, DESTROY_BUFFER_PREDICATE);
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
		commandIndex = 0;
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
			if (commandIndex == commands.size()) {
				reset();

				return false;
			}

			String command = commands.get(commandIndex++);
			if (command.length() > 0) {
				System.out.println(command);
				return executeCommand(command);
			}

			return true;
		default:
			return false;
		}
	}

	private boolean executeCommand(String command) {
		boolean containsSpaces = command.contains(" ");
		boolean containsBraces = command.contains("(") || command.contains(")");
		boolean isPure = !containsSpaces && !containsBraces;

		if (isPure) {
			switch (command.toLowerCase()) {
			case "exit":
			case "quit":
			case "end":
			default:
				return false;
			}
		} else {
			if (containsBraces) {
				if (command.contains("(") && command.contains(")")) {
					if (command.indexOf('(') == command.lastIndexOf('(')) {
						if (command.indexOf(')') == command.lastIndexOf(')')) {
							int indexLeft = command.indexOf('(');
							int indexRight = command.indexOf(')');
							if (indexLeft < indexRight) {
								String operationPart = command.substring(0, indexLeft);
								String argsContent = command.substring(indexLeft + 1, indexRight);
								StringBuilder argsBuilder = new StringBuilder(argsContent);
								int index = 0;
								while (true) {
									index = argsBuilder.indexOf(" ");
									if (index == -1) {
										break;
									}
									argsBuilder.deleteCharAt(index);
								}
								String[] args = argsBuilder.toString().split(",");
								String[] parts = operationPart.split(" ");
								// System.out.println(Arrays.toString(parts));
								// System.out.println(Arrays.toString(args));
								executed = false;
								operations.forEach((strings, operationPredicate) -> {
									if (Arrays.equals(strings, parts)) {
										Predicate<String[]> operation = operationPredicate;
										executed = operation.test(args);

										return;
									}
								});
								if (!executed) {
									System.out.println("Could not find operation: " + operationPart);
								} else {
									return true;
								}
							}
						}
					}
				}
			}
		}

		return false;
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

		int[] windowSize = getWindowSize(window);

		int format = getTextureFormat(4, float.class, false);
		texture = createTexture(windowSize[0], windowSize[1], format, resources);

		int defaultProgram = createProgram(loadComputeShader("compute.glsl"));
		glUseProgram(defaultProgram);
		glBindImageTexture(0, texture, 0, false, 0, GL_WRITE_ONLY, format);
		glDispatchCompute(windowSize[0] / 2, windowSize[1] / 2, 1);
	}

	private boolean defaultComputeShaderTick() {
		return tick(window, resources);
	}

	private void defaultComputeShaderDeinit() {
		destroyWindow(window);
	}

}