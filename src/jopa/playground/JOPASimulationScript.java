package jopa.playground;

import static jopa.io.JOPALoader.loadStandardScript;
import static jopa.main.JOPAMain.currentProject;
import static jopa.util.JOPAOGLUtil.checkWindow;
import static jopa.util.JOPAOGLUtil.closeWindow;
import static jopa.util.JOPAOGLUtil.compute;
import static jopa.util.JOPAOGLUtil.createBuffer;
import static jopa.util.JOPAOGLUtil.createProgram;
import static jopa.util.JOPAOGLUtil.createShader;
import static jopa.util.JOPAOGLUtil.createTexture;
import static jopa.util.JOPAOGLUtil.createWindow;
import static jopa.util.JOPAOGLUtil.deleteBuffer;
import static jopa.util.JOPAOGLUtil.deleteContext;
import static jopa.util.JOPAOGLUtil.deleteProgram;
import static jopa.util.JOPAOGLUtil.deleteShader;
import static jopa.util.JOPAOGLUtil.deleteTexture;
import static jopa.util.JOPAOGLUtil.getTextureFormat;
import static jopa.util.JOPAOGLUtil.getWindowSize;
import static jopa.util.JOPAOGLUtil.loadComputeShader;
import static jopa.util.JOPAOGLUtil.loadFragmentShader;
import static jopa.util.JOPAOGLUtil.loadTexture;
import static jopa.util.JOPAOGLUtil.tick;
import static jopa.util.JOPATypeUtil.getTypeForName;
import static jopa.util.JOPATypeUtil.getTypeSize;
import static jopa.util.JOPATypeUtil.getValueForType;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.GL_RGBA32F;
import static org.lwjgl.opengl.GL30.glBindBufferBase;
import static org.lwjgl.opengl.GL42.glBindImageTexture;
import static org.lwjgl.opengl.GL43.GL_COMPUTE_SHADER;
import static org.lwjgl.opengl.GL43.GL_SHADER_STORAGE_BUFFER;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

import jopa.graphics.JOPAImage;
import jopa.types.JOPAGLSLType;
import jopa.types.JOPAProjectType;
import jopa.types.JOPAResource;
import jopa.types.JOPAResourceType;

public class JOPASimulationScript implements Serializable {

	private static final long serialVersionUID = -5030460984321077507L;

	private transient static final String[] NEW_WINDOW = { "new", "window" };
	private transient static final String[] NEW_TEXTURE = { "new", "texture" };
	private transient static final String[] NEW_BUFFER = { "new", "buffer" };
	private transient static final String[] NEW_SHADER = { "new", "shader" };
	private transient static final String[] NEW_PROGRAM = { "new", "program" };
	private transient static final String[] LOAD_TEXTURE = { "load", "texture" };
	private transient static final String[] GENERATE_SHADER = { "generate", "shader" };
	private transient static final String[] SET_PROGRAM = { "set", "program" };
	private transient static final String[] SET_VAR = { "set", "var" };
	private transient static final String[] SET_BOOL = { "set", "bool" };
	private transient static final String[] SET_INT = { "set", "int" };
	private transient static final String[] SET_UINT = { "set", "uint" };
	private transient static final String[] SET_FLOAT = { "set", "float" };
	private transient static final String[] SET_LABEL = { "set", "label" };
	private transient static final String[] GO_TO = { "go", "to" };
	private transient static final String[] BIND_IMAGE = { "bind", "image" };
	private transient static final String[] BIND_BUFFER = { "bind", "buffer" };
	private transient static final String[] DRAW = { "draw" };
	private transient static final String[] CHECK = { "check" };
	private transient static final String[] COMPUTE = { "compute" };
	private transient static final String[] CLOSE_WINDOW = { "close", "window" };
	private transient static final String[] DELETE_TEXTURE = { "destroy", "texture" };
	private transient static final String[] DELETE_BUFFER = { "destroy", "buffer" };
	private transient static final String[] DELETE_SHADER = { "delete", "shader" };
	private transient static final String[] DELETE_PROGRAM = { "delete", "program" };
	private transient static final String[] EXIT = { "exit" };

	private transient long startTime;
	private transient long prevTime;
	private transient int commandIndex;
	private transient Predicate<String[]> foundOperation;
	private transient Map<String[], Predicate<String[]>> operations;
	private String plainCode;
	private ArrayList<String> commands;
	private ArrayList<JOPAResource> resources;

	private transient final Predicate<String[]> NEW_WINDOW_OPERATION = args -> {
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

		checkForError("new window");

		JOPAResource windowResource = new JOPAResource(JOPAResourceType.WINDOW_HANDLE, name, windowHandle);

		addResource(windowResource);

		return true;
	};

	private transient final Predicate<String[]> NEW_TEXTURE_OPERATION = args -> {
		if (args.length != 2) {
			logSimulationError(this, "CREATE TEXTURE uses 2 arguments (texture name, window name)", args);

			return false;
		}

		String textureName = args[0];
		String windowName = args[1];
		JOPAResource windowResource = getResourceByName(windowName);
		if (windowResource == null) {
			logSimulationError(this, "Window resource was not found", windowName);

			return false;
		}
		long windowHandle = windowResource.getAsWindow();
		if (windowHandle == 0) {
			logSimulationError(this, "Window handle is 0", windowResource);

			return false;
		}
		int[] size = getWindowSize(windowHandle);
		if (size == null) {
			logSimulationError(this, "Could not get window size", windowHandle);

			return false;
		}
		JOPAImage image = createTexture(size[0], size[1], getTextureFormat(4, float.class, false));
		checkForError("new texture");
		if (image == null) {
			logSimulationError(this, "Texture was not created", textureName);

			return false;
		}

		JOPAResource textureResource = new JOPAResource(JOPAResourceType.IMAGE, textureName, image);
		addResource(textureResource);

		return true;
	};

	private transient final Predicate<String[]> NEW_BUFFER_OPERATION = args -> {
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

		checkForError("new buffer");
		if (buffer == 0)

		{
			logSimulationError(this, "Buffer was not created1", name);

			return false;
		}

		JOPAResource bufferResource = new JOPAResource(JOPAResourceType.BUFFER_HANDLE, name, buffer);

		addResource(bufferResource);

		return true;
	};

	private transient final Predicate<String[]> NEW_SHADER_OPERATION = args -> {
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
			checkForError("new shader");
			break;
		case "compute":
			shader = loadComputeShader(file);
			checkForError("new shader");
			break;
		default:
			logSimulationError(this, "Shader type should be either \"fragment\" or \"compute\"", type);

			return false;
		}
		if (shader == 0) {
			logSimulationError(this, "Could not create shader from file", file);

			return false;
		}

		JOPAResource shaderResource = new JOPAResource(JOPAResourceType.SHADER, name, shader);
		addResource(shaderResource);

		return true;
	};

	private transient final Predicate<String[]> NEW_PROGRAM_OPERATION = args -> {
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
		checkForError("new program");
		if (program == 0) {
			logSimulationError(this, "Program could not be linked", programName);

			return false;
		}

		JOPAResource programResource = new JOPAResource(JOPAResourceType.PROGRAM, programName, program);
		addResource(programResource);

		return true;
	};

	private transient final Predicate<String[]> LOAD_TEXTURE_OPERATION = args -> {
		if (args.length != 2) {
			logSimulationError(this, "LOAD TEXTURE uses 2 arguments (name, file name)", args);

			return false;
		}
		String name = args[0];
		String fileName = args[1];

		JOPAImage image = loadTexture(fileName);
		checkForError("load texture");
		if (image == null) {
			logSimulationError(this, "Could not load texture from file", fileName);

			return false;
		}

		JOPAResource imageResource = new JOPAResource(JOPAResourceType.IMAGE, name, image);
		addResource(imageResource);

		return true;
	};

	private transient final Predicate<String[]> GENERATE_SHADER_OPERATION = args -> {
		if (args.length != 1) {
			logSimulationError(this, "GENERATE SHADER uses 1 argument (name)", args);

			return false;
		}

		String shaderName = args[0];
		String shaderCode = currentProject.getGeneratedShader();
		if (shaderCode == null) {
			logSimulationError(this, "Shader code could not be generated", shaderName);

			return false;
		}
		int shader;
		if (currentProject == null) {
			logSimulationError(this, "Project is not loaded", null);

			return false;
		}
		switch (currentProject.projectType) {
		case FRAGMENT:
			shader = createShader(GL_FRAGMENT_SHADER, shaderCode);
			checkForError("generate shader");
			break;
		case COMPUTE:
			shader = createShader(GL_COMPUTE_SHADER, shaderCode);
			checkForError("generate shader");
			break;
		default:
			// HACK gonna replace this
			shader = createShader(GL_FRAGMENT_SHADER, shaderCode);
			if (shader == 0) {
				shader = createShader(GL_COMPUTE_SHADER, shaderCode);
			}
			// logSimulationError(this, "Shader type is not set up in the project",
			// currentProject);

			// return false;
		}
		if (shader == 0) {
			logSimulationError(this, "Shader was not compiled", shaderCode);

			return false;
		}

		JOPAResource shaderResource = new JOPAResource(JOPAResourceType.SHADER, shaderName, shader);
		addResource(shaderResource);

		return true;
	};

	private transient final Predicate<String[]> SET_PROGRAM_OPERATION = args -> {
		if (args.length != 1) {
			logSimulationError(this, "SET PROGRAM uses 1 argument (name or 0)", args);

			return false;
		}

		String programName = args[0];
		int program;
		if (!programName.equals("0")) {
			JOPAResource programResource = getResourceByName(programName);
			if (programResource == null) {
				logSimulationError(this, "Program was not found", programName);

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
		checkForError("set program");

		return true;
	};

	private transient final Predicate<String[]> SET_VAR_OPERATION = args -> {
		if (args.length != 3) {
			logSimulationError(this, "SET VAR uses 3 arguments (name, type, value)", args);

			return false;
		}

		String name = args[0];
		String typeString = args[1];
		JOPAGLSLType type = getTypeForName(typeString);
		if (type == JOPAGLSLType.NONE) {
			logSimulationError(this, "Unknown type", typeString);

			return false;
		}
		if (type == JOPAGLSLType.VOID) {
			logSimulationError(this, "void is not a variable type", typeString);

			return false;
		}
		String valueString = args[2];
		JOPAResource valueVariable = getResourceByName(valueString);
		Object value;
		if (valueVariable != null) {
			if (valueVariable.glslType == type) {
				value = valueVariable.getAsGLSLType();
				if (value == null) {
					logSimulationError(this, "Value is NULL", value);

					return false;
				}
			} else {
				logSimulationError(this, "Types do not match", valueVariable);

				return false;
			}
		} else {
			value = getValueForType(type, valueString);
			if (value == null) {
				logSimulationError(this, "Value is incorrect", valueString);

				return false;
			}
		}

		JOPAResource variableResource = new JOPAResource(type, name, value);
		addResource(variableResource);

		return true;
	};

	private transient final Predicate<String[]> SET_BOOL_OPERATION = args -> {
		if (args.length != 2) {
			logSimulationError(this, "SET BOOL uses 2 arguments (name, value)", args);

			return false;
		}

		String name = args[0];
		String valueString = args[1];
		Object value = getValueForType(JOPAGLSLType.BOOL, valueString);
		if (value == null) {
			logSimulationError(this, "Value is incorrect", valueString);

			return false;
		}

		JOPAResource variableResource = new JOPAResource(JOPAGLSLType.BOOL, name, value);
		addResource(variableResource);

		return true;
	};

	private transient final Predicate<String[]> SET_INT_OPERATION = args -> {
		if (args.length != 2) {
			logSimulationError(this, "SET INT uses 2 arguments (name, value)", args);

			return false;
		}

		String name = args[0];
		String valueString = args[1];
		Object value = getValueForType(JOPAGLSLType.INT, valueString);
		if (value == null) {
			logSimulationError(this, "Value is incorrect", valueString);

			return false;
		}

		JOPAResource variableResource = new JOPAResource(JOPAGLSLType.INT, name, value);
		addResource(variableResource);

		return true;
	};

	private transient final Predicate<String[]> SET_UINT_OPERATION = args -> {
		if (args.length != 2) {
			logSimulationError(this, "SET UINT uses 2 arguments (name, value)", args);

			return false;
		}

		String name = args[0];
		String valueString = args[1];
		Object value = getValueForType(JOPAGLSLType.UINT, valueString);
		if (value == null) {
			logSimulationError(this, "Value is incorrect", valueString);

			return false;
		}

		JOPAResource variableResource = new JOPAResource(JOPAGLSLType.UINT, name, value);
		addResource(variableResource);

		return true;
	};

	private transient final Predicate<String[]> SET_FLOAT_OPERATION = args -> {
		if (args.length != 2) {
			logSimulationError(this, "SET FLOAT uses 2 arguments (name, value)", args);

			return false;
		}

		String name = args[0];
		String valueString = args[1];
		Object value = getValueForType(JOPAGLSLType.FLOAT, valueString);
		if (value == null) {
			logSimulationError(this, "Value is incorrect", valueString);

			return false;
		}

		JOPAResource variableResource = new JOPAResource(JOPAGLSLType.FLOAT, name, value);
		addResource(variableResource);

		return true;
	};

	private transient final Predicate<String[]> SET_LABEL_OPERATION = args -> {
		if (args.length != 1) {
			logSimulationError(this, "SET LABEL uses 1 argument (name)", args);

			return false;
		}

		String labelName = args[0];
		JOPAResource labelResource = new JOPAResource(JOPAResourceType.LABEL, labelName, commandIndex);
		addResource(labelResource);

		return true;
	};

	private transient final Predicate<String[]> GO_TO_OPERATION = args -> {
		if (args.length != 1) {
			logSimulationError(this, "GO TO uses 1 argument (name)", args);

			return false;
		}

		String labelName = args[0];
		JOPAResource labelResource = getResourceByName(labelName);
		if (labelResource == null) {
			logSimulationError(this, "Label resource was not found", labelName);

			return false;
		}
		int index = labelResource.getAsLabel();
		if (index == -1) {
			logSimulationError(this, "Label instruction index is -1", labelResource);

			return false;
		}
		commandIndex = index;

		return true;
	};

	private transient final Predicate<String[]> BIND_IMAGE_OPERATION = args -> {
		if (args.length != 2) {
			logSimulationError(this, "BIND IMAGE uses 2 arguments (texture name, binding index)", args);

			return false;
		}

		String textureName = args[0];
		JOPAResource textureResource = getResourceByName(textureName);
		if (textureResource == null) {
			logSimulationError(this, "Texture resource was not found", textureName);

			return false;
		}
		JOPAImage image = textureResource.getAsImage();
		if (image == null) {
			logSimulationError(this, "Image resource is NULL", textureResource);

			return false;
		}
		if (image.handle == 0) {
			logSimulationError(this, "Image handle is 0", image);

			return false;
		}
		String indexString = args[1];
		int index;
		try {
			index = Integer.parseUnsignedInt(indexString);
		} catch (NumberFormatException e) {
			logSimulationError(this, "\"binding index\" should be positive unsigned integer", indexString);

			return false;
		}

		glBindImageTexture(index, image.handle, 0, false, 0, GL_WRITE_ONLY, GL_RGBA32F);
		checkForError("bind image");

		return true;
	};

	private transient final Predicate<String[]> BIND_BUFFER_OPERATION = args -> {
		if (args.length != 2) {
			logSimulationError(this, "BIND BUFFER uses 2 arguments (buffer name, binding index)", args);

			return false;
		}

		String bufferName = args[0];
		JOPAResource bufferResource = getResourceByName(bufferName);
		if (bufferResource == null) {
			logSimulationError(this, "Buffer resource was not found", bufferName);

			return false;
		}
		int bufferHandle = bufferResource.getAsBuffer();
		if (bufferHandle == 0) {
			logSimulationError(this, "Buffer handle is 0", bufferName);

			return false;
		}
		String indexString = args[1];
		int index;
		try {
			index = Integer.parseUnsignedInt(indexString);
		} catch (NumberFormatException e) {
			logSimulationError(this, "\"binding index\" should be positive unsigned integer", indexString);

			return false;
		}
		if (index == 0) {
			logSimulationError(this, "\"binding index\" is 0", indexString);

			return false;
		}

		glBindBufferBase(GL_SHADER_STORAGE_BUFFER, index, bufferHandle);
		checkForError("bind buffer");

		return true;
	};

	private transient final Predicate<String[]> DRAW_PREDICATE = args -> {
		if (args.length != 1) {
			logSimulationError(this, "DRAW uses 1 argument (window)", args);

			return false;
		}

		String windowName = args[0];
		JOPAResource windowResource = getResourceByName(windowName);
		if (windowResource == null) {
			logSimulationError(this, "Window resource was not found", windowName);

			return false;
		}
		long windowHandle = windowResource.getAsWindow();
		if (windowHandle == 0) {
			logSimulationError(this, "Window variable is NULL", windowResource);

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
		tick(windowHandle, this);
		checkForError("draw");
		prevTime = currentTime;

		return true;
	};

	private transient final Predicate<String[]> CHECK_OPERATION = args -> {
		if (args.length != 1) {
			logSimulationError(this, "CHECK uses 1 argument (window)", args);

			return false;
		}

		String windowName = args[0];
		JOPAResource windowResource = getResourceByName(windowName);
		if (windowResource == null) {
			logSimulationError(this, "Window resource was not found", windowName);

			return false;
		}
		long windowHandle = windowResource.getAsWindow();
		if (windowHandle == 0) {
			logSimulationError(this, "Window variable is NULL", windowResource);

			return false;
		}
		boolean result = checkWindow(windowHandle);
		checkForError("check");
		if (!result) {
			return false;
		}

		return true;
	};

	private transient final Predicate<String[]> COMPUTE_OPERATION = args -> {
		if (args.length != 3) {
			logSimulationError(this, "COMPUTE uses 3 args (x groups, y groups, z groups)", args);

			return false;
		}

		String xString = args[0];
		int xGroups;
		try {
			xGroups = Integer.parseInt(xString);
		} catch (NumberFormatException e) {
			logSimulationError(this, "\"x groups\" should be positive unsigned integer", xString);

			return false;
		}
		if (xGroups == 0) {
			logSimulationError(this, "\"x groups\" is 0", xString);

			return false;
		}
		String yString = args[1];
		int yGroups;
		try {
			yGroups = Integer.parseInt(yString);
		} catch (NumberFormatException e) {
			logSimulationError(this, "\"y groups\" should be positive unsigned integer", yString);

			return false;
		}
		if (yGroups == 0) {
			logSimulationError(this, "\"y groups\" is 0", yString);

			return false;
		}
		String zString = args[2];
		int zGroups;
		try {
			zGroups = Integer.parseInt(zString);
		} catch (NumberFormatException e) {
			logSimulationError(this, "\"z groups\" should be positive unsigned integer", zString);

			return false;
		}
		if (zGroups == 0) {
			logSimulationError(this, "\"z groups\" is 0", zString);

			return false;
		}
		boolean result = compute(xGroups, yGroups, zGroups);

		checkForError("compute");
		if (!result) {
			logSimulationError(this, "Compute call failed", args);

			return false;
		}

		return true;
	};

	private transient final Predicate<String[]> CLOSE_WINDOW_PREDICATE = args -> {
		if (args.length != 1) {
			logSimulationError(this, "CLOSE WINDOW uses 1 argument (window)", args);

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
		closeWindow(windowHandle);
		checkForError("close window");
		resources.remove(resource);

		return true;
	};

	private transient final Predicate<String[]> DELETE_TEXTURE_PREDICATE = args -> {
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
		boolean result = deleteTexture(image);
		checkForError("delete texture");
		if (!result) {
			logSimulationError(this, "Texture was not deleted", image);

			return false;
		}

		return true;
	};

	private transient final Predicate<String[]> DELETE_BUFFER_PREDICATE = args -> {
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
		boolean result = deleteBuffer(buffer);
		checkForError("delete buffer");
		if (!result) {
			logSimulationError(this, "Buffer variable was not deleted", bufferName);

			return false;
		}

		return true;
	};

	private transient final Predicate<String[]> DELETE_SHADER_OPERATION = args -> {
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
		boolean result = deleteShader(shader);
		checkForError("delete shader");
		if (!result) {
			logSimulationError(this, "Shader was not deleted", shaderName);

			return false;
		}

		return true;
	};

	private transient final Predicate<String[]> DELETE_PROGRAM_OPERATION = args -> {
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
		boolean result = deleteProgram(program);
		checkForError("delete program");
		if (!result) {
			logSimulationError(this, "Program was not deleted", programName);

			return false;
		}

		return true;
	};

	private transient final Predicate<String[]> EXIT_OPERATION = args -> {
		if (args.length != 0) {
			logSimulationError(this, "EXIT uses 0 arguments", args);
		}

		deleteContext();
		checkForError("exit");

		return false;
	};

	private JOPASimulationScript() {
		this.commands = new ArrayList<String>();
		this.resources = new ArrayList<JOPAResource>();
	}

	public void init() {
		operations = new HashMap<String[], Predicate<String[]>>();
		operations.clear();
		operations.put(NEW_WINDOW, NEW_WINDOW_OPERATION);
		operations.put(NEW_TEXTURE, NEW_TEXTURE_OPERATION);
		operations.put(NEW_BUFFER, NEW_BUFFER_OPERATION);
		operations.put(NEW_SHADER, NEW_SHADER_OPERATION);
		operations.put(NEW_PROGRAM, NEW_PROGRAM_OPERATION);
		operations.put(LOAD_TEXTURE, LOAD_TEXTURE_OPERATION);
		operations.put(GENERATE_SHADER, GENERATE_SHADER_OPERATION);
		operations.put(SET_PROGRAM, SET_PROGRAM_OPERATION);
		operations.put(SET_VAR, SET_VAR_OPERATION);
		operations.put(SET_BOOL, SET_BOOL_OPERATION);
		operations.put(SET_INT, SET_INT_OPERATION);
		operations.put(SET_UINT, SET_UINT_OPERATION);
		operations.put(SET_FLOAT, SET_FLOAT_OPERATION);
		operations.put(SET_LABEL, SET_LABEL_OPERATION);
		operations.put(GO_TO, GO_TO_OPERATION);
		operations.put(BIND_IMAGE, BIND_IMAGE_OPERATION);
		operations.put(BIND_BUFFER, BIND_BUFFER_OPERATION);
		operations.put(DRAW, DRAW_PREDICATE);
		operations.put(CHECK, CHECK_OPERATION);
		operations.put(COMPUTE, COMPUTE_OPERATION);
		operations.put(CLOSE_WINDOW, CLOSE_WINDOW_PREDICATE);
		operations.put(DELETE_TEXTURE, DELETE_TEXTURE_PREDICATE);
		operations.put(DELETE_BUFFER, DELETE_BUFFER_PREDICATE);
		operations.put(DELETE_SHADER, DELETE_SHADER_OPERATION);
		operations.put(DELETE_PROGRAM, DELETE_PROGRAM_OPERATION);
		operations.put(EXIT, EXIT_OPERATION);

		JOPAResource timeResource = new JOPAResource(JOPAGLSLType.FLOAT, "time", 0.0f);
		addResource(timeResource);
		JOPAResource deltaTimeResource = new JOPAResource(JOPAGLSLType.FLOAT, "deltaTime", 0.0f);
		addResource(deltaTimeResource);
	}

	private void logSimulationError(Object source, String errorMessage, Object object) {
		System.err.println("[SCRIPT] " + source.getClass().getSimpleName() + ": " + errorMessage);
		// System.err.println("[SCRIPT] " + object);
	}

	public boolean start() {
		startTime = System.currentTimeMillis();
		commandIndex = 0;
		init();
		if (commands.size() == 0) {
			return false;
		}

		return true;
	}

	public static JOPASimulationScript create(JOPAProjectType type) {
		// if (type == null) {
		// return null;
		// }
		System.out.println("[SCRIPT] creating with type: " + type);
		JOPASimulationScript script = new JOPASimulationScript();
		switch (type) {
		case FRAGMENT:
			script.setupScript(loadStandardScript("frag.jopascript"));
			break;
		case COMPUTE:
			script.setupScript(loadStandardScript("comp.jopascript"));
			break;
		default:
			break;
		}

		return script;
	}

	public boolean setupScript(String code) {
		if (code == null) {
			return false;
		}

		setCode(code);
		System.out.println("[SCRIPT] set up with " + commands.size() + " lines");

		return true;
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
		if (commandIndex == commands.size()) {
			return false;
		}

		String command = commands.get(commandIndex);
		// System.out.println("[SCRIPT] Command: " + command);
		if (command.startsWith("#")) {
			return true;
		}
		if (command.length() > 0) {
			boolean result = executeCommand(command);
			commandIndex++;

			if (!result) {
				if (checkForError()) {
					System.err.println("[SCRIPT] Command: " + command);
				}
			}

			return result;
		}

		return true;
	}

	public static boolean checkForError(String... context) {
		int error = glGetError();
		if (error != 0) {
			if (context.length > 0) {
				System.out.println("[SCRIPT] " + context[0]);
			}
			System.out.println("[SCRIPT] Error code: " + error);

			return true;
		}

		return false;
	}

	private boolean executeCommand(String command) {
		boolean containsBraces = command.contains("(") || command.contains(")");

		switch (command.toLowerCase()) {
		case "exit":
		case "quit":
		case "end":
			return false;
		}
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
							String[] parts;
							if (operationPart.contains(" ")) {
								parts = operationPart.split(" ");
							} else {
								parts = new String[] { operationPart };
							}
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
							Predicate<String[]> operation = getOperation(parts);
							if (operation == null) {
								System.err.println("[SCRIPT] Unknown command: " + operationPart);

								return false;
							}

							return operation.test(args);
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
			System.out.println("[SCRIPT] Updating resource: " + resource.name);
			resources.remove(foundResource);
		} else {
			System.out.println("[SCRIPT] Adding resource: " + resource.name);
		}
		resources.add(resource);
	}

	public void forEachResource(Consumer<JOPAResource> consumer) {
		resources.forEach(consumer);
	}

	public Predicate<String[]> getOperation(String[] parts) {
		foundOperation = null;
		operations.forEach((name, operation) -> {
			if (Arrays.equals(name, parts)) {
				foundOperation = operation;

				return;
			}
		});

		return foundOperation;
	}

}