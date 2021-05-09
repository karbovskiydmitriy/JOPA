package jopa.playground;

import static jopa.util.JOPAOGLUtil.createProgram;
import static jopa.util.JOPAOGLUtil.createShader;
import static jopa.util.JOPAOGLUtil.createTexture;
import static jopa.util.JOPAOGLUtil.createWindow;
import static jopa.util.JOPAOGLUtil.deleteTexture;
import static jopa.util.JOPAOGLUtil.destroyWindow;
import static jopa.util.JOPAOGLUtil.getTextureFormat;
import static jopa.util.JOPAOGLUtil.getWindowSize;
import static jopa.util.JOPAOGLUtil.loadComputeShader;
import static jopa.util.JOPAOGLUtil.loadFragmentShader;
import static jopa.util.JOPAOGLUtil.tick;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
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
import jopa.io.JOPALoader;
import jopa.main.JOPAMain;
import jopa.types.JOPAResource;
import jopa.types.JOPAResourceType;

public class JOPASimulationScript implements Serializable {

	private static final long serialVersionUID = -5030460984321077507L;

	private static final String[] NEW_WINDOW = { "new", "window" };
	private static final String[] NEW_TEXTURE = { "new", "texture" };
	private static final String[] NEW_BUFFER = { "new", "buffer" };
	private static final String[] NEW_SHADER = { "new", "shader" };
	private static final String[] NEW_PROGRAM = { "new", "program" };
	private static final String[] GENERATE_SHADER = { "generate", "shader" };
	private static final String[] SET_PROGRAM = { "set", "program" };
	private static final String[] LOAD_TEXTURE = { "load", "texture" };
	private static final String[] SAVE_TEXTURE = { "save", "texture" };
	private static final String[] DO_TICKS = { "do", "ticks" };
	private static final String[] DESTROY_WINDOW = { "destroy", "window" };
	private static final String[] DELETE_TEXTURE = { "destroy", "texture" };
	private static final String[] DELETE_BUFFER = { "destroy", "buffer" };

	private static long window;
	private static int state = 0;
	private static int texture;

	private boolean executed;
	private int commandIndex;

	private ArrayList<String> commands;
	private JOPASimulationType executionType;
	private ArrayList<JOPAResource> resources;

	private final Map<String[], Predicate<String[]>> operations;

	private final Predicate<String[]> NEW_WINDOW_OPERATION = args -> {
		if (args.length != 4) {
			return false;
		}

		String name = args[0];
		boolean isFullscreen;
		int width;
		int height;
		try {
			width = Integer.parseUnsignedInt(args[1]);
			height = Integer.parseUnsignedInt(args[2]);
		} catch (Exception e) {
			return false;
		}
		if (!args[3].equals("true") && !args[3].equals("false")) {
			return false;
		} else {
			isFullscreen = Boolean.parseBoolean(args[3]);
		}

		long windowHandle = createWindow(width, height, isFullscreen, false, resources);

		JOPAResource windowResource = new JOPAResource(JOPAResourceType.WINDOW_HANDLE, name, windowHandle);

		addResource(windowResource);

		return true;
	};

	private final Predicate<String[]> NEW_TEXTURE_OPERATION = args -> {
		if (args.length != 1) {
			return false;
		}

		String name = args[0];
		int[] size = getWindowSize(window);
		int textureHandle = createTexture(size[0], size[1], getTextureFormat(1, Float.class, false), resources);

		JOPAResource textureResource = new JOPAResource(JOPAResourceType.TEXTURE_HANDLE, name, textureHandle);
		addResource(textureResource);

		return true;
	};

	private final Predicate<String[]> NEW_BUFFER_OPERATION = args -> {
		// TODO new buffer

		return true;
	};

	private final Predicate<String[]> NEW_SHADER_OPERATION = args -> {
		if (args.length != 3) {
			return false;
		}

		String name = args[0];
		String type = args[1];
		String file = args[2];
		int shader;
		switch (type) {
		case "fragment":
			shader = loadFragmentShader(file);
			break;
		case "compute":
			shader = loadComputeShader(file);
		default:
			return false;
		}
		if (shader == 0) {
			return false;
		}

		JOPAResource shaderResource = new JOPAResource(JOPAResourceType.SHADER, name, shader);
		addResource(shaderResource);

		return true;
	};

	private final Predicate<String[]> NEW_PROGRAM_OPERATION = args -> {
		if (args.length < 2) {
			return false;
		}

		String programName = args[0];
		String[] shaderNames = Arrays.copyOfRange(args, 1, args.length);
		int[] shaders = new int[shaderNames.length];
		for (int i = 0; i < shaderNames.length; i++) {
			String shaderName = shaderNames[i];
			JOPAResource shaderResource = getResourceByName(shaderName);
			if (shaderResource == null) {
				return false;
			}
			int shader = shaderResource.getAsShader();
			if (shader == 0) {
				return false;
			}
			shaders[i] = shader;
		}
		int program = createProgram(shaders);
		if (program == 0) {
			return false;
		}

		JOPAResource programResource = new JOPAResource(JOPAResourceType.PROGRAM, programName, program);
		addResource(programResource);

		return true;
	};

	private final Predicate<String[]> GENERATE_SHADER_OPERATION = args -> {
		if (args.length != 1) {
			return false;
		}

		String shaderName = args[0];
		String shaderCode = JOPAMain.currentProject.getGeneratedShader();
		if (shaderCode == null) {
			return false;
		}
		int shader;
		switch (JOPAMain.currentProject.projectType) {
		case FRAGMENT:
			shader = createShader(GL_FRAGMENT_SHADER, shaderCode);
			break;
		case COMPUTE:
			shader = createShader(GL_FRAGMENT_SHADER, shaderCode);
			break;
		default:
			return false;
		}

		JOPAResource shaderResource = new JOPAResource(JOPAResourceType.SHADER, shaderName, shader);
		addResource(shaderResource);

		return true;
	};

	private final Predicate<String[]> SET_PROGRAM_OPERATION = args -> {
		if (args.length != 1) {
			return false;
		}

		String programName = args[0];
		JOPAResource programResource = getResourceByName(programName);
		if (programResource == null) {
			return false;
		}
		int program = programResource.getAsProgram();
		if (program == 0) {
			return false;
		}

		glUseProgram(program);

		return true;
	};

	private final Predicate<String[]> LOAD_TEXTURE_OPERATION = args -> {
		if (args.length != 2) {
			return false;
		}
		String name = args[0];
		String fileName = args[1];

		return true;
	};

	private final Predicate<String[]> SAVE_TEXTURE_OPERATION = args -> {
		// TODO saving texture

		return true;
	};

	private final Predicate<String[]> DO_TICKS_PREDICATE = args -> {
		if (args.length != 0) {
			logError("", args);

			return false;
		}

		int windowsCount = 0;

		for (JOPAResource resource : resources) {
			if (resource.type == JOPAResourceType.WINDOW_HANDLE) {
				long windowHandle = resource.getAsWindow();
				if (windowHandle != 0) {
					if (tick(windowHandle, resources)) {
						windowsCount++;
					}
				} else {
					System.err.println("resource " + resource.name + " is not long");

					return false;
				}
			}
		}
		if (windowsCount > 0) {
			commandIndex--;

			return true;
		}

		return false;
	};

	private final Predicate<String[]> DESTROY_WINDOW_PREDICATE = args -> {
		if (args.length != 1) {
			return false;
		}

		String name = args[0];
		JOPAResource resource = getResourceByName(name);
		if (resource.type == JOPAResourceType.WINDOW_HANDLE) {
			long windowHandle = resource.getAsWindow();
			if (windowHandle != 0) {
				destroyWindow(windowHandle);
				resources.remove(resource);

				return true;
			} else {
				System.err.println("resource " + name + " is not long");

				return false;
			}
		}
		System.err.println("window " + name + " not found");

		return false;
	};

	private final Predicate<String[]> DELETE_TEXTURE_PREDICATE = args -> {
		if (args.length != 1) {
			return false;
		}

		String name = args[0];
		JOPAResource textureResource = getResourceByName(name);
		if (textureResource == null) {
			return false;
		}
		int texture = textureResource.getAsTexture();
		if (texture == 0) {
			return false;
		}

		if (!deleteTexture(texture)) {
			return false;
		}

		return true;
	};

	private final Predicate<String[]> DELETE_BUFFER_PREDICATE = args -> {
		if (args.length != 1) {
			return false;
		}

		// TODO destroying buffer

		return true;
	};

	private JOPASimulationScript(JOPASimulationType simulationType) {
		this.executionType = simulationType;
		this.commands = new ArrayList<String>();
		this.resources = new ArrayList<JOPAResource>();
		this.operations = new HashMap<String[], Predicate<String[]>>();
		init();
	}

	private void init() {
		operations.put(NEW_WINDOW, NEW_WINDOW_OPERATION);
		operations.put(NEW_TEXTURE, NEW_TEXTURE_OPERATION);
		operations.put(NEW_BUFFER, NEW_BUFFER_OPERATION);
		operations.put(NEW_SHADER, NEW_SHADER_OPERATION);
		operations.put(NEW_PROGRAM, NEW_PROGRAM_OPERATION);
		operations.put(GENERATE_SHADER, GENERATE_SHADER_OPERATION);
		operations.put(SET_PROGRAM, SET_PROGRAM_OPERATION);
		operations.put(LOAD_TEXTURE, LOAD_TEXTURE_OPERATION);
		operations.put(SAVE_TEXTURE, SAVE_TEXTURE_OPERATION);
		operations.put(DO_TICKS, DO_TICKS_PREDICATE);
		operations.put(DESTROY_WINDOW, DESTROY_WINDOW_PREDICATE);
		operations.put(DELETE_TEXTURE, DELETE_TEXTURE_PREDICATE);
		operations.put(DELETE_BUFFER, DELETE_BUFFER_PREDICATE);
	}

	private void logError(String errorMessage, Object object) {

	}

	public static JOPASimulationScript create(JOPASimulationType simulationType) {
		if (simulationType == null) {
			return null;
		}
		if (simulationType == JOPASimulationType.NONE) {
			return null;
		}

		JOPASimulationScript script = new JOPASimulationScript(simulationType);
		try {
			script.setupScript(JOPALoader.loadStandardScript("test.jopascript"));
		} catch (JOPAPlaygroundException e) {
			System.err.println(e.getMessage());

			return null;
		}

		return script;
	}

	public void setupScript(String code, JOPAResource... resources) throws JOPAPlaygroundException {
		if (executionType != JOPASimulationType.CUSTOM) {
			throw new JOPAPlaygroundException("custom setup is only possible in custom script");
		}
		if (code == null) {
			throw new JOPAPlaygroundException("commands is null");
		}

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
			// System.out.println("Command: " + command);
			if (command.length() > 0) {
				boolean result = executeCommand(command);

				return result;
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
								String[] parts = operationPart.split(" ");
								String[] args;
								if (argsContent.length() > 0) {
									args = argsBuilder.toString().split(",");
								} else {
									args = new String[0];
								}
								for (String s : parts) {
									if (s.length() == 0) {
										return false;
									}
								}
								for (String s : args) {
									if (s.length() == 0) {
										return false;
									}
								}
								executed = false;
								operations.forEach((strings, operationPredicate) -> {
									if (Arrays.equals(strings, parts)) {
										Predicate<String[]> operation = operationPredicate;
										executed = operation.test(args);

										return;
									}
								});
								if (executed) {
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

	private JOPAResource getResourceByName(String name) {
		for (JOPAResource resource : resources) {
			if (resource.name.equals(name)) {
				return resource;
			}
		}

		return null;
	}

	private void addResource(JOPAResource resource) {
		JOPAResource foundResource = getResourceByName(resource.name);
		if (foundResource != null) {
			resources.remove(foundResource);
		}
		resources.add(resource);
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