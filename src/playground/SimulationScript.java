package playground;

import static app.Main.currentProject;
import static io.Loader.loadStandardScript;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.GL_RGBA32F;
import static org.lwjgl.opengl.GL30.glBindBufferBase;
import static org.lwjgl.opengl.GL42.glBindImageTexture;
import static org.lwjgl.opengl.GL43.GL_COMPUTE_SHADER;
import static org.lwjgl.opengl.GL43.GL_SHADER_STORAGE_BUFFER;
import static util.OGLUtil.checkWindow;
import static util.OGLUtil.closeWindow;
import static util.OGLUtil.compute;
import static util.OGLUtil.createBuffer;
import static util.OGLUtil.createProgram;
import static util.OGLUtil.createShader;
import static util.OGLUtil.createTexture;
import static util.OGLUtil.createWindow;
import static util.OGLUtil.deleteBuffer;
import static util.OGLUtil.deleteContext;
import static util.OGLUtil.deleteProgram;
import static util.OGLUtil.deleteShader;
import static util.OGLUtil.deleteTexture;
import static util.OGLUtil.draw;
import static util.OGLUtil.getTextureFormat;
import static util.OGLUtil.getWindowSize;
import static util.OGLUtil.loadComputeShader;
import static util.OGLUtil.loadFragmentShader;
import static util.OGLUtil.loadTexture;
import static util.TypeUtil.getTypeForName;
import static util.TypeUtil.getTypeSize;
import static util.TypeUtil.getValueForType;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

import graphics.Image;
import types.Buffer;
import types.GLSLType;
import types.ProjectType;
import types.Resource;
import types.ResourceType;

public class SimulationScript implements Serializable {

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
	private ArrayList<Resource> resources;

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

		Resource windowResource = new Resource(ResourceType.WINDOW_HANDLE, name, windowHandle);

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
		Resource windowResource = getResourceByName(windowName);
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
		Image image = createTexture(size[0], size[1], getTextureFormat(4, float.class, false));
		checkForError("new texture");
		if (image == null) {
			logSimulationError(this, "Texture was not created", textureName);

			return false;
		}

		Resource textureResource = new Resource(ResourceType.IMAGE, textureName, image);
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
		ByteBuffer nativeBuffer = ByteBuffer.allocate(size);
		int bufferHandle = createBuffer(nativeBuffer);
		Buffer buffer = new Buffer();
		buffer.handle = bufferHandle;
		buffer.type = typeName;
		buffer.length = count;

		checkForError("new buffer");
		if (bufferHandle == 0)

		{
			logSimulationError(this, "Buffer was not created1", name);

			return false;
		}

		Resource bufferResource = new Resource(ResourceType.BUFFER, name, buffer);

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

		Resource shaderResource = new Resource(ResourceType.SHADER, name, shader);
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
			Resource shaderResource = getResourceByName(shaderName);
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

		Resource programResource = new Resource(ResourceType.PROGRAM, programName, program);
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

		Image image = loadTexture(fileName);
		checkForError("load texture");
		if (image == null) {
			logSimulationError(this, "Could not load texture from file", fileName);

			return false;
		}

		Resource imageResource = new Resource(ResourceType.IMAGE, name, image);
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
			shader = 0;
		}
		if (shader == 0) {
			logSimulationError(this, "Shader was not compiled", shaderCode);

			return false;
		}

		Resource shaderResource = new Resource(ResourceType.SHADER, shaderName, shader);
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
			Resource programResource = getResourceByName(programName);
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
		GLSLType type = getTypeForName(typeString);
		if (type == GLSLType.NONE) {
			logSimulationError(this, "Unknown type", typeString);

			return false;
		}
		if (type == GLSLType.VOID) {
			logSimulationError(this, "void is not a variable type", typeString);

			return false;
		}
		String valueString = args[2];
		Resource valueVariable = getResourceByName(valueString);
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

		Resource variableResource = new Resource(type, name, value);
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
		Object value = getValueForType(GLSLType.BOOL, valueString);
		if (value == null) {
			logSimulationError(this, "Value is incorrect", valueString);

			return false;
		}

		Resource variableResource = new Resource(GLSLType.BOOL, name, value);
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
		Object value = getValueForType(GLSLType.INT, valueString);
		if (value == null) {
			logSimulationError(this, "Value is incorrect", valueString);

			return false;
		}

		Resource variableResource = new Resource(GLSLType.INT, name, value);
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
		Object value = getValueForType(GLSLType.UINT, valueString);
		if (value == null) {
			logSimulationError(this, "Value is incorrect", valueString);

			return false;
		}

		Resource variableResource = new Resource(GLSLType.UINT, name, value);
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
		Object value = getValueForType(GLSLType.FLOAT, valueString);
		if (value == null) {
			logSimulationError(this, "Value is incorrect", valueString);

			return false;
		}

		Resource variableResource = new Resource(GLSLType.FLOAT, name, value);
		addResource(variableResource);

		return true;
	};

	private transient final Predicate<String[]> SET_LABEL_OPERATION = args -> {
		if (args.length != 1) {
			logSimulationError(this, "SET LABEL uses 1 argument (name)", args);

			return false;
		}

		String labelName = args[0];
		Resource labelResource = new Resource(ResourceType.LABEL, labelName, commandIndex);
		addResource(labelResource);

		return true;
	};

	private transient final Predicate<String[]> GO_TO_OPERATION = args -> {
		if (args.length != 1) {
			logSimulationError(this, "GO TO uses 1 argument (name)", args);

			return false;
		}

		String labelName = args[0];
		Resource labelResource = getResourceByName(labelName);
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
		Resource textureResource = getResourceByName(textureName);
		if (textureResource == null) {
			logSimulationError(this, "Texture resource was not found", textureName);

			return false;
		}
		Image image = textureResource.getAsImage();
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
			logSimulationError(this, "\"binding index\" should be unsigned integer", indexString);

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
		Resource bufferResource = getResourceByName(bufferName);
		if (bufferResource == null) {
			logSimulationError(this, "Buffer resource was not found", bufferName);

			return false;
		}
		Buffer buffer = bufferResource.getAsBuffer();
		if (buffer == null) {
			logSimulationError(this, "Buffer variable is NULL", bufferName);

			return false;
		}
		int bufferHandle = buffer.handle;
		if (bufferHandle == 0) {
			logSimulationError(this, "Buffer handle is 0", buffer);

			return false;
		}
		String indexString = args[1];
		int index;
		try {
			index = Integer.parseUnsignedInt(indexString);
		} catch (NumberFormatException e) {
			logSimulationError(this, "\"binding index\" should be unsigned integer", indexString);

			return false;
		}

		glBindBufferBase(GL_SHADER_STORAGE_BUFFER, index, bufferHandle);
		checkForError("bind buffer");

		return true;
	};

	private transient final Predicate<String[]> DRAW_OPERATION = args -> {
		if (args.length != 1) {
			logSimulationError(this, "DRAW uses 1 argument (window)", args);

			return false;
		}

		String windowName = args[0];
		Resource windowResource = getResourceByName(windowName);
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
		Resource timeResource = getResourceByName("time");
		if (timeResource == null) {
			logSimulationError(this, "Could not find resource \"time\"", resources);

			return false;
		}
		Resource deltaTimeResource = getResourceByName("deltaTime");
		if (deltaTimeResource == null) {
			logSimulationError(this, "Could not find resource \"deltaTime\"", resources);

			return false;
		}
		timeResource.setValue(currentTime / 1000.0f);
		deltaTimeResource.setValue(deltaTime);
		draw(windowHandle, this);
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
		Resource windowResource = getResourceByName(windowName);
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
		boolean result = compute(xGroups, yGroups, zGroups, this);

		checkForError("compute");
		if (!result) {
			logSimulationError(this, "Compute call failed", args);

			return false;
		}

		return true;
	};

	private transient final Predicate<String[]> CLOSE_WINDOW_OPERATION = args -> {
		if (args.length != 1) {
			logSimulationError(this, "CLOSE WINDOW uses 1 argument (window)", args);

			return false;
		}

		String name = args[0];
		Resource resource = getResourceByName(name);
		if (resource.type != ResourceType.WINDOW_HANDLE) {
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

	private transient final Predicate<String[]> DELETE_TEXTURE_OPERATION = args -> {
		if (args.length != 1) {
			logSimulationError(this, "DELETE TEXTURE uses 1 argument (texture)", args);

			return false;
		}

		String name = args[0];
		Resource textureResource = getResourceByName(name);
		if (textureResource == null) {
			logSimulationError(this, "Texture resource was not found", textureResource);

			return false;
		}
		Image image = textureResource.getAsImage();
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

	private transient final Predicate<String[]> DELETE_BUFFER_OPERATION = args -> {
		if (args.length != 1) {
			logSimulationError(this, "DELETE BUFFER uses 1 argument (buffer)", args);

			return false;
		}

		String bufferName = args[0];
		Resource bufferResource = getResourceByName(bufferName);
		if (bufferResource == null) {
			logSimulationError(this, "Buffer resource was not found", bufferResource);

			return false;
		}
		Buffer buffer = bufferResource.getAsBuffer();
		if (buffer == null) {
			logSimulationError(this, "Buffer variable is NULL", bufferName);

			return false;
		}
		int bufferHandle = buffer.handle;
		if (bufferHandle == 0) {
			logSimulationError(this, "Buffer handle is 0", buffer);

			return false;
		}
		boolean result = deleteBuffer(bufferHandle);
		checkForError("delete buffer");
		if (!result) {
			logSimulationError(this, "Buffer variable was not deleted", bufferName);

			return false;
		}

		return true;
	};

	private transient final Predicate<String[]> DELETE_SHADER_OPERATION = args -> {
		if (args.length != 1) {
			logSimulationError(this, "DELETE SHADER uses 1 argument (shader)", args);

			return false;
		}

		String shaderName = args[0];
		Resource shaderResource = getResourceByName(shaderName);
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
			logSimulationError(this, "DELETE PROGRAM uses 1 argument (program)", args);

			return false;
		}

		String programName = args[0];
		Resource programResource = getResourceByName(programName);
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

			return false;
		}

		deleteContext();
		checkForError("exit");

		return false;
	};

	private SimulationScript() {
		this.commands = new ArrayList<String>();
		this.resources = new ArrayList<Resource>();
	}

	private void logSimulationError(Object source, String errorMessage, Object object) {
		System.err.println("[SCRIPT] " + source.getClass().getSimpleName() + ": " + errorMessage);
		// System.err.println("[SCRIPT] " + object);
	}

	public static SimulationScript create(ProjectType type) {
		if (type == null) {
			return null;
		}

		System.out.println("[SCRIPT] Creating with type: " + type);
		SimulationScript script = new SimulationScript();
		switch (type) {
		case FRAGMENT:
			script.setupScript(loadStandardScript("frag.script"));
			break;
		case COMPUTE:
			script.setupScript(loadStandardScript("comp.script"));
			break;
		default:
			break;
		}

		return script;
	}

	public static SimulationScript createCustom(String customScriptFileName) {
		if (customScriptFileName == null) {
			return null;
		}

		System.out.println("[SCRIPT] Creating custom script");
		SimulationScript script = new SimulationScript();
		script.setupScript(loadStandardScript(customScriptFileName));

		return script;
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

	private void init() {
		operations = new HashMap<String[], Predicate<String[]>>();
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
		operations.put(DRAW, DRAW_OPERATION);
		operations.put(CHECK, CHECK_OPERATION);
		operations.put(COMPUTE, COMPUTE_OPERATION);
		operations.put(CLOSE_WINDOW, CLOSE_WINDOW_OPERATION);
		operations.put(DELETE_TEXTURE, DELETE_TEXTURE_OPERATION);
		operations.put(DELETE_BUFFER, DELETE_BUFFER_OPERATION);
		operations.put(DELETE_SHADER, DELETE_SHADER_OPERATION);
		operations.put(DELETE_PROGRAM, DELETE_PROGRAM_OPERATION);
		operations.put(EXIT, EXIT_OPERATION);

		Resource timeResource = new Resource(GLSLType.FLOAT, "time", 0.0f);
		addResource(timeResource);
		Resource deltaTimeResource = new Resource(GLSLType.FLOAT, "deltaTime", 0.0f);
		addResource(deltaTimeResource);
	}

	public boolean setupScript(String code) {
		if (code == null) {
			return false;
		}

		setCode(code);
		// System.out.println("[SCRIPT] Set up with " + commands.size() + " lines");

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
		commandIndex++;
		// System.out.println("[SCRIPT] Command: " + command);
		if (command.startsWith("#")) {
			return true;
		}
		if (command.length() > 1) {
			boolean result = executeCommand(command);

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
				System.out.println("[SCRIPT] Context: " + context[0]);
			}
			System.out.println("[SCRIPT] Error code: " + error);

			return true;
		}

		return false;
	}

	private boolean executeCommand(String command) {
		boolean containsBraces = command.contains("(") || command.contains(")");

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

	private Resource getResourceByName(String name) {
		for (Resource resource : resources) {
			if (resource.name.equals(name)) {
				return resource;
			}
		}

		return null;
	}

	public void addResource(Resource resource) {
		Resource foundResource = getResourceByName(resource.name);
		if (foundResource != null) {
//			System.out.println("[SCRIPT] Updating resource: " + resource.name);
			resources.remove(foundResource);
		} else {
			System.out.println("[SCRIPT] Adding resource: " + resource.name);
		}
		resources.add(resource);
	}

	public void forEachResource(Consumer<Resource> consumer) {
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