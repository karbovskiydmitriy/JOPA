package jopa.playground;

import static jopa.io.JOPALoader.loadStandardScript;
import static jopa.util.JOPATypeUtil.*;
import static jopa.util.JOPAOGLUtil.createBuffer;
import static jopa.util.JOPAOGLUtil.createProgram;
import static jopa.util.JOPAOGLUtil.createShader;
import static jopa.util.JOPAOGLUtil.createTexture;
import static jopa.util.JOPAOGLUtil.createWindow;
import static jopa.util.JOPAOGLUtil.deleteBuffer;
import static jopa.util.JOPAOGLUtil.deleteProgram;
import static jopa.util.JOPAOGLUtil.deleteShader;
import static jopa.util.JOPAOGLUtil.deleteTexture;
import static jopa.util.JOPAOGLUtil.destroyWindow;
import static jopa.util.JOPAOGLUtil.getTextureFormat;
import static jopa.util.JOPAOGLUtil.getWindowSize;
import static jopa.util.JOPAOGLUtil.loadComputeShader;
import static jopa.util.JOPAOGLUtil.loadFragmentShader;
import static jopa.util.JOPAOGLUtil.loadTexture;
import static jopa.util.JOPAOGLUtil.tick;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL42.glBindImageTexture;
import static org.lwjgl.opengl.GL43.glDispatchCompute;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

import jopa.exceptions.JOPAPlaygroundException;
import jopa.graphics.JOPAImage;
import jopa.main.JOPAMain;
import jopa.types.JOPAGLSLType;
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
	private static final String[] DO_TICKS = { "do", "ticks" };
	private static final String[] DESTROY_WINDOW = { "destroy", "window" };
	private static final String[] DELETE_TEXTURE = { "destroy", "texture" };
	private static final String[] DELETE_BUFFER = { "destroy", "buffer" };
	private static final String[] DELETE_SHADER = { "delete", "shader" };
	private static final String[] DELETE_PROGRAM = { "delete", "program" };

	private static long window;
	private static int state = 0;
	private static JOPAImage singleImage;

	private boolean executed;
	private int commandIndex;

	private long startTime;
	private long prevTime;

	private String plainCode;
	private ArrayList<String> commands;
	private JOPASimulationType executionType;
	private ArrayList<JOPAResource> resources;

	private final Map<String[], Predicate<String[]>> operations;

	private final Predicate<String[]> NEW_WINDOW_OPERATION = args -> {
		if (args.length != 4) {
			logSimulationError(this, "NEW WINDOW uses 4 arguments! (name, width, height, fullscreen)", args);

			return false;
		}

		String name = args[0];
		boolean isFullscreen;
		int width;
		int height;
		try {
			width = Integer.parseUnsignedInt(args[1]);
		} catch (NumberFormatException e) {
			logSimulationError(this, "\"width\" should be unsigned integer", args[1]);

			return false;
		}
		try {
			height = Integer.parseUnsignedInt(args[2]);
		} catch (NumberFormatException e) {
			logSimulationError(this, "\"height\" should be unsigned integer", args[2]);

			return false;
		}
		if (!args[3].equals("true") && !args[3].equals("false")) {
			logSimulationError(this, "\"fullscreen\" should be either \"true\" or \"false\"", args[3]);

			return false;
		} else {
			isFullscreen = Boolean.parseBoolean(args[3]);
		}

		long windowHandle = createWindow(width, height, isFullscreen, this);

		JOPAResource windowResource = new JOPAResource(JOPAResourceType.WINDOW_HANDLE, name, windowHandle);

		addResource(windowResource);

		return true;
	};

	private final Predicate<String[]> NEW_TEXTURE_OPERATION = args -> {
		if (args.length != 1) {
			logSimulationError(this, "CREATE WINDOW uses 1 argument (window name)", args);

			return false;
		}

		String name = args[0];
		int[] size = getWindowSize(window);
		if (size == null) {
			logSimulationError(this, "Could not find window", window);
		}
		JOPAImage image = createTexture(size[0], size[1], getTextureFormat(1, Float.class, false));
		if (image == null) {
			logSimulationError(this, "Texture was not created", name);

			return false;
		}

		JOPAResource textureResource = new JOPAResource(JOPAResourceType.IMAGE, name, image);
		addResource(textureResource);

		return true;
	};

	private final Predicate<String[]> NEW_BUFFER_OPERATION = args -> {
		if (args.length != 3) {
			logSimulationError(this, "NEW BUFFER uses 3 arguments (name, type name, count)", args);

			return false;
		}

		String name = args[0];
		String typeName = args[1];
		String countString = args[2];
		int typeSize = getTypeSize(typeName);
		if (typeSize == 0) {
			logSimulationError(this, "Unknown type", typeName);

			return false;
		}
		int count;
		try {
			count = Integer.parseUnsignedInt(countString);
		} catch (NumberFormatException e) {
			logSimulationError(this, "\"count\" should be unsigned integer", countString);

			return false;
		}
		if (count == 0) {
			logSimulationError(this, "\"count\" can not be 0", countString);

			return false;
		}
		int size = typeSize * count;
		int buffer = createBuffer(ByteBuffer.allocate(size));
		if (buffer == 0) {
			logSimulationError(this, "Buffer was not created1", name);

			return false;
		}

		JOPAResource bufferResource = new JOPAResource(JOPAResourceType.BUFFER_HANDLE, name, buffer);
		addResource(bufferResource);

		return true;
	};

	private final Predicate<String[]> NEW_SHADER_OPERATION = args -> {
		if (args.length != 3) {
			logSimulationError(this, "NEW SHADER uses 3 arguments (name, type, file)", args);

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
			logSimulationError(this, "Shader type should be either \"fragment\" or \"compute\"", type);

			return false;
		}
		if (shader == 0) {
			logSimulationError(this, "Could not load shader file", file);

			return false;
		}

		JOPAResource shaderResource = new JOPAResource(JOPAResourceType.SHADER, name, shader);
		addResource(shaderResource);

		return true;
	};

	private final Predicate<String[]> NEW_PROGRAM_OPERATION = args -> {
		if (args.length < 2) {
			logSimulationError(this, "NEW PROGRAM uses 2+ arguments (program, shader0, shader1, ...)", args);

			return false;
		}

		String programName = args[0];
		String[] shaderNames = Arrays.copyOfRange(args, 1, args.length);
		int[] shaders = new int[shaderNames.length];
		for (int i = 0; i < shaderNames.length; i++) {
			String shaderName = shaderNames[i];
			JOPAResource shaderResource = getResourceByName(shaderName);
			if (shaderResource == null) {
				logSimulationError(this, "Shader could not be found", shaderName);

				return false;
			}
			int shader = shaderResource.getAsShader();
			if (shader == 0) {
				logSimulationError(this, "Shader variable is NULL", shaderResource);

				return false;
			}
			shaders[i] = shader;
		}
		int program = createProgram(shaders);
		if (program == 0) {
			logSimulationError(this, "Program could not be linked", programName);

			return false;
		}

		JOPAResource programResource = new JOPAResource(JOPAResourceType.PROGRAM, programName, program);
		addResource(programResource);

		return true;
	};

	private final Predicate<String[]> GENERATE_SHADER_OPERATION = args -> {
		if (args.length != 1) {
			logSimulationError(this, "GENERATE SHADER uses 1 argument (name)", args);

			return false;
		}

		String shaderName = args[0];
		String shaderCode = JOPAMain.currentProject.getGeneratedShader();
		if (shaderCode == null) {
			logSimulationError(this, "Shader code could not be generated", shaderName);

			return false;
		}
		int shader;
		if (JOPAMain.currentProject == null) {
			logSimulationError(this, "Project is not loaded", null);

			return false;
		}
		switch (JOPAMain.currentProject.projectType) {
		case FRAGMENT:
			shader = createShader(GL_FRAGMENT_SHADER, shaderCode);
			break;
		case COMPUTE:
			shader = createShader(GL_FRAGMENT_SHADER, shaderCode);
			break;
		default:
			logSimulationError(this, "Shader type is not set up in the project", JOPAMain.currentProject);

			return false;
		}
		if (shader == 0) {
			logSimulationError(this, "Shader could not be compiled", shaderCode);

			return false;
		}

		JOPAResource shaderResource = new JOPAResource(JOPAResourceType.SHADER, shaderName, shader);
		addResource(shaderResource);

		return true;
	};

	private final Predicate<String[]> SET_PROGRAM_OPERATION = args -> {
		if (args.length != 1) {
			logSimulationError(this, "SET PROGRAM uses 1 argument (name)", args);

			return false;
		}

		String programName = args[0];
		int program;
		if (!programName.equals("0")) {
			JOPAResource programResource = getResourceByName(programName);
			if (programResource == null) {
				logSimulationError(this, "Program could not be found", programName);

				return false;
			}
			program = programResource.getAsProgram();
			if (program == 0) {
				logSimulationError(this, "Program variable is NULL", programResource);

				return false;
			}
		} else {
			program = 0;
		}

		glUseProgram(program);

		return true;
	};

	private final Predicate<String[]> LOAD_TEXTURE_OPERATION = args -> {
		if (args.length != 2) {
			logSimulationError(this, "LOAD TEXTURE uses 2 arguments (name, file name)", args);

			return false;
		}
		String name = args[0];
		String fileName = args[1];

		JOPAImage image = loadTexture(fileName);
		if (image == null) {
			logSimulationError(this, "Could not load texture from file", fileName);

			return false;
		}

		JOPAResource imageResource = new JOPAResource(JOPAResourceType.IMAGE, name, image);
		addResource(imageResource);

		return true;
	};

	private final Predicate<String[]> DO_TICKS_PREDICATE = args -> {
		if (args.length != 0) {
			logSimulationError(this, "DO TICKS uses 0 arguments", args);

			return false;
		}

		long currentTime = System.currentTimeMillis() - startTime;
		float deltaTime = (currentTime - prevTime) / 1000.0f;
		JOPAResource timeResource = getResourceByName("time");
		if (timeResource == null) {
			logSimulationError(this, "Could not find resource \"time\"", resources);

			return false;
		}
		JOPAResource deltaTimeResource = getResourceByName("deltaTime");
		if (deltaTimeResource == null) {
			logSimulationError(this, "Could not find resource \"deltaTime\"", resources);

			return false;
		}
		timeResource.setValue(currentTime / 1000.0f);
		deltaTimeResource.setValue(deltaTime);

		int windowsCount = 0;

		for (JOPAResource resource : resources) {
			if (resource.type == JOPAResourceType.WINDOW_HANDLE) {
				long windowHandle = resource.getAsWindow();
				if (windowHandle == 0) {
					logSimulationError(this, "Window variable is NULL", resource);

					return false;
				} else {
					windowsCount++;
					if (!tick(windowHandle, this)) {
						return false;
					}
				}
			}
		}
		if (windowsCount == 0) {
			logSimulationError(this, "Could not find any window resource", resources);

			return false;
		}
		commandIndex--;
		prevTime = currentTime;

		return true;
	};

	private final Predicate<String[]> DESTROY_WINDOW_PREDICATE = args -> {
		if (args.length != 1) {
			logSimulationError(this, "DESTROY WINDOW uses 1 argument (window)", args);

			return false;
		}

		String name = args[0];
		JOPAResource resource = getResourceByName(name);
		if (resource.type != JOPAResourceType.WINDOW_HANDLE) {
			logSimulationError(this, "Window was not found", name);

			return false;
		}
		long windowHandle = resource.getAsWindow();
		if (windowHandle == 0) {
			logSimulationError(this, "Window resource is NULL", resource);

			return false;
		}
		destroyWindow(windowHandle);
		resources.remove(resource);

		return true;
	};

	private final Predicate<String[]> DELETE_TEXTURE_PREDICATE = args -> {
		if (args.length != 1) {
			logSimulationError(this, "DELETE TEXTURE uses 1 argument (texture)", args);

			return false;
		}

		String name = args[0];
		JOPAResource textureResource = getResourceByName(name);
		if (textureResource == null) {
			logSimulationError(this, "Texture resource was not found", textureResource);

			return false;
		}
		JOPAImage image = textureResource.getAsImage();
		if (image == null) {
			logSimulationError(this, "Image resource is NULL", textureResource);

			return false;
		}
		if (!deleteTexture(image)) {
			logSimulationError(this, "Texture was not deleted", image);

			return false;
		}

		return true;
	};

	private final Predicate<String[]> DELETE_BUFFER_PREDICATE = args -> {
		if (args.length != 1) {
			logSimulationError(this, "DELETE BUFFER uses 1 argument (name)", args);

			return false;
		}

		String bufferName = args[0];
		JOPAResource bufferResource = getResourceByName(bufferName);
		if (bufferResource == null) {
			logSimulationError(this, "Buffer resource was not found", bufferResource);

			return false;
		}
		int buffer = bufferResource.getAsBuffer();
		if (buffer == 0) {
			logSimulationError(this, "Buffer variable is NULL", bufferResource);

			return false;
		}
		if (!deleteBuffer(buffer)) {
			logSimulationError(this, "Buffer variable was not deleted", bufferName);

			return false;
		}

		return true;
	};

	private final Predicate<String[]> DELETE_SHADER_OPERATION = args -> {
		if (args.length != 1) {
			logSimulationError(this, "DELETE SHADER uses 1 argument (name)", args);

			return false;
		}

		String shaderName = args[0];
		JOPAResource shaderResource = getResourceByName(shaderName);
		if (shaderResource == null) {
			logSimulationError(this, "Shader resource was not found", shaderName);

			return false;
		}
		int shader = shaderResource.getAsShader();
		if (shader == 0) {
			logSimulationError(this, "Shader variable is NULL", shaderResource);

			return false;
		}
		if (!deleteShader(shader)) {
			logSimulationError(this, "Shader was not deleted", shaderName);

			return false;
		}

		return true;
	};

	private final Predicate<String[]> DELETE_PROGRAM_OPERATION = args -> {
		if (args.length != 1) {
			logSimulationError(this, "DELETE PROGRAM uses 1 argument (name)", args);

			return false;
		}

		String programName = args[0];
		JOPAResource programResource = getResourceByName(programName);
		if (programResource == null) {
			logSimulationError(this, "Program resource was not found", programName);

			return false;
		}
		int program = programResource.getAsProgram();
		if (program == 0) {
			logSimulationError(this, "Program variable is NULL", programResource);

			return false;
		}
		if (!deleteProgram(program)) {
			logSimulationError(this, "Program was not deleted", programName);

			return false;
		}

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
		operations.put(DO_TICKS, DO_TICKS_PREDICATE);
		operations.put(DESTROY_WINDOW, DESTROY_WINDOW_PREDICATE);
		operations.put(DELETE_TEXTURE, DELETE_TEXTURE_PREDICATE);
		operations.put(DELETE_BUFFER, DELETE_BUFFER_PREDICATE);
		operations.put(DELETE_SHADER, DELETE_SHADER_OPERATION);
		operations.put(DELETE_PROGRAM, DELETE_PROGRAM_OPERATION);

		JOPAResource timeResource = new JOPAResource(JOPAGLSLType.JOPA_FLOAT, "time", 0.0f);
		addResource(timeResource);
		JOPAResource deltaTimeResource = new JOPAResource(JOPAGLSLType.JOPA_FLOAT, "deltaTime", 0.0f);
		addResource(deltaTimeResource);
	}

	public void start() {
		startTime = System.currentTimeMillis();
		commandIndex = 0;
	}

	private void logSimulationError(Object source, String errorMessage, Object object) {
		System.err.println(source.getClass().getSimpleName() + ": " + errorMessage + " " + object);
	}

	public static JOPASimulationScript create(JOPASimulationType simulationType) {
		if (simulationType == null) {
			return null;
		}
		if (simulationType == JOPASimulationType.NONE) {
			return null;
		}

		JOPASimulationScript script = new JOPASimulationScript(simulationType);
		if (simulationType == JOPASimulationType.CUSTOM) {
			try {
				script.setupScript(loadStandardScript("test.jopascript"));
			} catch (JOPAPlaygroundException e) {
				System.err.println(e.getMessage());

				return null;
			}
		}

		return script;
	}

	public void setupScript(String code) throws JOPAPlaygroundException {
		if (executionType != JOPASimulationType.CUSTOM) {
			throw new JOPAPlaygroundException("custom setup is only possible in custom script");
		}
		if (code == null) {
			throw new JOPAPlaygroundException("commands is null");
		}

		setCode(code);
		// this.resources.clear();
		// this.resources.addAll(Arrays.asList(resources));
	}

	public String getCode() {
		return plainCode;
	}

	public void setCode(String code) {
		plainCode = code;
		if (commands != null) {
			commands.clear();
		}
		commands = new ArrayList<String>(Arrays.asList(code.split("\n")));
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

	public void addResource(JOPAResource resource) {
		JOPAResource foundResource = getResourceByName(resource.name);
		if (foundResource != null) {
			resources.remove(foundResource);
		}
		resources.add(resource);
		System.out.println("Adding: " + resource.name);
	}

	public void forEachResource(Consumer<JOPAResource> consumer) {
		resources.forEach(consumer);
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
		window = createWindow(500, 500, false, this);
		glUseProgram(createProgram(loadFragmentShader("fragment.glsl")));
	}

	private boolean defaultFragmentShaderTick() {
		return tick(window, this);
	}

	private void defaultFragmentShaderDeinit() {
		destroyWindow(window);
	}

	private void defaultComputeShaderInit() {
		window = createWindow(500, 500, false, this);

		int[] windowSize = getWindowSize(window);

		int format = getTextureFormat(4, float.class, false);
		singleImage = createTexture(windowSize[0], windowSize[1], format);

		int defaultProgram = createProgram(loadComputeShader("compute.glsl"));
		glUseProgram(defaultProgram);
		glBindImageTexture(0, singleImage.handle, 0, false, 0, GL_WRITE_ONLY, format);
		glDispatchCompute(windowSize[0] / 2, windowSize[1] / 2, 1);
	}

	private boolean defaultComputeShaderTick() {
		return tick(window, this);
	}

	private void defaultComputeShaderDeinit() {
		destroyWindow(window);
	}

}